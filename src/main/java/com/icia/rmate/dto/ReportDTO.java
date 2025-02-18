package com.icia.rmate.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDTO {

    private int ReportNum;           // 신고 번호 (PK)
    private String UserId;           // 유저 아이디  로그인한 유저
    private String ReportTitle;      // 신고 유형
    private LocalDateTime ReportDate; // 신고 작성일
    private int BNum;                // 게시글 번호 (FK)
    private int CommentNum;          // 댓글 번호 (FK)
    private LocalDateTime BlockDate; // 차단 날짜


    // ReportEntity 객체를 ReportDTO로 변환하는 메서드
    public static ReportDTO toDTO(ReportEntity entity) {
        ReportDTO dto = new ReportDTO();

        // 필드 값 매핑
        dto.setReportNum(entity.getReportNum());
        dto.setReportTitle(entity.getReportTitle());  // ReportTitle이 누락된 경우 추가
        dto.setReportDate(entity.getReportDate());    // ReportDate 추가
        dto.setBNum(entity.getBNum());  // BNum을 가져와서 DTO에 설정
        dto.setCommentNum(entity.getCommentNum());  // CNum을 가져와서 DTO에 설정
        dto.setUserId(entity.getUserId());  // UserId를 가져와서 설정
        dto.setBlockDate(entity.getBlockDate()); // 차단 날짜 추가

        return dto;
    }

    // ReportEntity와 userId를 받아 ReportDTO로 변환하는 메서드
    public static ReportDTO fromEntity(ReportEntity entity, String userId) {
        ReportDTO dto = new ReportDTO();
        dto.setReportNum(entity.getReportNum());
        dto.setReportTitle(entity.getReportTitle());
        dto.setReportDate(entity.getReportDate());
        dto.setBNum(entity.getBNum());
        dto.setCommentNum(entity.getCommentNum());
        dto.setUserId(userId);
        dto.setBlockDate(entity.getBlockDate());  // 차단 날짜 추가
        return dto;
    }
}