package com.icia.rmate.dto;

import com.icia.rmate.dao.NoticeRepository;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notice")
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 자동 증가하는 ID

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", length = 500, nullable = false)
    private String content;

    @Column(name = "author")
    private String author;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    // JPA Entity에는 기본 생성자가 필요합니다.
    public NoticeEntity() {
    }

    // 모든 필드를 초기화하는 생성자
    public NoticeEntity(Long id, String title, String content, String author, LocalDateTime createdDate) {
        // this: 현재 생성 중인 NoticeEntity 객체
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdDate = createdDate;
    }

    // NoticeDTO에서 데이터 초기화
    public void initializeFromDTO(NoticeDTO noticeDTO) {
        // this: 현재 메서드가 호출된 NoticeEntity 객체
        this.id = noticeDTO.getId();
        this.title = noticeDTO.getTitle();
        this.content = noticeDTO.getContent();
        this.author = noticeDTO.getAuthor();
        this.createdDate = LocalDateTime.now();
    }

    // NoticeRepository를 사용하여 데이터베이스에 저장
    public void save(NoticeRepository repository) {
        // this: 현재 메서드가 호출된 NoticeEntity 객체
        repository.save(this);
    }

    @PrePersist
    public void prePersist() {
        // this: 현재 메서드가 호출된 NoticeEntity 객체
        this.createdDate = LocalDateTime.now();
    }
}