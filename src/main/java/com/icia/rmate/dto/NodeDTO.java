package com.icia.rmate.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NodeDTO {
    private Long id;    // 노드번호(PK)
    private String name;    // 노드명
    private String address;    // 노드주소
    private String phone;   // 노드연락처
    private double x;          // 노드위도
    private double y;          // 노드경도
    private Date regDt; // 등록일
    private Date modDt;  // 수정일

    public static NodeDTO toDTO(NodeEntity entity) {
        NodeDTO dto = new NodeDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAddress(entity.getAddress());
        dto.setPhone(entity.getPhone());
        dto.setX(entity.getX());
        dto.setY(entity.getY());
        dto.setRegDt(entity.getRegDt());
        dto.setModDt(entity.getModDt());

        return dto;
    }

}
