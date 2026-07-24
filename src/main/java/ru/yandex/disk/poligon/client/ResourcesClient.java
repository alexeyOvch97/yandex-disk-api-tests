package ru.yandex.disk.poligon.client;

import io.restassured.response.Response;
import ru.yandex.disk.poligon.config.RequestSpecifications;
import ru.yandex.disk.poligon.dto.request.ResourcesRequest;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import static io.restassured.RestAssured.given;

public class ResourcesClient {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public Response getResources(ResourcesRequest request) {

        Map<String, Object> queryParams =
                OBJECT_MAPPER.convertValue(request, Map.class);

        return given(RequestSpecifications.defaultSpec())
                .queryParams(queryParams)
                .when()
                .get("/v1/disk/resources")
                .then()
                .extract()
                .response();
    }

    public Response getResources(Map<String, Object> queryParams) {
        return given(RequestSpecifications.defaultSpec())
                .queryParams(queryParams)
                .when()
                .get("/v1/disk/resources")
                .then()
                .extract()
                .response();
    }

    public Response getResourcesWithoutAuth(ResourcesRequest request) {

        Map<String, Object> queryParams =
                OBJECT_MAPPER.convertValue(request, Map.class);

        return given(RequestSpecifications.noAuthSpec())
                .auth().none()
                .queryParams(queryParams)
                .when()
                .get("/v1/disk/resources")
                .then()
                .extract()
                .response();
    }

    public Response copyResources(ResourcesRequest request) {

        Map<String, Object> queryParams =
                OBJECT_MAPPER.convertValue(request, Map.class);

        return given(RequestSpecifications.defaultSpec())
                .queryParams(queryParams)
                .when()
                .post("/v1/disk/resources/copy")
                .then()
                .extract()
                .response();
    }

    public Response copyResources(Map<String, Object> queryParams) {
        return given(RequestSpecifications.defaultSpec())
                .queryParams(queryParams)
                .when()
                .post("/v1/disk/resources/copy")
                .then()
                .extract()
                .response();
    }

    public Response copyResourcesWithoutAuth(ResourcesRequest request) {

        Map<String, Object> queryParams =
                OBJECT_MAPPER.convertValue(request, Map.class);

        return given(RequestSpecifications.noAuthSpec())
                .queryParams(queryParams)
                .when()
                .post("/v1/disk/resources/copy")
                .then()
                .extract()
                .response();
    }

    public Response createDirectory(ResourcesRequest request) {

        Map<String, Object> queryParams =
                OBJECT_MAPPER.convertValue(request, Map.class);

        return given(RequestSpecifications.defaultSpec())
                .queryParams(queryParams)
                .when()
                .put("/v1/disk/resources")
                .then()
                .extract()
                .response();
    }

    public Response createDirectoryWithoutAuth(ResourcesRequest request) {

        Map<String, Object> queryParams =
                OBJECT_MAPPER.convertValue(request, Map.class);

        return given(RequestSpecifications.noAuthSpec())
                .queryParams(queryParams)
                .when()
                .put("/v1/disk/resources")
                .then()
                .extract()
                .response();
    }

    public Response deleteResources(ResourcesRequest request) {

        Map<String, Object> queryParams =
                OBJECT_MAPPER.convertValue(request, Map.class);

        return given(RequestSpecifications.defaultSpec())
                .queryParams(queryParams)
                .when()
                .delete("/v1/disk/resources")
                .then()
                .extract()
                .response();
    }

    public Response deleteResources(Map<String, Object> queryParams) {
        return given(RequestSpecifications.defaultSpec())
                .queryParams(queryParams)
                .when()
                .delete("/v1/disk/resources")
                .then()
                .extract()
                .response();
    }

    public Response deleteResourcesWithoutAuth(ResourcesRequest request) {

        Map<String, Object> queryParams =
                OBJECT_MAPPER.convertValue(request, Map.class);

        return given(RequestSpecifications.noAuthSpec())
                .queryParams(queryParams)
                .when()
                .delete("/v1/disk/resources")
                .then()
                .extract()
                .response();
    }
}
