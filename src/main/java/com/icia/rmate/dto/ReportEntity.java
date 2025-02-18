package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "REPORT")
@Data
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REPORT_SEQ_GEN")
    @SequenceGenerator(name = "REPORT_SEQ_GEN", sequenceName = "REPORT_SEQ", allocationSize = 1, initialValue = 999)
    @Column(name = "REPORT_NUM")
    private int ReportNum; // 신고 번호 (PK)

    @Column(name = "REPORT_TITLE")
    private String ReportTitle; // 신고 제목

    @Column(name = "B_NUM")
    private int BNum;

    @ManyToOne
    @JoinColumn(name = "B_NUM", referencedColumnName = "B_NUM", insertable = false, updatable = false)
    private BoardEntity board; // 게시글 번호 (FK)

    @CreationTimestamp
    @Column(name = "REPORT_DATE")
    private LocalDateTime ReportDate; // 게시글 작성일

    @Column(name = "USER_ID")
    private String UserId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    private UserInfoEntity userInfo; // 유저 아이디 (FK)

    @Column(name = "C_NUM")
    private int CommentNum;
    @ManyToOne
    @JoinColumn(name = "C_NUM", referencedColumnName = "C_NUM", insertable = false, updatable = false)
    private CommentEntity comment; // 댓글 번호 (FK)

    @UpdateTimestamp
    @Column(name = "BLOCK_DATE")
    private LocalDateTime BlockDate; // 차단 날짜

    // ReportDTO 객체를 ReportEntity로 변환하는 메서드
    public static ReportEntity toEntity(ReportDTO dto) {
        ReportEntity entity = new ReportEntity();
        entity.setReportNum(dto.getReportNum());
        entity.setReportNum(dto.getReportNum());
        entity.setReportTitle(dto.getReportTitle());
        entity.setReportDate(dto.getReportDate());
        entity.setBNum(dto.getBNum());
        entity.setCommentNum(dto.getCommentNum());
        entity.setUserId(dto.getUserId());
        entity.setBlockDate(dto.getBlockDate());

        return entity;
    }
}
