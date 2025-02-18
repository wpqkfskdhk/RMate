package com.icia.rmate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icia.rmate.dao.ApplyRepository;
import com.icia.rmate.dao.BoardRepository;
import com.icia.rmate.dao.CommentRepository;
import com.icia.rmate.dao.CourseRepository;
import com.icia.rmate.dto.*;
import com.icia.rmate.service.*;
import com.icia.rmate.util.JsonResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;  // Model import 추가
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final CommentService csvc; // CommentService 주입
    private final HttpSession session;
    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository course;
    private final BoardService bsvc;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CommentRepository  crepo;
    @Autowired
    private ReviewService rsvc;

    @Autowired
    private ApplyRepository arepo;
    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/board1Form")
    public String board1Form() {
        return "Board/board1";
    }

    @GetMapping("/board2Form")
    public String board2Form() {
        return "Board/board2";
    }

    @GetMapping("/board3Form")
    public String board3Form() {
        return "Board/board3";
    }

    @GetMapping("/board4Form")
    public String board4Form() {
        return "Board/board4";
    }

    @GetMapping("/boardformForm")
    public String boardformForm() {
        return "Board/boardform";
    }

    // 게시글 작성
    @PostMapping("/bWrite")
    public ModelAndView boardForm(@ModelAttribute BoardDTO board) {
        System.out.println(board);
        System.out.println(board.getBDate()+"bdate확인");
        return bsvc.boardForm(board);
    }



    @GetMapping("/boarddetail/{bNum}")
    public String getBoardDetail(@PathVariable("bNum") int bNum, Model model) {
        Object[] result = boardRepository.boardDetail(bNum);


        model.addAttribute("result", result);

        return "Board/boarddetail"; // boardDetail.html로 이동
    }


    @Autowired
    private BoardOptionService boardService;

    @PostMapping("/saveSearchOption")
    public String saveSearchOption(SearchOptionDTO searchOption, HttpSession session) {
        // 기존 세션의 searchOption 객체를 제거 (세션 초기화)
        session.removeAttribute("searchOption");
        System.out.println("서치 dto 작동확인 : "+searchOption);
        // 새로운 searchOption 객체를 세션에 저장
        session.setAttribute("searchOption", searchOption);

        // 저장 후, boardList 페이지로 리다이렉트
        return "redirect:/board1Form";
    }
    @PostMapping("/reportBoard")
    public ResponseEntity<String> reportBoard(@RequestParam("BNum") int BNum, HttpServletRequest request, HttpServletResponse response) {
        // 로그인 아이디 가져오기
        String loginId = (String) request.getSession().getAttribute("loginId");

        // 로그인 아이디가 없으면 "Guest"로 설정
        if (loginId == null) {
            loginId = "Guest";
        }

        // 서비스에서 신고 처리 로직 호출
        String message = bsvc.reportBoard(BNum, loginId, request, response);



        // 클라이언트에게 처리 결과 메시지를 반환
        return ResponseEntity.ok(message);
    }
    @PostMapping("/addCourse")
    @ResponseBody
    public ResponseEntity<JsonResult> addCourse(@RequestBody Map<String, Object> payload) throws IOException {
        // 테이블 행의 수를 확인합니다.
        int bNum = Integer.parseInt(payload.get("bNum").toString());

        long courseCount =courseRepository.countByBnum(bNum);

        if (courseCount >= 15) {
            return new ResponseEntity<>(JsonResult.fail(new RuntimeException("코스수를 더 추가할수 없습니다.")), HttpStatus.CONFLICT); // 409 Conflict 상태 코드 반환
        }

        Map<String, Object> course = (Map<String, Object>) ((List) payload.get("courseList")).get(0);
        String oLink = (String) payload.get("oLink");

        String userId = (String) payload.get("userId");
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setId(Long.parseLong(course.get("id").toString()));
        courseEntity.setName((String) course.get("name"));
        courseEntity.setAddress((String) course.get("address"));
        courseEntity.setPhone((String) course.get("phone"));
        courseEntity.setX(Double.parseDouble(course.get("x").toString()));
        courseEntity.setY(Double.parseDouble(course.get("y").toString()));
        courseEntity.setRegDt(new ObjectMapper().convertValue(course.get("regDt"), Date.class));
        courseEntity.setModDt(new ObjectMapper().convertValue(course.get("modDt"), Date.class));
        courseEntity.setBNum(bNum);
        courseEntity.setOLink(oLink);
        courseEntity.setUserId(userId);
        courseEntity.setCStatus(Integer.parseInt(String.valueOf(course.get("CStatus"))));


        // 주소가 이미 존재하는지 확인
        Optional<CourseEntity> existingCourse = courseRepository.findByAddressAndBNum(courseEntity.getAddress(),bNum);

        System.out.println(course+ ": existingCourse 확인");
        // 주소가 이미 존재한다면 저장하지 않음
        if (existingCourse.isPresent()) {
            return new ResponseEntity<>(JsonResult.fail(new Exception("이미 동일한 주소가 존재합니다.")), HttpStatus.CONFLICT); // 409 Conflict 상태 코드 반환
        }

        // 존재하지 않으면 저장
        courseService.addCourse(courseEntity);
        return new ResponseEntity<>(new JsonResult().success(), HttpStatus.OK); // 200 OK 상태 코드 반환
    }
    @GetMapping("/myboardList")
    public ResponseEntity<List<BoardDTO>> myboardList() {
        List<BoardDTO> myBoards = bsvc.getMyBoards();
        System.out.println(myBoards + ": dto로 변환된 myBoards를 확인하라!");

        if (myBoards == null || myBoards.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(myBoards, HttpStatus.OK);
        }
    }


    @GetMapping("/myCommentList")
    public ResponseEntity<List<CommentDTO>> myCommentList() {
        List<CommentDTO> myComments = csvc.getMyComments(); // 서비스에서 댓글 목록 가져오기
        System.out.println(myComments + ": dto로 변환된 myComments를 확인하라!");


        if (myComments == null || myComments.isEmpty()) { // 댓글이 없으면 NO_CONTENT 상태 코드 반환
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(myComments, HttpStatus.OK);
        }
    }



    @PostMapping("/boardToHistory")
    @ResponseBody
    public ResponseEntity<String> boardToHistory(@RequestParam("BNum") int BNum) {

        try{
            String message = bsvc.boardToHistory(BNum);
            return new ResponseEntity<>(message, HttpStatus.OK); //200
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); //400
        }catch (Exception e){
            return new ResponseEntity<>("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }


    @GetMapping("/getCategoryNum")
    @ResponseBody
    public ResponseEntity<Integer> getCategoryNum(@RequestParam("bNum") int bNum) {
        try {
            int categoryNum = bsvc.getCategoryNum(bNum);
            return new ResponseEntity<>(categoryNum, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/boardDelete")
    @ResponseBody
    public ResponseEntity<String> deleteBoard(@RequestParam("BNum") int bnum) {
        // bnum을 사용하여 게시글 삭제 로직 구현
        System.out.println("삭제할 게시글 번호: " + bnum);

        try {
            crepo.deleteBybnum(bnum);
            arepo.deleteBybnum(bnum);
            course.deleteBybnum(bnum);

            boardRepository.deleteBybnum(bnum);

            return new ResponseEntity<>("게시글 삭제 성공 태우고로 이동합니다!", HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("게시글 삭제 실패: " + e.getMessage());
            return new ResponseEntity<>("게시글 삭제 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/checkReview")
    @ResponseBody
    public String checkReview(@RequestParam("BNum") int bNum, HttpServletRequest request) {
        // 로그인 아이디 가져오기
        String loginId = (String) request.getSession().getAttribute("loginId");
        List<RevEntity> reviewExists = rsvc.checkReviewExists(bNum,loginId);
        if (reviewExists != null) {
            return "이미 리뷰가 작성된 게시글입니다.";
        } else {
            return "리뷰 작성이 가능합니다.";
        }
    }
    @PostMapping("/searchBoardList")
    @ResponseBody
    public ResponseEntity<?> searchBoardList(
            @RequestParam Integer categoryNum,
            @RequestParam String category,
            @RequestParam String keyword) {

        if (categoryNum == null || category == null || category.trim().isEmpty() ||
                keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("필수 파라미터가 누락되었습니다.");
        }

        try {
            List boardList = bsvc.searchList(categoryNum, category, keyword);
            return ResponseEntity.ok(boardList);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 내부 오류가 발생했습니다.");
        }
    }
}







