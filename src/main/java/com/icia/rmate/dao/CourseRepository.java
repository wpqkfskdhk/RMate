package com.icia.rmate.dao;

import com.icia.rmate.dto.CourseDTO;
import com.icia.rmate.dto.CourseEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {
    Optional<CourseEntity> findByAddress(String address);

    List<CourseEntity> findByBNum(int bNum);



    // 방법 1: @Query 어노테이션을 사용하여 직접 쿼리 정의
    @Transactional
    @Modifying
    @Query("DELETE FROM CourseEntity c WHERE c.BNum = :bnum")
    void deleteBybnum(@Param("bnum") int bnum);
//    ORDER BY c.cid ASC
    @Transactional
    @Modifying
    @Query("SELECT c FROM CourseEntity c WHERE c.BNum = :bnum ORDER BY c.CStatus DESC, c.cid ASC")
    List<CourseEntity> getCourseListByBNum(@Param("bnum") int bnum);
    @Transactional
    @Modifying
    @Query("DELETE FROM CourseEntity c WHERE c.id = :Id")
    void deletebynodeId(@Param("Id") Long nodeId);

    Optional<CourseEntity> findByAddressAndBNum(String address, int bNum);
    @Transactional
    @Query("SELECT COUNT(c) FROM CourseEntity c WHERE c.BNum = :bNum") // JPQL 쿼리 직접 작성
    long countByBnum(int bNum);
}