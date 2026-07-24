package ru.yandex.disk.poligon.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResourcesRequest {

    private String path;
    private String fields;
    private Integer limit;
    private Integer offset;

    @JsonProperty("preview_crop")
    private Boolean previewCrop;

    @JsonProperty("preview_size")
    private String previewSize;

    @JsonProperty("force_async")
    private Boolean forceAsync;

    private String sort;
    private Boolean permanently;
    private Boolean overwrite;
    private String from;
}
