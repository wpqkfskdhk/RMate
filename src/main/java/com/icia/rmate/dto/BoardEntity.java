package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "BOARD")
@Data
public class BoardEntity{

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UB_SEQ_GEN")
        @SequenceGenerator(name = "UB_SEQ_GEN", sequenceName = "UB_SEQ2", allocationSize = 1 , initialValue = 999)
        @Column(name = "B_NUM")
        private int BNum; // 게시글 번호 (PK)

    @Column(name = "CATEGORY_NUM")
    private int CategoryNum;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_NUM", referencedColumnName = "CATEGORY_NUM", insertable = false, updatable = false)
    private CategoryEntity category; // 게시글 유형 (FK)

    @Column(name = "USER_ID")
    private String UserId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    private UserInfoEntity userInfo; // 유저 아이디 (FK)

    @Column(name = "B_NICKNAME")
    private String BNickName; // 유저 닉네임

    @CreationTimestamp
    @Column(name = "B_DATE")
    private LocalDateTime BDate; // 게시글 작성일

    @UpdateTimestamp
    @Column(name = "B_UPDATE")
    private LocalDateTime BUpdate;  // 게시글 수정일

    @Column(name = "CNT_NUM")
    private int CntNum;

    @Column(name = "COUNT_PEOPLE")
    private int CountPeople;

    @Column(name = "B_TITLE")
    private String BTitle; // 게시글 제목

    @Column(name = "B_CONTENT", columnDefinition = "CLOB")
    private String BContent; // 게시글 내용

    @Column(name = "B_FILENAME")
    private String BFileName; // 게시글 파일명

    @Column(name = "DETAIL_NUM")
    private int DetailNum;

    @ManyToOne
    @JoinColumn(name = "DETAIL_NUM", referencedColumnName = "DETAIL_NUM", insertable = false, updatable = false)
    private RegionDetailEntity regionDetail; // 상세주소번호(FK)

    @Column(name = "B_START")
    private LocalDate BStart; // 일정 시작

    @Column(name = "B_END")
    private LocalDate BEnd; // 일정 종료

    @Column(name = "B_RP")
    private int BRp; // 게시글 신고

    @Column(name = "M_TYPE")
    private String MType;

    @Column(name = "TAG_NAME")
    private String TagName;

    @Column(name = "IS_BLOCKED")
    private boolean isBlocked;  // 차단유무

    @Column(name = "T_NUM")
    private int TNum;

    @ManyToOne
    @JoinColumn(name = "T_NUM", referencedColumnName = "T_NUM", insertable = false, updatable = false)
    private TagEntity Tag;

    // Board와 댓글 간의 양방향 관계 설정
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentEntity> comments; // 댓글 리스트

    // 댓글과 신고 관계 설정 (OneToMany)
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<ReportEntity> reports; // 이 게시글에 대한 신고들

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<ApplyEntity> applyList; // 신청 리스트

        // BoardDTO 객체를 Board 엔티티로 변환하는 메서드
        public static BoardEntity toEntity(BoardDTO dto) {
            BoardEntity entity = new BoardEntity();

            // 각 필드를 DTO에서 받아서 엔티티로 세팅
            entity.setBNum(dto.getBNum());
            entity.setCategoryNum(dto.getCategoryNum());
            entity.setUserId(dto.getUserId());
            entity.setCntNum(dto.getCntNum());
            entity.setDetailNum(dto.getDetailNum());
            entity.setBNickName(dto.getBNickName());
            entity.setBTitle(dto.getBTitle());
            entity.setBContent(dto.getBContent());
            entity.setBFileName(dto.getBFileName());
            entity.setBStart(dto.getBStart());
            entity.setBEnd(dto.getBEnd());
            entity.setBRp(dto.getBRp());
            entity.setBUpdate(dto.getBUpdate());
            entity.setMType(dto.getMType());
            entity.setBlocked(dto.isBlocked());
            entity.setTNum(dto.getTNum());
            entity.setTagName(dto.getTagName());
            entity.setCountPeople(dto.getCountPeople());
            // BDate가 null이어도 설정 게시글 작성시에는 알아서 설정됩니다
            // 대신 수정폼테그에서 히든으로 bdate값을 집어넣어야만합니다.
                entity.setBDate(dto.getBDate());

            return entity;
        }
}
