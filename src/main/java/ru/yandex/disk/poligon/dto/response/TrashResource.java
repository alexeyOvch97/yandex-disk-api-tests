package ru.yandex.disk.poligon.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrashResource {

    private String path;

    private String name;

    @JsonProperty("origin_path")
    private String originPath;
}
