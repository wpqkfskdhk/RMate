package com.icia.rmate.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BoardDTO {

    private int BNum;         // 게시글 번호 (PK)
    private int CategoryNum;  // 게시글 유형 (FK)
    private String UserId;    // 유저 아이디 (FK)
    private String BNickName; // 유저 닉네임
    private LocalDateTime BDate;  // 게시글 작성일
    private LocalDateTime BUpdate; // 게시글 수정일
    private int CountPeople;       // 현재인원
    private int CntNum;       // 정원(총인원수)
    private String BTitle;    // 게시글 제목
    private String BContent;  // 게시글 내용
    private String BFileName; // 게시글 파일명
    private int DetailNum;     // 상세주소번호(PK)
    private LocalDate BStart; // 일정 시작
    private LocalDate BEnd;   // 일정 종료
    private int BRp;          // 게시글 신고
    private String MType;     // 메이트 유형
    private String TagName;   // 태그내용
    private int TNum;    // 모임번호(FK) 추가(내용만 바꿔서 그대로 사용)
    private boolean isBlocked; // 게시글 차단유무
    private MultipartFile BFile;    // 첨부파일


    // Board 엔티티 객체를 BoardDTO로 변환하는 메서드
    public static BoardDTO toDTO(BoardEntity entity) {
        BoardDTO dto = new BoardDTO();
        dto.setBNum(entity.getBNum());

        dto.setCategoryNum(entity.getCategoryNum());
        dto.setUserId(entity.getUserId());
        dto.setBNickName(entity.getBNickName());
        dto.setBDate(entity.getBDate());
        dto.setBUpdate(entity.getBUpdate());
        dto.setCntNum(entity.getCntNum());
        dto.setBTitle(entity.getBTitle());
        dto.setBContent(entity.getBContent());
        dto.setBFileName(entity.getBFileName());
        dto.setDetailNum(entity.getDetailNum());
        dto.setMType(entity.getMType());
        dto.setBStart(entity.getBStart());
        dto.setBEnd(entity.getBEnd());
        dto.setBRp(entity.getBRp());
        dto.setBlocked(entity.isBlocked());
        dto.setTNum(entity.getTNum());
        dto.setTagName(entity.getTagName());
        dto.setCountPeople(entity.getCountPeople());
        return dto;

    }


}
