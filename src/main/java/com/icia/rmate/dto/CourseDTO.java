package com.icia.rmate.dto;

import lombok.Data;

@Data
public class CourseDTO {
    private int cid; // 코스노드id
    private Long id; // 노드id
    private String name;
    private String address;
    private String phone;
    private Double x; // 경도
    private Double y; // 위도
    private java.util.Date regDt; // 등록일시
    private java.util.Date modDt; // 수정일시
    private int BNum; // 게시글 번호 (FK)
    private String UserId; // 유저 아이디 (FK)
    private String OLink; // 오픈톡링크
    private int CStatus; // 고정노드 확인(0: 일반, 1: 고정)

    // DTO 객체로 변환하는 메서드
    public static CourseDTO toDTO(CourseEntity entity) {
        CourseDTO dto = new CourseDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAddress(entity.getAddress());
        dto.setPhone(entity.getPhone());
        dto.setX(entity.getX());
        dto.setY(entity.getY());
        dto.setRegDt(entity.getRegDt());
        dto.setModDt(entity.getModDt());
        dto.setBNum(entity.getBNum());
        dto.setUserId(entity.getUserId());
        dto.setOLink(entity.getOLink());
        dto.setCStatus(entity.getCStatus());

        return dto;
    }
}