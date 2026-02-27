package com.yono.status.controller;

import com.yono.status.dto.ApiResponse;
import com.yono.status.dto.StatusResponse;
import com.yono.status.service.StatusService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/status")
@RequiredArgsConstructor
@Validated
public class StatusController {

    private final StatusService statusService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StatusResponse>>> getStatusByRole(
            @RequestParam
            @NotBlank(message = "Role must not be blank")
            @Size(max = 100, message = "Role must not exceed 100 characters")
            String role,

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page number must be zero or greater")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            int size) {

        Pageable pageable = PageRequest.of(page, size);
        ApiResponse<List<StatusResponse>> response = statusService.getActiveStatusesByRole(role, pageable);
        return ResponseEntity.ok(response);
    }
}
