package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "APPLY")
public class ApplyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APPLY_SEQ_GEN")
    @SequenceGenerator(name = "APPLY_SEQ_GEN", sequenceName = "APPLY_SEQ", allocationSize = 1, initialValue = 999)
    @Column(name = "A_NUM")
    private int ANum; // 신청 번호 (PK)
    @Column(name = "USER_ID")
    private String UserId; //
    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false )
    private UserInfoEntity UserInfo; // 유저 아이디 (FK)

    @Column(name = "USER_GENDER", nullable = false, length = 10)
    private String UserGender; // 유저 성별

    @Column(name = "USER_NICKNAME", nullable = false, length = 200)
    private String UserNickName; // 유저 닉네임

    @Column(name = "B_NUM")
    private int BNum;

    @Column(name = "CATEGORY_NUM")
    private int CategoryNum;

    @ManyToOne
    @JoinColumn(name = "B_NUM", referencedColumnName = "B_NUM", insertable = false, updatable = false)
    private BoardEntity board; // 게시글 번호 (FK)

    @ManyToOne
    @JoinColumn(name = "CATEGORY_NUM", referencedColumnName = "CATEGORY_NUM", insertable = false, updatable = false)
    private CategoryEntity category; // 카테고리 번호 (FK)

    @Column(name = "A_STATUS", nullable = false)
    private int AStatus; // 신청 상태 (0: 신청, 1: 거절)

    @Column(name = "A_CONTENT")
    private String AContent; // 신청내용

    @Column(name = "B_TITLE")
    private String BTitle;    // 게시글 제목

    @Column(name = "P_STATUS", nullable = false)
    private int PStatus;    // 결제상태


    @Column(name = "R_STATUS")
    private int RStatus; // 리뷰 작성 여부 필드 추가

    @Column(name = "CNT_NUM")
    private int CntNum;

    // ApplyEntity 객체를 ApplyDTO로 변환하는 메서드
    public static ApplyEntity toEntity(ApplyDTO dto) {
        ApplyEntity entity = new ApplyEntity();
        entity.setUserGender(dto.getUserGender());
        entity.setUserId(dto.getUserId());
        entity.setANum(dto.getANum());
        entity.setUserNickName(dto.getUserNickName());
        entity.setAStatus(dto.getAStatus());
        entity.setBNum(dto.getBNum());
        entity.setCategoryNum(dto.getCategoryNum());
        entity.setAContent(dto.getAContent());
        entity.setBTitle(dto.getBTitle());
        entity.setPStatus(dto.getPStatus());
        entity.setRStatus(dto.getRStatus());
        entity.setCntNum(dto.getCntNum());

        return entity;
    }
}
