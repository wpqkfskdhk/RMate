package com.icia.rmate.dto;

import lombok.Data;

@Data
public class ApplyDTO {

    private int ANum; // 신청 번호 (PK)
    private String UserId; // 유저 아이디 (FK)
    private String UserGender; // 유저 성별
    private String UserNickName; // 유저 닉네임
    private String UserAddress; // 유저 주소
    private int BNum; // 게시글 번호 (FK)
    private int CategoryNum; // 카테고리 번호 (FK)
    private String AContent; // 신청내용
    private int AStatus; // 신청 상태(0:신청하기, 1:거절하기)
    private String BTitle;    // 게시글 제목
    private int PStatus;    // 결제상태
    private int RStatus; // 리뷰 작성 여부 필드 추가
    private int CntNum;       // 정원(총인원수)
    private String RAddrDetail; // 지역 상세주소
    private String UserPhone; //유저 번호

    // ApplyEntity 객체를 ApplyDTO로 변환하는 메서드


    public static ApplyDTO toDTO(ApplyEntity entity) {
        ApplyDTO dto = new ApplyDTO();

        dto.setANum(entity.getANum());
        dto.setUserId(entity.getUserId());
        dto.setUserNickName(entity.getUserNickName());
        dto.setUserGender(entity.getUserGender());
        dto.setUserAddress(entity.getUserInfo().getUserAddress());
        dto.setAStatus(entity.getAStatus());
        dto.setBNum(entity.getBNum()); // BoardEntity에서 B_Num 가져오기
        dto.setCategoryNum(entity.getBoard().getCategoryNum()); // CategoryEntity에서 Category_Num 가져오기
        dto.setAContent(entity.getAContent());
        dto.setBTitle(entity.getBTitle());
        dto.setPStatus(entity.getPStatus());
        dto.setRStatus(entity.getRStatus());
        dto.setCntNum(entity.getCntNum());
        dto.setRAddrDetail(entity.getBoard().getRegionDetail().getRAddrDetail());
        dto.setUserPhone(entity.getUserInfo().getUserPhone());

        return dto;
    }
}
