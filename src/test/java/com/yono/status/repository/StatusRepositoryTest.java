package com.yono.status.repository;

import com.yono.status.entity.StatusConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class StatusRepositoryTest {

    @Autowired
    private StatusRepository statusRepository;

    @BeforeEach
    void setUp() {
        statusRepository.deleteAll();

        statusRepository.save(StatusConfig.builder()
                .role("ADMIN").statusCode("ACTIVE").statusDescription("Active")
                .statusOrder(1).activeFlag("Y").createdDate(LocalDateTime.now()).build());

        statusRepository.save(StatusConfig.builder()
                .role("ADMIN").statusCode("INACTIVE").statusDescription("Inactive")
                .statusOrder(2).activeFlag("N").createdDate(LocalDateTime.now()).build());

        statusRepository.save(StatusConfig.builder()
                .role("USER").statusCode("PENDING").statusDescription("Pending")
                .statusOrder(1).activeFlag("Y").createdDate(LocalDateTime.now()).build());
    }

    @Test
    void findActiveStatusesByRole_returnsOnlyActiveRecordsForRole() {
        Page<StatusConfig> result = statusRepository.findActiveStatusesByRole("ADMIN", PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getStatusCode()).isEqualTo("ACTIVE");
        assertThat(result.getContent().get(0).getActiveFlag()).isEqualTo("Y");
    }

    @Test
    void findActiveStatusesByRole_excludesInactiveRecords() {
        Page<StatusConfig> result = statusRepository.findActiveStatusesByRole("ADMIN", PageRequest.of(0, 10));

        assertThat(result.getContent())
                .noneMatch(s -> "N".equals(s.getActiveFlag()));
    }

    @Test
    void findActiveStatusesByRole_filtersCorrectlyByRole() {
        Page<StatusConfig> result = statusRepository.findActiveStatusesByRole("USER", PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getRole()).isEqualTo("USER");
    }

    @Test
    void findActiveStatusesByRole_returnsPaginatedResults() {
        Page<StatusConfig> result = statusRepository.findActiveStatusesByRole("ADMIN", PageRequest.of(0, 1));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findActiveStatusesByRole_returnsEmptyPage_whenRoleNotFound() {
        Page<StatusConfig> result = statusRepository.findActiveStatusesByRole("UNKNOWN", PageRequest.of(0, 10));

        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }
}
