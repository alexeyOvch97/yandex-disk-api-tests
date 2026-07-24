package ru.yandex.disk.poligon.tests.api.resources;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.disk.poligon.client.ResourcesClient;
import ru.yandex.disk.poligon.client.TrashResourcesClient;
import ru.yandex.disk.poligon.dto.request.ResourcesRequest;
import ru.yandex.disk.poligon.dto.response.GetTrashResourceResponse;

import java.util.Map;
import java.util.UUID;

import static java.net.HttpURLConnection.*;
import static org.junit.jupiter.api.Assertions.*;

public class DeleteResourcesTest {

    private final ResourcesClient client = new ResourcesClient();
    private final TrashResourcesClient trashClient = new TrashResourcesClient();

    private String resourcePath;

    private String diskPath() {
        return "disk:" + resourcePath;
    }

    @BeforeEach
    void setUpResourceData() {
        resourcePath = "/test_resource_" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 3);
    }

    private void createDirectory(String path) {

        client.createDirectory(
                        ResourcesRequest.builder()
                                .path(path)
                                .build()
                ).then()
                .statusCode(HTTP_CREATED);
    }

    private GetTrashResourceResponse getTrashRoot() {
        return trashClient.getTrashResources(
                        ResourcesRequest.builder()
                                .path("/")
                                .build())
                .then()
                .statusCode(HTTP_OK)
                .extract()
                .as(GetTrashResourceResponse.class);
    }

    @Test
    @DisplayName("Delete resource permanently")
    void deleteResources_permanentlyTrue_deletesResource() {

        createDirectory(resourcePath);

        client.deleteResources(
                        ResourcesRequest.builder()
                                .path(resourcePath)
                                .permanently(true)
                                .build())
                .then()
                .statusCode(HTTP_NO_CONTENT);

        client.getResources(
                        ResourcesRequest.builder()
                                .path(resourcePath)
                                .build())
                .then()
                .statusCode(HTTP_NOT_FOUND);

        GetTrashResourceResponse response = getTrashRoot();

        assertFalse(
                response.getEmbedded().getItems().stream()
                        .anyMatch(item ->
                                (diskPath()).equals(item.getOriginPath())),
                "Permanently deleted resource should not be present in trash"
        );
    }

    @Test
    @DisplayName("Move resource to trash")
    public void deleteResources_permanentlyFalse_movesResourceToTrash() {

        createDirectory(resourcePath);

        ResourcesRequest deleteRequest = ResourcesRequest.builder()
                .path(resourcePath)
                .permanently(false)
                .build();

        client.deleteResources(deleteRequest)
                .then()
                .statusCode(HTTP_NO_CONTENT);

        ResourcesRequest getRequest = ResourcesRequest.builder()
                .path(resourcePath)
                .build();

        client.getResources(getRequest)
                .then()
                .statusCode(HTTP_NOT_FOUND);

        GetTrashResourceResponse response = getTrashRoot();

        assertTrue(
                response.getEmbedded().getItems().stream()
                        .anyMatch(item ->
                                (diskPath()).equals(item.getOriginPath())),
                "Deleted resource should be present in trash"
        );
    }

    @Test
    @DisplayName("Return 400 when path parameter is missing")
    public void deleteResources_withoutPath_returns400() {

        client.deleteResources(
                        ResourcesRequest.builder()
                                .build()
                ).then()
                .statusCode(HTTP_BAD_REQUEST);
    }

    private static Object[][] invalidQueryParameters() {
        return new Object[][] {
                {"force_async", "abc"},
                {"permanently", 123}
        };
    }

    @ParameterizedTest(name = "Invalid {0}: {1}")
    @MethodSource("invalidQueryParameters")
    @DisplayName("Return 400 for invalid query parameter types")
    public void deleteResources_withInvalidQueryParameterType_returns400(String parameter, Object value) {

        Map<String, Object> queryParams = Map.of(
                "path", resourcePath,
                parameter, value
        );

        client.deleteResources(queryParams)
                .then()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Test
    @DisplayName("Return 401 when authorization is missing")
    public void deleteResources_withoutAuth_returns401() {

        client.deleteResourcesWithoutAuth(
                        ResourcesRequest.builder()
                                .path(resourcePath)
                                .permanently(false)
                                .build()
                )
                .then()
                .statusCode(HTTP_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Return 404 when resource does not exist")
    public void deleteResources_withNonExistingPath_returns404() {

        client.deleteResources(
                        ResourcesRequest.builder()
                                .path(resourcePath)
                                .permanently(false)
                                .build()
                )
                .then()
                .statusCode(HTTP_NOT_FOUND);
    }

    @Test
    @DisplayName("Return 404 when deleting resource twice")
    public void deleteResources_whenCalledTwice_returns404() {

        createDirectory(resourcePath);

        ResourcesRequest request = ResourcesRequest.builder()
                .path(resourcePath)
                .permanently(true)
                .build();

        client.deleteResources(request)
                .then()
                .statusCode(HTTP_NO_CONTENT);

        client.deleteResources(request)
                .then()
                .statusCode(HTTP_NOT_FOUND);
    }

    @AfterEach
    void deleteResources() {

        client.deleteResources(
                ResourcesRequest.builder()
                        .path(resourcePath)
                        .permanently(true)
                        .build()
        );
    }
}
