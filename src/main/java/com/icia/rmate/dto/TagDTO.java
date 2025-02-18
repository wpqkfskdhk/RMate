package com.icia.rmate.dto;

import lombok.Data;

@Data
public class TagDTO {
    private int TNum;   // 태그번호(PK)
    private String TName;   // 태그명

    public static TagDTO toDTO(TagEntity entity) {
        TagDTO dto = new TagDTO();


        return dto;
    }
}
