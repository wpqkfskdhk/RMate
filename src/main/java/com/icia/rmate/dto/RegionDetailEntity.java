package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "REGION_DETAIL")
public class RegionDetailEntity {

    @Id
    @Column(name = "DETAIL_NUM", nullable = false)
    private int DetailNum;  // 상세주소번호(pk)

    @Column(name = "R_NUM")
    private int RNum;

    @ManyToOne
    @JoinColumn(name = "R_NUM", referencedColumnName = "R_NUM", insertable = false, updatable = false)
    private RegionEntity Region;  // 지역번호(FK)

    @Column(name = "R_ADDR_DETAIL", nullable = false, length = 50)
    private String RAddrDetail; // 지역 상세주소

    @Column(name = "REGION_X")
    private double RegionX; // 지역 위도

    @Column(name = "REGION_Y")
    private double RegionY; // 지역 경도

    @OneToMany(mappedBy = "regionDetail")
    private List<BoardEntity> boards; // 여러 게시글이 특정 지역을 참조

    public static RegionDetailEntity toEntity(RegionDetailDTO dto) {
        RegionDetailEntity entity = new RegionDetailEntity();
        entity.setDetailNum(dto.getDetailNum());
        entity.setRAddrDetail(dto.getRAddrDetail());
        entity.setRegionX(dto.getRegionX());
        entity.setRegionY(dto.getRegionY());
        entity.setRNum(dto.getRNum());

        return entity;

    }



}
