package com.yono.status.repository;

import com.yono.status.entity.StatusConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<StatusConfig, Long> {

    @Query("SELECT s FROM StatusConfig s WHERE s.role = :role AND s.activeFlag = 'Y' ORDER BY s.statusOrder ASC")
    Page<StatusConfig> findActiveStatusesByRole(@Param("role") String role, Pageable pageable);
}
