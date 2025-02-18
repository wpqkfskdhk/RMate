package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "NODE")
public class NodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UB_SEQ_GEN")
    @SequenceGenerator(name = "UB_SEQ_GEN", sequenceName = "UB_SEQ", allocationSize = 1)
    @Column(name = "id")
    private Long id;    // 노드번호(PK)

    @Column(name = "name")
    private String name;    // 노드명

    @Column(name = "address")
    private String address;    // 노드주소

    @Column(name = "phone")
    private String phone;   // 노드연락처

    @Column(name = "x")
    private double x;   // 노드위도

    @Column(name = "y")
    private double y;   // 노드경도

    @CreationTimestamp
    @Column(name = "reg_dt")
    private Date regDt; // 등록일

    @UpdateTimestamp
    @Column(name = "mod_dt")
    private Date modDt;  // 수정일

    public static NodeEntity toEntity(NodeDTO dto) {
        NodeEntity entity = new NodeEntity();

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setPhone(dto.getPhone());
        entity.setX(dto.getX());
        entity.setY(dto.getY());
        entity.setRegDt(dto.getRegDt());
        entity.setModDt(dto.getModDt());
        return entity;
    }

}
