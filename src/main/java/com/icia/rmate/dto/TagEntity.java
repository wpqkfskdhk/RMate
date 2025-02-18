package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Table(name = "TAG")
@Entity
public class TagEntity {

    @Id
    @Column(name = "T_NUM")
    private int TNum;

    @Column(name = "T_NAME")
    private String TName;

    @OneToMany(mappedBy = "Tag")  // Board와의 관계 설정 (One to Many)
    private List<BoardEntity> boards;  // Tag에 연결된 Board 목록

    public static TagEntity toEntity(TagDTO dto) {
        TagEntity entity = new TagEntity();
        entity.setTNum(dto.getTNum());
        entity.setTName(dto.getTName());

        return entity;

    }
}
