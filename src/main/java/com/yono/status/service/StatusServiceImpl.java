package com.yono.status.service;

import com.yono.status.dto.ApiResponse;
import com.yono.status.dto.StatusResponse;
import com.yono.status.entity.StatusConfig;
import com.yono.status.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    @Override
    @Cacheable(value = "statusConfigs", key = "#role + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public ApiResponse<List<StatusResponse>> getActiveStatusesByRole(String role, Pageable pageable) {
        Page<StatusConfig> page = statusRepository.findActiveStatusesByRole(role, pageable);

        List<StatusResponse> statusList = page.getContent().stream()
                .map(this::toStatusResponse)
                .collect(Collectors.toList());

        ApiResponse.PageInfo pageInfo = ApiResponse.PageInfo.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();

        return ApiResponse.<List<StatusResponse>>builder()
                .success(true)
                .message("Status configurations fetched successfully")
                .data(statusList)
                .pageInfo(pageInfo)
                .build();
    }

    private StatusResponse toStatusResponse(StatusConfig entity) {
        return StatusResponse.builder()
                .id(entity.getId())
                .role(entity.getRole())
                .statusCode(entity.getStatusCode())
                .statusDescription(entity.getStatusDescription())
                .statusOrder(entity.getStatusOrder())
                .build();
    }
}
