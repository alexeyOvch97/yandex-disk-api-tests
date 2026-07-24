package ru.yandex.disk.poligon.tests.api.resources;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.yandex.disk.poligon.client.ResourcesClient;
import ru.yandex.disk.poligon.dto.request.ResourcesRequest;

import java.util.Map;
import java.util.UUID;

import static java.net.HttpURLConnection.*;

public class CopyResourcesTest {

    private final ResourcesClient client = new ResourcesClient();

    private String parentResourcePath;
    private String newResourcePath;

    @BeforeEach
    void setUpResourcesData() {
        parentResourcePath = "/test_parent_resource_" +
                UUID.randomUUID()
                        .toString()
                        .substring(0,3);

        newResourcePath = "/test_new_resource_" +
                UUID.randomUUID()
                        .toString()
                        .substring(0,3);
    }

    private void createDirectory(String path) {

        client.createDirectory(
                        ResourcesRequest.builder()
                                .path(path)
                                .build()
                ).then()
                .statusCode(HTTP_CREATED);
    }

    @Test
    @DisplayName("Copy resource with existing path")
    public void copyResources_existingPath_copiesResource() {

        createDirectory(parentResourcePath);

        ResourcesRequest request = ResourcesRequest.builder()
                .from(parentResourcePath)
                .path(newResourcePath)
                .build();

        client.copyResources(request)
                .then()
                .statusCode(HTTP_CREATED);

        ResourcesRequest getResourceRequest = ResourcesRequest.builder()
                .path(newResourcePath)
                .build();

        client.getResources(getResourceRequest)
                .then()
                .statusCode(HTTP_OK);
    }

    @Test
    @DisplayName("Copy resource with overwrite enabled")
    void copyResources_withOverwriteTrue_returns201() {

        createDirectory(parentResourcePath);

        createDirectory(newResourcePath);

        createDirectory(newResourcePath + parentResourcePath);

        client.copyResources(
                        ResourcesRequest.builder()
                                .from(parentResourcePath)
                                .path(newResourcePath)
                                .overwrite(true)
                                .build())
                .then()
                .statusCode(HTTP_CREATED);
    }

    @Test
    @DisplayName("Copy resource with overwrite disabled")
    void copyResources_withOverwriteFalse_returns409() {

        createDirectory(parentResourcePath);

        createDirectory(newResourcePath);

        createDirectory(newResourcePath + parentResourcePath);

        client.copyResources(
                        ResourcesRequest.builder()
                                .from(parentResourcePath)
                                .path(newResourcePath)
                                .overwrite(false)
                                .build())
                .then()
                .statusCode(HTTP_CONFLICT);
    }

    @ParameterizedTest(name = "Missing parameter: {0}")
    @CsvSource({
            "from",
            "path"
    })
    @DisplayName("Return 400 when a required query parameter is missing")
    public void copyResources_withoutRequiredParameter_returns400(String parameter) {

        Map<String, Object> queryParams = Map.of(
                parameter, parentResourcePath
        );

        client.copyResources(queryParams).then()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Test
    @DisplayName("Should return 401 when authorization is missing")
    public void copyResources_withoutAuth_returns401() {

        client.copyResourcesWithoutAuth(
                        ResourcesRequest.builder()
                                .from(parentResourcePath)
                                .path(newResourcePath)
                                .overwrite(false)
                                .build())
                .then()
                .statusCode(HTTP_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Should return 404 when from path does not exist")
    public void copyResources_withNonExistingFromPath_returns404() {

        client.copyResources(
                        ResourcesRequest.builder()
                                .from(parentResourcePath)
                                .path(newResourcePath)
                                .overwrite(false)
                                .build())
                .then()
                .statusCode(HTTP_NOT_FOUND);
    }

    @AfterEach
    void deleteResources() {

        client.deleteResources(
                ResourcesRequest.builder()
                        .path(parentResourcePath)
                        .permanently(true)
                        .build()
        );

        client.deleteResources(
                ResourcesRequest.builder()
                        .path(newResourcePath)
                        .permanently(true)
                        .build()
        );
    }
}
