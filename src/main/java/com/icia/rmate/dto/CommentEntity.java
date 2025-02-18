package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "COMMENTS")
@Data
@NoArgsConstructor // 기본 생성자 추가
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQ_GEN")
    @SequenceGenerator(name = "COMMENT_SEQ_GEN", sequenceName = "COMMENT_SEQ", allocationSize = 1, initialValue = 999)
    @Column(name = "C_NUM")
    private int CNum; // 댓글 번호 (PK)

    /*@Column(name = "USER_ID")
    private String UserId;*/
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "USER_ID",referencedColumnName = "USER_ID", updatable = false )
    private UserInfoEntity UserInfo; // 유저 아이디 (FK)

    @Column(name = "C_NICKNAME")
    private String CNickName; // 유저 닉네임

    /*@Column(name = "B_NUM")
    private int BNum; // 게시글 번호 (FK)*/
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "B_NUM",referencedColumnName = "B_NUM", updatable = false)
    private BoardEntity board;

    @Column(name = "C_CONTENT", columnDefinition = "CLOB")
    private String CContent; // 댓글 내용

    @CreationTimestamp
    @Column(name = "C_DATE")
    private LocalDateTime CDate; // 댓글 작성일

    @Column(name = "C_RP")
    private int CRp; // 댓글 신고

    @Column(name = "IS_BLOCKED")
    private boolean isBlocked;

    @Column(name = "B_TITLE")
    private String BTitle;    // 게시글 제목

    // 신고와의 관계 설정 (OneToMany)
    @ToString.Exclude
    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    private List<ReportEntity> reports; // 이 댓글에 대한 신고들


    // CommentDTO 객체를 CommentEntity로 변환하는 메서드
    public static CommentEntity toEntity(CommentDTO dto) {
        CommentEntity entity = new CommentEntity();

        // 각 필드를 DTO에서 받아서 엔티티로 세팅
        entity.setCNum(dto.getCNum());
        entity.setCNickName(dto.getCNickName());
        entity.setCContent(dto.getCContent());
        entity.setCDate(dto.getCDate());
        entity.setCRp(dto.getCRp());
        entity.setBlocked(dto.isBlocked());
        entity.setBTitle(dto.getBTitle());

        UserInfoEntity userInfo = new UserInfoEntity();
        userInfo.setUserId(dto.getUserId());
        userInfo.setUserNickname(dto.getCNickName());
        entity.setUserInfo(userInfo);

        BoardEntity board = new BoardEntity();
        board.setBNum(dto.getBNum());
        entity.setBoard(board);

        return entity;
    }
}
