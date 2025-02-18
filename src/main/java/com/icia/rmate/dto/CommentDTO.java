package com.icia.rmate.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CommentDTO {

    private int CNum;          // 댓글 번호 (PK)
    private String UserId;     // 유저 아이디
    private String CNickName;  // 유저 닉네임
    private int BNum;           // 게시글 번호 (FK)
    private String CContent;    // 댓글 내용
    private LocalDateTime CDate; // 댓글 작성일
    private int CRp;            // 댓글 신고
    private boolean isBlocked; // 회원차단유무
    private String BTitle;    // 게시글 제목

    // CommentEntity 객체를 CommentDTO로 변환하는 메서드
    public static CommentDTO toDTO(CommentEntity entity) {
        CommentDTO dto = new CommentDTO();

        dto.setCNum(entity.getCNum());
        dto.setUserId(entity.getUserInfo().getUserId()); // User_ID 값 가져오기
        dto.setCNickName(entity.getCNickName());
        dto.setBNum(entity.getBoard().getBNum()); // 게시글 번호 가져오기
        dto.setCContent(entity.getCContent());
        dto.setCDate(entity.getCDate());
        dto.setCRp(entity.getCRp());
        dto.setBlocked(entity.isBlocked());
        dto.setBTitle(entity.getBTitle());

        return dto;
    }
}
