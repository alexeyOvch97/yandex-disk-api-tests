package ru.yandex.disk.poligon.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import ru.yandex.disk.poligon.config.RequestSpecifications;
import ru.yandex.disk.poligon.dto.request.ResourcesRequest;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class TrashResourcesClient {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public Response getTrashResources(ResourcesRequest request) {

        Map<String, Object> queryParams =
                OBJECT_MAPPER.convertValue(request, Map.class);

        return given(RequestSpecifications.defaultSpec())
                .queryParams(queryParams)
                .when()
                .get("/v1/disk/trash/resources")
                .then()
                .extract()
                .response();
    }
}
