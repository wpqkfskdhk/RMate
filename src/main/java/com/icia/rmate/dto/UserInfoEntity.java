package com.icia.rmate.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "USER_INFO")
public class UserInfoEntity {

    @Id
    @Column(name = "USER_ID", nullable = false, length = 100)
    private String UserId;

    @Column(name = "USER_PW", nullable = false, length = 200)
    private String UserPw; // 유저 비밀번호

    @Column(name = "USER_GENDER", nullable = false, length = 10)
    private String UserGender; // 유저 성별

    @Column(name = "USER_BIRTH", nullable = false, length = 10)
    private String UserBirth; // 유저 생년월일

    @Column(name = "USER_EMAIL", nullable = false, length = 200, unique = true)
    private String UserEmail; // 유저 이메일

    @Column(name = "USER_PHONE", nullable = false, length = 50, unique = true)
    private String UserPhone; // 유저 연락처

    @Column(name = "USER_ADDRESS", nullable = false, length = 500)
    private String UserAddress; // 유저 주소

    @Column(name = "USER_PROFILENAME", nullable = false, length = 255, columnDefinition = "VARCHAR(255) DEFAULT 'default.jpg'")


    private String UserProfileName; // 유저 프로필 이름
    @Pattern(regexp = "^[가-힣]{2,5}$", message = "이름은 2~5자의 한글(초성X)만 입력 가능합니다. ")
    @Column(name = "USER_NAME", nullable = false, length = 100)
    private String UserName; // 유저 이름

    @Column(name = "USER_NICKNAME", nullable = false, length = 200, unique = true)
    @Pattern(regexp = "^[가-힣]{1,5}$", message = "닉네임은 1~5자의 한글만 입력 가능합니다.")
    private String userNickname; // 유저 닉네임

    @Column(name = "CAR_STATUS")
    private int CarStatus; // 자차유무 번호

    @Column(name = "IS_BLOCKED")
    private boolean isBlocked;  // 차단유무


    // USER_ID가 여러 BOARD 데이터의 레퍼런스로 사용됩니다.
    @OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY)
    private List<BoardEntity> boards; // 여러 게시글을 참조하는 목록

    // USER_ID가 여러 COMMENT 데이터의 레퍼런스로 사용됩니다.
    @OneToMany(mappedBy = "UserInfo", fetch = FetchType.LAZY)
    private List<CommentEntity> comments; // 여러 댓글을 참조하는 목록
//
//    // USER_ID가 여러 REVIEW 데이터의 레퍼런스로 사용됩니다.
//    @OneToMany(mappedBy = "UserInfo", fetch = FetchType.LAZY)
//    private List<RevEntity> reviews; // 여러 리뷰를 참조하는 목록

    // USER_ID가 여러 ORDER 데이터의 레퍼런스로 사용됩니다.
    @OneToMany(mappedBy = "UserInfo", fetch = FetchType.LAZY)
    private List<OrderEntity> orders; // 여러 주문을 참조하는 목록

    @OneToMany(mappedBy = "UserInfo", fetch = FetchType.LAZY)
    private List<ApplyEntity> applys;

    public static UserInfoEntity toEntity(UserInfoDTO dto) {
        UserInfoEntity entity = new UserInfoEntity();
        entity.setUserId(dto.getUserId()); // UserId 설정
        entity.setUserPw(dto.getUserPw());
        entity.setUserGender(dto.getUserGender());
        entity.setUserBirth(dto.getUserBirth());
        entity.setUserEmail(dto.getUserEmail());
        entity.setUserPhone(dto.getUserPhone());
        entity.setUserAddress(dto.getUserAddress());
        entity.setUserProfileName(dto.getUserProfileName());
        entity.setUserName(dto.getUserName());
        entity.setUserNickname(dto.getUserNickname());
        entity.setCarStatus(dto.getCarStatus());
        entity.setBlocked(dto.isBlocked());

        return entity;
    }



}
