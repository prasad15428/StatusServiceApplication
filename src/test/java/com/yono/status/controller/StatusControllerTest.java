package com.yono.status.controller;

import com.yono.status.dto.ApiResponse;
import com.yono.status.dto.StatusResponse;
import com.yono.status.service.StatusService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatusController.class)
class StatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatusService statusService;

    @Test
    void getStatusByRole_returnsOkWithData() throws Exception {
        List<StatusResponse> statusList = List.of(
                StatusResponse.builder().id(1L).role("ADMIN").statusCode("ACTIVE")
                        .statusDescription("Active state").statusOrder(1).build()
        );
        ApiResponse<List<StatusResponse>> apiResponse = ApiResponse.<List<StatusResponse>>builder()
                .success(true)
                .message("Status configurations fetched successfully")
                .data(statusList)
                .pageInfo(ApiResponse.PageInfo.builder()
                        .page(0).size(10).totalElements(1).totalPages(1).last(true).build())
                .build();

        when(statusService.getActiveStatusesByRole(eq("ADMIN"), any(Pageable.class))).thenReturn(apiResponse);

        mockMvc.perform(get("/api/v1/status")
                        .param("role", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].statusCode").value("ACTIVE"))
                .andExpect(jsonPath("$.pageInfo.totalElements").value(1));
    }

    @Test
    void getStatusByRole_returnsBadRequest_whenRoleIsBlank() throws Exception {
        mockMvc.perform(get("/api/v1/status")
                        .param("role", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getStatusByRole_returnsBadRequest_whenRoleMissing() throws Exception {
        mockMvc.perform(get("/api/v1/status")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void getStatusByRole_usesPaginationParams() throws Exception {
        List<StatusResponse> statusList = List.of(
                StatusResponse.builder().id(2L).role("USER").statusCode("CLOSED")
                        .statusDescription("Closed state").statusOrder(1).build()
        );
        ApiResponse<List<StatusResponse>> apiResponse = ApiResponse.<List<StatusResponse>>builder()
                .success(true)
                .message("Status configurations fetched successfully")
                .data(statusList)
                .pageInfo(ApiResponse.PageInfo.builder()
                        .page(1).size(5).totalElements(6).totalPages(2).last(true).build())
                .build();

        when(statusService.getActiveStatusesByRole(eq("USER"), any(Pageable.class))).thenReturn(apiResponse);

        mockMvc.perform(get("/api/v1/status")
                        .param("role", "USER")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageInfo.page").value(1))
                .andExpect(jsonPath("$.pageInfo.size").value(5));
    }
}
