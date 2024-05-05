package com.example.Primarch.Utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityResponse<T> {

    private String message;
    private T entity;
    private Integer statusCode;
}
