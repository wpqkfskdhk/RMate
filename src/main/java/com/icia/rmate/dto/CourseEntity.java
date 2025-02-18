package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "COURSE")
@Data
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CID") // 변경: 컬럼명을 CID로 변경
    private int cid; // 코스 ID

    @Column(name = "NODE_ID")
    private Long id; // 노드id

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "X")
    private Double x; // 경도

    @Column(name = "Y")
    private Double y; // 위도

    @Column(name = "REG_DT")
    private java.util.Date regDt; // 등록일시

    @Column(name = "MOD_DT")
    private java.util.Date modDt; // 수정일시

    @Column(name = "B_NUM", nullable = false)
    private int BNum;

    @Column(name = "USER_ID")
    private String UserId;

    @Column(name = "O_LINK")
    private String OLink;

    @Column(name = "C_STATUS")
    private int CStatus;

    // CourseEntity 객체로 변환하는 메서드
    public static CourseEntity toEntity(CourseDTO dto) {
        CourseEntity entity = new CourseEntity();
        entity.setCid(dto.getCid());
        entity.setId(dto.getId()); // 노드 id 설정
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setPhone(dto.getPhone());
        entity.setX(dto.getX());
        entity.setY(dto.getY());
        entity.setRegDt(dto.getRegDt());
        entity.setModDt(dto.getModDt());
        entity.setBNum(dto.getBNum());
        entity.setUserId(dto.getUserId());
        entity.setOLink(dto.getOLink());
        entity.setCStatus(dto.getCStatus());
        return entity;
    }
}