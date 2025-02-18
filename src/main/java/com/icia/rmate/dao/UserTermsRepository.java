package com.icia.rmate.dao;

import com.icia.rmate.dto.UserTermsEntity;
import com.icia.rmate.dto.UserTermsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserTermsRepository extends JpaRepository<UserTermsEntity, UserTermsId> {
    @Query("SELECT ut FROM UserTermsEntity ut WHERE ut.UserInfo.UserId = :UserId")
    List<UserTermsEntity> findByUserId(@Param("UserId") String loginId);
    // 추가적인 쿼리 메소드 필요시 작성
}
