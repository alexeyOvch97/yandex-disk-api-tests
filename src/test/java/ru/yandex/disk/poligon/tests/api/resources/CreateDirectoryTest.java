package ru.yandex.disk.poligon.tests.api.resources;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.disk.poligon.client.ResourcesClient;
import ru.yandex.disk.poligon.dto.request.ResourcesRequest;
import ru.yandex.disk.poligon.dto.response.GetResourceResponse;

import java.util.UUID;


import static java.net.HttpURLConnection.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateDirectoryTest {

    private final ResourcesClient client = new ResourcesClient();

    private String parentDirectoryPath;
    private String testedDirectoryPath;
    private String testedDirectoryName;

    @BeforeEach
    void prepareDirectoryData() {

        testedDirectoryName = "test_dir_" +
                UUID.randomUUID()
                .toString()
                .substring(0,3);

        testedDirectoryPath = "/" + testedDirectoryName;
    }

    private GetResourceResponse createAndGetDirectory(String path) {

        ResourcesRequest request = ResourcesRequest.builder()
                .path(path)
                .build();

        client.createDirectory(request)
                .then()
                .statusCode(HTTP_CREATED);

        return client.getResources(request)
                .then()
                .statusCode(HTTP_OK)
                .extract()
                .as(GetResourceResponse.class);
    }

    @Test
    @DisplayName("Create directory in root path")
    public void createDirectory_withRootPath_createsDirectory() {

        GetResourceResponse response = createAndGetDirectory(testedDirectoryPath);

        assertEquals(
                testedDirectoryName,
                response.getName(),
                "Directory name should match the created directory name"
        );

        assertEquals(
                "disk:" + testedDirectoryPath,
                response.getPath(),
                "Directory path should match the created directory path"
        );
    }

    @Test
    @DisplayName("Create directory in parent path")
    public void createDirectory_withParentPath_createsDirectory() {

        parentDirectoryPath = "/set_up_dir_" +
                UUID.randomUUID()
                        .toString()
                        .substring(0,3);

        createAndGetDirectory(parentDirectoryPath);

        String path = parentDirectoryPath + testedDirectoryPath;

        GetResourceResponse response = createAndGetDirectory(path);;

        assertEquals(
                testedDirectoryName,
                response.getName(),
                "Directory name should match the created directory name"
        );

        assertEquals(
                "disk:" + path,
                response.getPath(),
                "Directory path should match the created directory path"
        );
    }

    @Test
    @DisplayName("Return 400 when path parameter is missing")
    public void createDirectory_withoutPath_returns400() {

        client.createDirectory(
                        ResourcesRequest.builder()
                                .build()
                ).then()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Test
    @DisplayName("Should return 401 when authorization is missing")
    public void createDirectory_withoutAuth_return401() {

        client.createDirectoryWithoutAuth(
                        ResourcesRequest.builder()
                                .path(testedDirectoryPath)
                                .build())
                .then()
                .statusCode(HTTP_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Should return 404 when parent path does not exist")
    void createDirectory_withNonExistingParentPath_returns404() {

        String path = "/non_existing_" + UUID.randomUUID() + testedDirectoryPath;

        client.createDirectory(
                        ResourcesRequest.builder()
                                .path(path)
                                .build()
                )
                .then()
                .statusCode(HTTP_NOT_FOUND);
    }

    @AfterEach
    void deleteDirectory() {

        if (parentDirectoryPath != null && !parentDirectoryPath.isEmpty()) {

            ResourcesRequest deleteParentDirRequest = ResourcesRequest.builder()
                    .path(parentDirectoryPath)
                    .permanently(true)
                    .build();

            client.deleteResources(deleteParentDirRequest);
        } else {

            ResourcesRequest deleteTestDirRequest = ResourcesRequest.builder()
                    .path(testedDirectoryPath)
                    .permanently(true)
                    .build();

            client.deleteResources(deleteTestDirRequest);
        }
    }

}
