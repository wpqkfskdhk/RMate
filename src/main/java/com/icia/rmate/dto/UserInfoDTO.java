package com.icia.rmate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Setter
public class UserInfoDTO {

    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(regexp = "[a-zA-Z0-9]{3,12}", message = "아이디는 3~12자의 영어 또는 숫자만 입력 가능합니다.")
    private String UserId; // 유저 아이디 (PK)
    private String UserPw; // 유저 비밀번호 o
    private String UserGender; // 유저 성별 o
    private String UserBirth; // 유저 생년월일 o
    private String UserEmail; // 유저 이메일 o
    private String UserPhone; // 유저 연락처 o
    private String UserAddress; // 유저 주소 o
    private String UserProfileName; // 유저 프로필 이름 o
    @NotBlank(message = "이름을 입력해주세요.")
    @Size(max = 5, message = "닉네임은 2~5자 사이로 입력해주세요.")
    @Pattern(regexp = "^[가-힣]{2,5}$", message = "이름은 2~5자의 한글(초성X)만 입력 가능합니다. ")
    private String UserName; // 유저 이름 o
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max = 5, message = "닉네임은 5자 이하로 입력해주세요.")
    @Pattern(regexp = "^[가-힣]+$", message = "닉네임은 한글만 입력 가능합니다.")
    private String userNickname; // 유저 닉네임 o
    private int CarStatus; // 자차유무 번호 o
    private boolean isBlocked; // 회원차단유무
    private String AdminId;
    private MultipartFile  UserProfile;

    public static UserInfoDTO toDTO(UserInfoEntity entity) {
        UserInfoDTO dto = new UserInfoDTO();
        dto.setUserId(entity.getUserId());
        dto.setUserPw(entity.getUserPw());
        dto.setUserGender(entity.getUserGender());
        dto.setUserBirth(entity.getUserBirth());
        dto.setUserEmail(entity.getUserEmail());
        dto.setUserPhone(entity.getUserPhone());
        dto.setUserAddress(entity.getUserAddress());
        dto.setUserProfileName(entity.getUserProfileName());
        dto.setUserName(entity.getUserName());
        dto.setUserNickname(entity.getUserNickname());
        dto.setCarStatus(entity.getCarStatus());
        dto.setBlocked(entity.isBlocked());

        return dto;
    }
    public static UserInfoDTO fromEntity(UserInfoEntity entity) {
        UserInfoDTO dto = new UserInfoDTO();
        dto.setUserId(entity.getUserId());
        dto.setUserPw(entity.getUserPw());
        dto.setUserGender(entity.getUserGender());
        dto.setUserBirth(entity.getUserBirth());
        dto.setUserEmail(entity.getUserEmail());
        dto.setUserPhone(entity.getUserPhone());
        dto.setUserAddress(entity.getUserAddress());
        dto.setUserProfileName(entity.getUserProfileName());
        dto.setUserName(entity.getUserName());
        dto.setUserNickname(entity.getUserNickname());
        dto.setCarStatus(entity.getCarStatus());
        dto.setBlocked(entity.isBlocked());
        return dto;
    }
}
