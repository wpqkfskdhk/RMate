package com.icia.rmate.dto;

import lombok.Data;

@Data
public class RegionDetailDTO {

    private int DetailNum; // 상세주소번호(PK)
    private int RNum; // 지역번호(FK)
    private String RAddrDetail; // 지역 상세주소
    private double RegionX; // 지역 위도
    private double RegionY; // 지역 경도

    public static RegionDetailDTO toDTO(RegionDetailEntity entity) {
        RegionDetailDTO dto = new RegionDetailDTO();

        dto.setDetailNum(entity.getDetailNum());
        dto.setRAddrDetail(entity.getRAddrDetail());
        dto.setRegionX(entity.getRegionX());
        dto.setRegionY(entity.getRegionY());
        dto.setRNum(entity.getRNum());

        return dto;
    }
}
