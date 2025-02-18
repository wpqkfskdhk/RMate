package com.icia.rmate.dao;

import com.icia.rmate.dto.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, String> {
    Optional<UserInfoEntity> findByUserNickname(String userNickname);
}



