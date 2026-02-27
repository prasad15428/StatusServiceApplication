package com.yono.status.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private PageInfo pageInfo;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PageInfo {
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean last;
    }
}
