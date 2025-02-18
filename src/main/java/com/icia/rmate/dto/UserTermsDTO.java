package com.icia.rmate.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserTermsDTO {
    private int TermsId;    // 약관코드(PK, FK)
    private String UserId;  // 유저아이디(FK)
    private int Agreed;     // 동의여부(0: 동의, 1: 동의안함)
    private LocalDateTime AgreedAt;     // 동의일자

    public static UserTermsDTO toDTO(UserTermsEntity entity) {
        UserTermsDTO dto = new UserTermsDTO();
//        dto.setTermsId(entity.getTermsId());
//        dto.setUserId(entity.getUserId());
        dto.setAgreed(entity.getAgreed());
        dto.setAgreedAt(entity.getAgreedAt());

        return dto;
    }
}
