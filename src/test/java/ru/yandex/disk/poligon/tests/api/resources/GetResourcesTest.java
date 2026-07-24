package ru.yandex.disk.poligon.tests.api.resources;


import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.disk.poligon.client.ResourcesClient;
import ru.yandex.disk.poligon.dto.request.ResourcesRequest;
import ru.yandex.disk.poligon.dto.response.GetResourceResponse;

import java.util.Map;
import java.util.UUID;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class GetResourcesTest {

    private final ResourcesClient client = new ResourcesClient();

    private String resourceName;
    private String resourcePath;

    @BeforeEach
    void setUpResource() {

        resourceName = "test_dir_" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 4);

        resourcePath = "/" + resourceName;

        ResourcesRequest request = ResourcesRequest.builder()
                .path(resourcePath)
                .build();

        client.createDirectory(request)
                .then()
                .statusCode(HTTP_CREATED);
    }

    @Test
    @DisplayName("Get resource by existing path")
    public void getResources_existingPath_returnsResource() {

        ResourcesRequest request = ResourcesRequest.builder()
                .path(resourcePath)
                .build();

        GetResourceResponse responseBody = client.getResources(request)
                .then()
                .statusCode(HTTP_OK)
                .extract().as(GetResourceResponse.class);

        assertEquals(
                "disk:" + resourcePath,
                responseBody.getPath(),
                "Resource path should match the requested path"
        );

        assertEquals(
                resourceName,
                responseBody.getName(),
                "Resource name should match the created directory name"
        );
    }

    @Test
    @DisplayName("Should return only requested fields when fields parameter is specified")
    public void getResources_withFields_returnsOnlyRequestedFields() {

        Response response = client.getResources(
                ResourcesRequest.builder()
                        .path(resourcePath)
                        .fields("name,path")
                        .build()
        );

        response.then()
                .statusCode(HTTP_OK)
                .body("$", aMapWithSize(2))
                .body("name", equalTo(resourceName))
                .body("path", equalTo("disk:" + resourcePath));
    }

    @Test
    @DisplayName("Return 400 when path parameter is missing")
    public void getResources_withoutPath_returns400() {

        client.getResources(
                        ResourcesRequest.builder()
                                .build()
                ).then()
                .statusCode(HTTP_BAD_REQUEST);
    }

    private static Object[][] invalidQueryParameters() {
        return new Object[][] {
                {"limit", "abc"},
                {"offset", true},
                {"preview_crop", 123},
                {"preview_size", "max"}
        };
    }

    @ParameterizedTest(name = "Invalid {0}: {1}")
    @MethodSource("invalidQueryParameters")
    @DisplayName("Return 400 for invalid query parameter types")
    public void getResources_withInvalidQueryParameterType_returns400(String parameter, Object value) {

        Map<String, Object> queryParams = Map.of(
                "path", resourcePath,
                parameter, value
        );

        client.getResources(queryParams)
                .then()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Test
    @DisplayName("Should return 401 when authorization is missing")
    public void getResources_withoutAuthorization_returns401() {

        client.getResourcesWithoutAuth(
                        ResourcesRequest.builder()
                                .path(resourcePath)
                                .build()
                ).then()
                .statusCode(HTTP_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Should return 404 when resources path is not exist")
    public void getResources_withNonExistingPath_returns404() {

        client.getResources(
                        ResourcesRequest.builder()
                                .path(resourcePath + UUID.randomUUID())
                                .build()
                ).then()
                .statusCode(HTTP_NOT_FOUND);
    }

    @AfterEach
    void deleteResource() {

        ResourcesRequest request = ResourcesRequest.builder()
                .path(resourcePath)
                .permanently(true)
                .build();

        client.deleteResources(request);
    }
}
