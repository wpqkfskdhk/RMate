package com.icia.rmate.dao;

import com.icia.rmate.dto.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    // 페이지네이션과 함께 네이티브 쿼리 사용
    @Query(value = "SELECT * FROM report_entity WHERE report_status = :status LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<ReportEntity> findReportsByStatus(@Param("status") String status, @Param("limit") int limit, @Param("offset") int offset);

}