package com.icia.rmate.dao;

import com.icia.rmate.dto.RevEntity;
import com.icia.rmate.dto.UserInfoEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<RevEntity, Integer> {
    List<RevEntity> findAllByOrderByREVNumDesc();

    @Query("SELECT r FROM RevEntity r WHERE LOWER(r.UserNickname) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY r.REVNum DESC")
    List<RevEntity> findByUserNicknameContainingOrderByREVNumDesc(@Param("keyword") String keyword);

    @Query("SELECT r FROM RevEntity r WHERE LOWER(r.RevTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY r.REVNum DESC")
    List<RevEntity> findByRevTitleContainingOrderByREVNumDesc(@Param("keyword") String keyword);

    @Query("SELECT r FROM RevEntity r WHERE LOWER(r.RevContent) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY r.REVNum DESC")
    List<RevEntity> findByRevContentContainingOrderByREVNumDesc(@Param("keyword") String keyword);

    @Modifying
    @Transactional
    @Query("UPDATE RevEntity r Set r.RHit = r.RHit + 1 WHERE r.REVNum = :RevNum")
    void incrementBHit(@Param("RevNum")int RevNum);

    @Query("SELECT r FROM RevEntity r WHERE r.UserId = :UserId")
    List<RevEntity> findByUserId(@Param("UserId") String loginId);

    // R_RP 증가 쿼리 (수동으로 @Query로 정의)
    @Modifying
    @Transactional
    @Query("UPDATE RevEntity r SET r.RRp = r.RRp + 1 WHERE r.REVNum = :RevNum")
    void incrementRRp(@Param("RevNum") int RevNum);  // Integer로 매핑


    @Query("SELECT r FROM RevEntity r WHERE r.UserId = :UserId and r.BNum = :bNum")
    List<RevEntity> existsByBNumAndUserId(@Param("bNum") int bNum,@Param("UserId") String loginId);
}