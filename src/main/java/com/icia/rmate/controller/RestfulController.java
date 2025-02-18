package com.icia.rmate.controller;

import com.icia.rmate.dto.CommentDTO;
import com.icia.rmate.dto.RevDTO;
import com.icia.rmate.dto.SearchDTO;
import com.icia.rmate.dto.SearchOptionDTO;
import com.icia.rmate.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestfulController {

    private final UserService usvc;
    private final PhoneService psvc;
    private final HttpSession session;
    private final BoardService bsvc;
    private final CommentService csvc;
    private final ReviewService rsvc;

    // idCheck : 아이디 중복 체크
    @PostMapping("/idCheck")
    public String idCheck(@RequestParam("UserId") String userId) {
        return usvc.idCheck(userId);
    }

    // emailCheck : 이메일 인증번호 받아오기
    @PostMapping("/emailCheck")
    public String emailCheck(@RequestParam("UserEmail") String userEmail) {
        return usvc.emailCheck(userEmail);
    }

    // PhoneCheck : 휴대폰 인증번호 받아오기
    @PostMapping("/PhoneCheck")
    public String phoneCheck(@RequestParam("UserPhone") String userPhone) {
        System.out.println("휴대전화 :" + userPhone);
        return psvc.phoneCheck(userPhone);
    }


    // boardList (categoryNum 파라미터 받도록 수정)
    @PostMapping("/boardList")
    @ResponseBody
    public List<Object[]> boardList(HttpSession session, @RequestParam(value = "categoryNum", required = false) Integer categoryNum) {

        // 세션에서 searchOption 객체를 가져옵니다.
        SearchOptionDTO searchOption = (SearchOptionDTO) session.getAttribute("searchOption");

        // searchOption이 null이면 기본값으로 초기화
        if (searchOption == null) {
            searchOption = new SearchOptionDTO();
        }

        // categoryNum이 null이 아니면, searchOption에 설정
        if (categoryNum != null) {
            searchOption.setCategoryType(categoryNum);
        }

        System.out.println(searchOption);
        return bsvc.boardList(searchOption);
    }

    @PostMapping("/boardList2")
    @ResponseBody  // JSON 응답을 클라이언트로 보냄
    public List<Object[]> boardList2() {
        return bsvc.boardList2();
    }


    @PostMapping("/boardList3")
    @ResponseBody  // JSON 응답을 클라이언트로 보냄
    public List<Object[]> boardList3() {
        return bsvc.boardList3();
    }

    // revList : 게시판 목록 가져오기
    @PostMapping("/revList")
    public List<RevDTO> revList() {
        return rsvc.revList();
    }

    // bsearchList : 게시글 검색 목록 가져오기
    @PostMapping("/RsearchList")
    public List<RevDTO> rsearchList(@ModelAttribute SearchDTO search) {
        System.out.println("search : " + search); // 검색 조건 출력
        return rsvc.searchList(search);
    }

    // RestfulController
    @PostMapping("/nicknameCheck")
    public String nicknameCheck(@RequestParam("UserNickname") String userNickname) {
        System.out.println("nicknameCheck called with: " + userNickname); // 로깅
        return usvc.nicknameCheck(userNickname);
    }
}