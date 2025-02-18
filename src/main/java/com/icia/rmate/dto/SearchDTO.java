package com.icia.rmate.dto;

import lombok.Data;

@Data
public class SearchDTO {
    private String category; //카테고리
    private String keyword; // 검색키워드
}
