package com.icia.rmate.dto;

import lombok.Data;

@Data
public class RegionDTO {

    private int RNum; // 지역 번호(PK)
    private String RAddr; // 지역 주소

    public static RegionDTO toDTO(RegionEntity entity) {
        RegionDTO dto = new RegionDTO();
        dto.setRNum(entity.getRNum());
        dto.setRAddr(entity.getRAddr());

        return dto;
    }
}
