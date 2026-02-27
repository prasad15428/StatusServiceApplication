package com.yono.status.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusResponse {

    private Long id;
    private String role;
    private String statusCode;
    private String statusDescription;
    private Integer statusOrder;
}
