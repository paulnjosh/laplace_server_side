package com.example.Primarch.Utils;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationResponse {
    @Builder.Default
    private Integer status = null;
    @Builder.Default
    private Integer statusCode = null;

    @Builder.Default
    private String message = null;

    @Builder.Default
    private Long totalItems = null;

    @Builder.Default
    private Integer totalPages = null;

    @Builder.Default
    private Integer currentPage = null;
    @Builder.Default
    private List<?> data = null;
}
