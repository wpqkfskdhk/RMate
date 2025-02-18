package com.icia.rmate.controller;

import com.icia.rmate.dto.CommentDTO;
import com.icia.rmate.service.CommentService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService csvc;

    // 댓글 목록 조회
    @PostMapping("/commentList")
    public List<CommentDTO> commentList(@RequestParam("BNum") int BNum) {
        System.out.println("BNum: " + BNum); // bNum 값 확인
        System.out.println("[1]html → ");
        return csvc.commentList(BNum);

    }

    // 댓글 작성
    @PostMapping("/commentWrite")
    public List<CommentDTO> commentWrite(@ModelAttribute CommentDTO comment) {
        System.out.println("\n댓글 작성\n[1]html → controller : " + comment);
        return csvc.commentWrite(comment);
    }

    // 댓글 작성
    @PostMapping("/commentUpdate")
    public List<CommentDTO> commentUpdate(@ModelAttribute CommentDTO comment) {
        System.out.println("\n댓글 수정\n[1]html → controller : " + comment);
        return csvc.commentUpdate(comment);
    }

    // 댓글 삭제
    @PostMapping("/commentDelete")
    public List<CommentDTO> commentDelete(@ModelAttribute CommentDTO comment) {
        System.out.println("\n댓글 삭제\n[1]html → controller : " + comment);
        return csvc.commentDelete(comment);
    }

    // 댓글 신고
    @PostMapping("/commentReport")
    public ResponseEntity<String> commentReport(@RequestParam("CNum") int CNum, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("commentReport 메서드 호출됨"); // 메서드 진입 확인
        // 로그인 아이디 가져오기
        String loginId = (String) request.getSession().getAttribute("loginId");

        // 로그인 아이디가 없으면 "Guest"로 설정
        if (loginId == null) {
            loginId = "Guest";
        }
        System.out.println("CNum: " + CNum); // CNum 값 확인
        System.out.println("loginId: " + loginId); // loginId 값 확인

        // 서비스에서 신고 처리 로직 호출
        String message = csvc.reportComment(CNum, loginId, request, response);
        System.out.println("csvc.reportComment 반환 값: " + message); // 반환 값 확인

        // 클라이언트에게 처리 결과 메시지를 반환
        return ResponseEntity.ok(message);
    }

}
