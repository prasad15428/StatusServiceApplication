package com.yono.status.service;

import com.yono.status.dto.ApiResponse;
import com.yono.status.dto.StatusResponse;
import com.yono.status.entity.StatusConfig;
import com.yono.status.repository.StatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatusServiceImplTest {

    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private StatusServiceImpl statusService;

    private List<StatusConfig> activeConfigs;

    @BeforeEach
    void setUp() {
        activeConfigs = List.of(
                StatusConfig.builder().id(1L).role("ADMIN").statusCode("ACTIVE")
                        .statusDescription("Active state").statusOrder(1).activeFlag("Y").build(),
                StatusConfig.builder().id(2L).role("ADMIN").statusCode("PENDING")
                        .statusDescription("Pending state").statusOrder(2).activeFlag("Y").build()
        );
    }

    @Test
    void getActiveStatusesByRole_returnsActiveRecords() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<StatusConfig> page = new PageImpl<>(activeConfigs, pageable, activeConfigs.size());

        when(statusRepository.findActiveStatusesByRole(eq("ADMIN"), any(Pageable.class))).thenReturn(page);

        ApiResponse<List<StatusResponse>> response = statusService.getActiveStatusesByRole("ADMIN", pageable);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).hasSize(2);
        assertThat(response.getData().get(0).getStatusCode()).isEqualTo("ACTIVE");
        assertThat(response.getData().get(1).getStatusCode()).isEqualTo("PENDING");
        assertThat(response.getPageInfo().getTotalElements()).isEqualTo(2);
    }

    @Test
    void getActiveStatusesByRole_returnsEmptyList_whenNoRecordsFound() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<StatusConfig> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(statusRepository.findActiveStatusesByRole(eq("UNKNOWN"), any(Pageable.class))).thenReturn(emptyPage);

        ApiResponse<List<StatusResponse>> response = statusService.getActiveStatusesByRole("UNKNOWN", pageable);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isEmpty();
        assertThat(response.getPageInfo().getTotalElements()).isZero();
    }

    @Test
    void getActiveStatusesByRole_pageInfoIsCorrect() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<StatusConfig> page = new PageImpl<>(List.of(activeConfigs.get(0)), pageable, 2);

        when(statusRepository.findActiveStatusesByRole(eq("ADMIN"), any(Pageable.class))).thenReturn(page);

        ApiResponse<List<StatusResponse>> response = statusService.getActiveStatusesByRole("ADMIN", pageable);

        assertThat(response.getPageInfo().getPage()).isZero();
        assertThat(response.getPageInfo().getSize()).isEqualTo(1);
        assertThat(response.getPageInfo().getTotalElements()).isEqualTo(2);
        assertThat(response.getPageInfo().getTotalPages()).isEqualTo(2);
        assertThat(response.getPageInfo().isLast()).isFalse();
    }
}
