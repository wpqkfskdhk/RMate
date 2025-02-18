package com.icia.rmate.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeDTO {

    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdDate;

    // DTO -> Entity 변환
    public NoticeEntity toEntity() {
        return new NoticeEntity(this.id, this.title, this.content, this.author, this.createdDate);
    }

    public static NoticeDTO toDTO(NoticeEntity entity) {
        NoticeDTO dto = new NoticeDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setAuthor(entity.getAuthor());
        dto.setCreatedDate(entity.getCreatedDate());

        return dto;
    }

    public static NoticeDTO fromEntity(NoticeEntity noticeEntity) {
        NoticeDTO dto = new NoticeDTO();
        dto.setId(noticeEntity.getId());
        dto.setTitle(noticeEntity.getTitle());
        dto.setContent(noticeEntity.getContent());
        dto.setAuthor(noticeEntity.getAuthor());
        dto.setCreatedDate(noticeEntity.getCreatedDate());

        return dto;
    }
}
