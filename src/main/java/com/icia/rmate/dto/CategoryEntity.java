package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "CATEGORY")
public class CategoryEntity {

    @Id
    @Column(name = "CATEGORY_NUM", nullable = false)
    private int CategoryNum; // 카테고리 번호 (PK)

    @Column(name = "CATEGORY_NAME", nullable = false, length = 200)
    private String CategoryName; // 카테고리명

    @OneToMany(mappedBy = "category")
    private List<BoardEntity> boards; // 여러 게시글을 참조

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<ApplyEntity> applyList; // 신청 리스트



    public static CategoryEntity toEntity(CategoryDTO dto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setCategoryNum(dto.getCategoryNum());
        entity.setCategoryName(dto.getCategoryName());
        return entity;
    }
}
