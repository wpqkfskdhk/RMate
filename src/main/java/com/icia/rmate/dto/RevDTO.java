package com.icia.rmate.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
@Data
public class RevDTO {

    private int REVNum; // 후기 글번호
    private String UserId; // 유저 아이디
    private String UserNickname;    // 유저닉네임
    private LocalDateTime RevDate; // 후기 작성일
    private LocalDateTime RevUpdate; // 후기 수정일
    private String RevTitle; // 후기 제목
    private String RevContent; // 후기 내용
    private int BNum; // 카테고리 번호
    private int Rating; // 후기 평가
    private int RHit = 0;   // 조회수
    private Boolean IsBlocked = false;  // 차단유무
    private int RRp;          // 리뷰 신고


    private String btitle;

    private MultipartFile RevFile; //후기 파일
    private String RevFileName; // 후기 첨부파일 이름

    // RevDTO 객체로 변환하는 메서드
    public static RevDTO toDTO(RevEntity entity) {
        RevDTO dto = new RevDTO();

        dto.setREVNum(entity.getREVNum());
        dto.setUserId(entity.getUserId()); // 유저 아이디 값
        dto.setUserNickname(entity.getUserNickname());
        dto.setRevDate(entity.getRevDate());
        dto.setRevUpdate(entity.getRevUpdate());
        dto.setRevTitle(entity.getRevTitle());
        dto.setRevContent(entity.getRevContent());
        dto.setBNum(entity.getBNum()); // 카테고리 번호 값
        dto.setRating(entity.getRating());
        dto.setRevFileName(entity.getRevFileName());
        dto.setRHit(entity.getRHit());
        dto.setRevUpdate(entity.getRevUpdate());
        dto.setIsBlocked(entity.getIsBlocked());
        dto.setRRp(entity.getRRp());
        dto.setBtitle(entity.getBoard().getBTitle());

        return dto;
    }
}
