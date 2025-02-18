package com.icia.rmate.dto;

import lombok.Data;

@Data
public class TermsDTO {
    private int TermsId;            // 약관코드(P)
    private String TermsName;       // 약관명
    private String TermsContent;    // 약관내용
    private int TermsRequired;      // 필수여부(0 : 필수, 1: 선택)

    public static TermsDTO toDTO(TermsEntity entity) {
        TermsDTO dto = new TermsDTO();
        dto.setTermsId(entity.getTermsId());
        dto.setTermsName(entity.getTermsName());
        dto.setTermsContent(entity.getTermsContent());
        dto.setTermsRequired(entity.getTermsRequired());

        return dto;
    }

}
