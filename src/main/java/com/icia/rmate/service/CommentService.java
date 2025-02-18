package com.icia.rmate.service;

import com.icia.rmate.dao.CommentRepository;
import com.icia.rmate.dto.CommentDTO;
import com.icia.rmate.dto.CommentEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository crepo;
    private final HttpSession session;

    // 댓글 목록 조회
    public List<CommentDTO> commentList(int BNum) {
        List<CommentDTO> dtoList = new ArrayList<>();
        List<CommentEntity> entityList = crepo.findAllByBoard_BNum(BNum);

        for (CommentEntity entity : entityList){
            dtoList.add(CommentDTO.toDTO(entity));
        }

        return dtoList;
    }

    // 댓글 작성
    public List<CommentDTO> commentWrite(CommentDTO comment) {

        // 4. 댓글 저장
        CommentEntity entity = CommentEntity.toEntity(comment);
        crepo.save(entity);
        System.out.println("댓글 저장 성공"+ entity);

        // 댓글 입력 후 목록 불러오기
        List<CommentDTO> dtoList = commentList(comment.getBNum());
        return dtoList;
    }

    // 댓글 수정
    public List<CommentDTO> commentUpdate(CommentDTO comment) {
        // 1. CNum으로 댓글 엔티티 찾기
        CommentEntity entity = crepo.findById(comment.getCNum())
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다. CNum: " + comment.getCNum()));

        // 2. 댓글 내용 업데이트
        entity.setCContent(comment.getCContent());

        /*crepo.save(entity);*/
        System.out.println("댓글 저장 성공"+ entity);

        // 댓글 입력 후 목록 불러오기
        List<CommentDTO> dtoList = commentList(comment.getBNum());
        return dtoList;
    }

    // 댓글 삭제
    public List<CommentDTO> commentDelete(CommentDTO comment) {

        // 댓글 삭제
        crepo.deleteById(comment.getCNum());

        // 댓글 입력 후 목록 불러오기
        List<CommentDTO> dtoList = commentList(comment.getBNum());
        return dtoList;
    }

    // 댓글 신고
    @Transactional
    public String reportComment(int CNum, String loginId, HttpServletRequest request, HttpServletResponse response) {
        // 로그인 아이디 없으면 "Guest"로 설정
        if (loginId == null) {
            loginId = "Guest";
        }

        // 현재 쿠키를 담을 배열 생성
        Cookie[] cookies = request.getCookies();
        Cookie viewCookie = null;

        // 쿠키가 존재한다면
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cookie_" + loginId + "_" + CNum)) {
                    viewCookie = cookie;
                    break;
                }
            }
        }

        // 쿠키가 존재하지 않으면 신고 처리
        if (viewCookie == null) {
            String cookieName = "cookie_" + loginId + "_" + CNum;
            Cookie newCookie = new Cookie(cookieName, cookieName);
            newCookie.setMaxAge(60 * 60 * 24 * 365); // 1년 동안 유효
            response.addCookie(newCookie);

            // 신고 처리 (B_RP 증가)
            crepo.incrementCRp(CNum);

            return "신고가 정상적으로 처리되었습니다.";
        } else {
            return "이미 신고한 게시글입니다.";
        }
    }


    /*public List<Object[]> commentList(int BNum) {

        return crepo.CommentList(BNum);
    }*/


    public List<CommentDTO> getMyComments() {
        String userId = (String) session.getAttribute("loginId");
        System.out.println("userId : " + userId);

        if (userId == null) {
            System.out.println("userId가 null입니다.");
            return Collections.emptyList();
        }
        if (userId.isEmpty()) {
            System.out.println("userId가 비어있습니다.");
            return Collections.emptyList();
        }

        try {
            List<CommentEntity> commentEntities = crepo.findByUserId(userId);
            if (commentEntities.isEmpty()) {
                System.out.println("findByUserId 결과가 비어있습니다.");
                return Collections.emptyList();
            }
            for(CommentEntity comment : commentEntities){
                System.out.println("댓글 번호 : "+comment.getCNum()+", 댓글 내용 : "+comment.getCContent());
            }
            return commentEntities.stream()
                    .map(CommentDTO::toDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
