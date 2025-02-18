package com.icia.rmate.dao;

import com.icia.rmate.dto.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO notice (title, content, author, created_date) VALUES (:title, :content, :author, :createdDate)", nativeQuery = true)
    void saveNoticeQuery(@Param("title") String title,
                         @Param("content") String content,
                         @Param("author") String author,
                         @Param("createdDate") LocalDateTime createdDate);
}