package com.cacheing.cacheingtest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private String status;
    private String message;
    private String reason;
    private Integer key;
    private String value;

    public Response(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status='" + status + '\'' +
                ", massage='" + message + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
