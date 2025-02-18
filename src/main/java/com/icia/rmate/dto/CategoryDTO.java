package com.icia.rmate.dto;

import lombok.Data;

@Data
public class CategoryDTO {

    private int CategoryNum; // 카테고리 번호
    private String CategoryName; // 카테고리명

    public static CategoryDTO toDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryNum(entity.getCategoryNum());
        dto.setCategoryName(entity.getCategoryName());
        return dto;
    }
}
