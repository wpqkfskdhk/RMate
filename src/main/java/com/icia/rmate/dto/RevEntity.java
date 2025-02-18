package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "REV")
@Data
public class RevEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REV_SEQ_GEN")
    @SequenceGenerator(name = "REV_SEQ_GEN", sequenceName = "REV_SEQ", allocationSize = 1, initialValue = 999)
    @Column(name = "REV_NUM")
    private int REVNum; // 후기 글 번호 (PK)

    @Column(name = "USER_ID")
    private String UserId;    // 후기 작성자 (FK)

    @ManyToOne // 추가: UserInfoEntity와의 관계 설정
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    private UserInfoEntity UserInfo;

    @Column(name = "USER_NICKNAME")     // 작성자 닉네임
    private String UserNickname;

    @Column(name = "REV_DATE",updatable = false)
    @CreationTimestamp
    private LocalDateTime RevDate; // 후기 작성일

    @Column(name = "REV_UPDATE",insertable = false)
    @CreationTimestamp
    private LocalDateTime RevUpdate; // 후기 수정일

    @Column(name = "REV_TITLE")
    private String RevTitle; // 후기 제목

    @Column(name = "REV_CONTENT", columnDefinition = "CLOB")
    private String RevContent; // 후기 내용

    @Column(name = "B_NUM")
    private int BNum;

    @ManyToOne
    @JoinColumn(name = "B_NUM", referencedColumnName = "B_NUM", insertable = false, updatable = false)
    private BoardEntity board; // 게시글 번호 (FK)

    @Column(name = "IS_BLOCKED")
    private Boolean IsBlocked = false;  // 차단유무

    @Column(name = "RATING")
    private int Rating; // 후기 평가

    @Column(name = "RHIT")
    private int RHit = 0;  // 조회수

    @Column(name = "REV_FILENAME")
    private String RevFileName; // 후기 첨부파일 이름

    @Column(name = "R_RP")
    private int RRp ;

    // RevEntity 객체로 변환하는 메서드
    public static RevEntity toEntity(RevDTO dto) {
        RevEntity entity = new RevEntity();

        entity.setREVNum(dto.getREVNum());
        entity.setUserId(dto.getUserId());
        entity.setUserNickname(dto.getUserNickname());
        entity.setRevDate(dto.getRevDate());
        entity.setRevUpdate(dto.getRevUpdate());
        entity.setRevTitle(dto.getRevTitle());
        entity.setRevContent(dto.getRevContent());
        entity.setBNum(dto.getBNum());
        entity.setRating(dto.getRating());
        entity.setRevFileName(dto.getRevFileName());
        entity.setRHit(dto.getRHit());
        entity.setRevUpdate(dto.getRevUpdate());
        entity.setIsBlocked(dto.getIsBlocked());
        entity.setRRp(dto.getRRp());

        return entity;
    }
}
