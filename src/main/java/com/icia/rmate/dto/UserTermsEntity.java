package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Table(name = "USER_TERMS")
@Entity
public class UserTermsEntity {

    // 복합키(UserId와 TermsId 둘다 기본키로 두기 위해 사용, 가독성 높이기 위함)
    // UserTermId 클래스 따로 만들어서 사용
    @EmbeddedId
    private UserTermsId userTermsId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    private UserInfoEntity UserInfo;

    @ManyToOne
    @JoinColumn(name = "TERMS_ID", referencedColumnName = "TERMS_ID", insertable = false, updatable = false)
    private TermsEntity UserTerms;

    @Column(name = "AGREED")
    private int Agreed;     // 동의여부(0: 동의, 1: 동의안함)

    @Column(name = "AGREED_AT", updatable = false)
    @CreationTimestamp
    private LocalDateTime AgreedAt;     // 동의일자

    public static UserTermsEntity toEntity(UserTermsDTO dto) {
        UserTermsEntity entity = new UserTermsEntity();
//        entity.setTermsId(dto.getTermsId());
//        entity.setUserId(dto.getUserId());
        UserTermsId id = new UserTermsId(dto.getUserId(), dto.getTermsId());
        entity.setUserTermsId(id); // 복합키 설정
        entity.setAgreed(dto.getAgreed());
        entity.setAgreedAt(dto.getAgreedAt());

        return entity;
    }
}
