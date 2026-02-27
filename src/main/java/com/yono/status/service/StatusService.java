package com.yono.status.service;

import com.yono.status.dto.ApiResponse;
import com.yono.status.dto.StatusResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StatusService {

    ApiResponse<List<StatusResponse>> getActiveStatusesByRole(String role, Pageable pageable);
}
