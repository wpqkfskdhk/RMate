package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Table(name = "TERMS")
@Entity
public class TermsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REV_SEQ_GEN")
    @SequenceGenerator(name = "T_SEQ_GEN", sequenceName = "T_SEQ", allocationSize = 1, initialValue = 999)
    @Column(name = "TERMS_ID")
    private int TermsId;            // 약관코드(PK)

    @Column(name = "TERMS_NAME")
    private String TermsName;       // 약관명

    @Column(name = "TERMS_CONTENT")
    private String TermsContent;    // 약관내용

    @Column(name = "TERMS_REQUIRED")
    private int TermsRequired;      // 필수여부(0: 필수, 1: 선택)

    @OneToMany(mappedBy = "UserTerms", fetch = FetchType.LAZY)
    private List<UserTermsEntity> userTerms;

    public static TermsEntity toEntity(TermsDTO dto) {
        TermsEntity entity = new TermsEntity();
        entity.setTermsId(dto.getTermsId());
        entity.setTermsName(dto.getTermsName());
        entity.setTermsContent(dto.getTermsContent());
        entity.setTermsRequired(dto.getTermsRequired());

        return entity;
    }
}
