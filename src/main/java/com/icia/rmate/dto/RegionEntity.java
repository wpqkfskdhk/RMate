package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "REGION")
public class RegionEntity {

    @Id
    @Column(name = "R_NUM", nullable = false)
    private int RNum; // 지역 번호 (PK)

    @Column(name = "R_ADDR", nullable = false, length = 50)
    private String RAddr; // 지역 주소

    @OneToMany(mappedBy = "Region", fetch = FetchType.LAZY)
    List<RegionDetailEntity> RegionDetail;


    public static RegionEntity toEntity(RegionDTO dto) {
        RegionEntity entity = new RegionEntity();
        entity.setRNum(dto.getRNum());
        entity.setRAddr(dto.getRAddr());

        return entity;
    }
}
