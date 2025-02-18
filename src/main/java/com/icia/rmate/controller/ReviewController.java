package com.icia.rmate.controller;

import com.icia.rmate.dto.RevDTO;
import com.icia.rmate.dto.SearchDTO;
import com.icia.rmate.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService rsvc;

    // RWriteForm :게시글 작성 폼 이동
    @GetMapping("/RWriteForm")
    public String RWriteForm(@RequestParam("bNum") int bNum, Model model) {
        model.addAttribute("bNum", bNum); // 수정: bNum으로 변경
        System.out.println(bNum + "bnum확인:");
        return "/review/reviewform";
    }

    // RWrite : 게시글 작성
    @PostMapping("/RWrite")
    public ModelAndView bWrite(@ModelAttribute RevDTO rev) {
        System.out.println("\n게시글작성\n[1] rev : " + rev);
        return rsvc.RWrite(rev);
    }

    // RList : 리뷰 목록 페이지로 이동
    @GetMapping("/RList")
    public String rList(Model model, HttpSession session) {
        // searchDTO 객체를 생성 (또는 세션에서 가져오기)
        SearchDTO searchDTO = new SearchDTO(); // 기본 생성자로 객체 생성

        // 세션에서 검색 관련 정보를 가져와 searchDTO에 설정 (필요한 경우)
        // SearchOptionDTO searchOption = (SearchOptionDTO) session.getAttribute("searchOption");
        // if (searchOption != null) {
        //     // searchOption의 값들을 searchDTO에 설정 (예: 카테고리, 키워드 등)
        // }

        // 모델에 searchDTO 객체를 추가
        model.addAttribute("searchDTO", searchDTO);

        // 다른 모델 속성 추가 (필요한 경우)
        // model.addAttribute("revList", rsvc.revList());

        return "/review/reviewboard"; // reviewboard.html 템플릿 반환
    }

    // RView : 리뷰 상세보기
    @GetMapping("/revView/{REVNum}")
    public ModelAndView RView(@PathVariable int REVNum) {
        return rsvc.RView(REVNum);
    }

    // RModify : 리뷰 수정
    @PostMapping("/RModify")
    public ModelAndView RModify(@ModelAttribute RevDTO rev) {
        System.out.println("\n게시글 수정 메소드\n[1]html → controller : " + rev);
        return rsvc.RModify(rev);
    }

    // RDelete : 리뷰 삭제
    @GetMapping("/RDelete")
    public ModelAndView RDelete(@ModelAttribute RevDTO rev) {
        System.out.println("\n게시글 삭제 메소드\n[1]html → controller : " + rev);
        return rsvc.RDelete(rev);
    }
    // reportReview : 리뷰 신고
    @PostMapping("/reportReview")
    public ResponseEntity<String> reportReview(@RequestParam("REVNum") int REVNum, HttpServletRequest request, HttpServletResponse response) {
        // 로그인 아이디 가져오기
        String loginId = (String) request.getSession().getAttribute("loginId");

        // 로그인 아이디가 없으면 "Guest"로 설정
        if (loginId == null) {
            loginId = "Guest";
        }

        // 서비스에서 신고 처리 로직 호출
        String message = rsvc.reportReview(REVNum, loginId, request, response);

        // 클라이언트에게 처리 결과 메시지를 반환
        return ResponseEntity.ok(message);
    } // 컨트롤러



    @GetMapping("/myReviewList")
    @ResponseBody
    public ResponseEntity<List<RevDTO>> myReviewList(HttpServletRequest request) {

        // 로그인 아이디 가져오기
        String loginId = (String) request.getSession().getAttribute("loginId");

        if (loginId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<RevDTO> myReviews = rsvc.getMyReviews(loginId);

        System.out.println("mypage 리뷰 dto 확인 ! " + myReviews);

        if (myReviews.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(myReviews, HttpStatus.OK);
    }
}