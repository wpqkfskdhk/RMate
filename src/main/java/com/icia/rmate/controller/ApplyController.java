package com.icia.rmate.controller;

import com.icia.rmate.dao.BoardRepository;
import com.icia.rmate.dto.*;
import com.icia.rmate.service.ApplyService;
import com.icia.rmate.service.CourseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ApplyController {

    private final HttpSession session;
    @Autowired
    private CourseService courseService;
    @Autowired
    private BoardRepository BoardRepository;  // BoardRepository를 인스턴스 변수로 선언
    @Autowired
    private ApplyService applyService;

    // /applyForm/{bNum}/{categoryNum} 형태로 URL을 처리
    @GetMapping("/applyForm/{bNum}/{categoryNum}")
    public ModelAndView applyForm(@PathVariable int bNum, @PathVariable int categoryNum) {
        // 경로 변수 처리
        System.out.println("BNum: " + bNum);
        System.out.println("CategoryNum: " + categoryNum);

        ModelAndView mav = new ModelAndView("Board/applyForm");  // applyForm.html 또는 해당 뷰 이름
        mav.addObject("bNum", bNum);
        mav.addObject("categoryNum", categoryNum);

        return mav;
    }


    @PostMapping("/apply")
    public String submitApplyForm(@ModelAttribute ApplyDTO applyDTO, HttpSession session, Model model) {
        try {
            // ApplyService 인스턴스를 통해 isAlreadyApplied 메서드 호출

            boolean alreadyApplied = applyService.isAlreadyApplied(applyDTO.getBNum(), (String) session.getAttribute("loginId"));

            // 이미 신청했으면 바로 리다이렉트
            if (alreadyApplied) {
                session.setAttribute("error", "이미 신청하셨습니다.");
                return "redirect:/boarddetail/" + applyDTO.getBNum(); // BNum을 URL에 포함
            }

            // 유효성 검사 후 신청 데이터 저장
            applyService.saveApply(applyDTO);

            // 게시물 번호를 통해 해당 게시물을 찾아 COUNT_PEOPLE을 1 증가
            BoardEntity board = BoardRepository.findById(applyDTO.getBNum())
                    .orElseThrow(() -> new EntityNotFoundException("Board not found"));

            // COUNT_PEOPLE 증가
            board.setCountPeople(board.getCountPeople() + 1);

            System.out.println("인원수 체크" + board.getCountPeople());
            System.out.println("인원수 체크" + board.getCountPeople());
            System.out.println("인원수 체크" + board.getCountPeople());

            // 업데이트된 게시물 저장
            BoardRepository.save(board);

            // 저장 후 리다이렉트
            return "redirect:/boarddetail/" + applyDTO.getBNum(); // BNum을 URL에 포함
        } catch (DataIntegrityViolationException e) {
            session.setAttribute("error", "이미 신청하셨습니다.");
            return "redirect:/boarddetail/" + applyDTO.getBNum(); // BNum을 URL에 포함
        } catch (Exception e) {
            System.out.println(e + "에러확인");
            session.setAttribute("error", "신청 처리 중 오류가 발생했습니다.");
            return "redirect:/boarddetail/" + applyDTO.getBNum(); // BNum을 URL에 포함
        }
    }

    @ResponseBody
    @PostMapping("/clearError")
    public String clearErrorMessage(HttpSession session) {
        System.out.println(session);
        session.removeAttribute("error"); // 세션에서 오류 메시지 삭제
        System.out.println(session);
        return null; // 클라이언트에 성공 메시지 반환
    }


    @GetMapping("/myApplicantList")
    public ResponseEntity<List<ApplyDTO>> myApplicantList() {

        List<ApplyDTO> myApplicants = applyService.getMyApplicants();
        System.out.println(myApplicants + ": dto로 변환된 myApplicants를 확인하라!");

        // 디버깅을 위해 myApplicants 내용을 출력
        if (myApplicants.isEmpty()) {
            System.out.println("myApplicants is empty");
        } else {
            myApplicants.forEach(System.out::println);
        }

        if (myApplicants.isEmpty()) { // 신청자가 없으면 NO_CONTENT 상태 코드 반환
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(myApplicants, HttpStatus.OK);
        }
    }


    @PostMapping("/increaseAStatus/{aNum}")
    public ResponseEntity<String> increaseAStatus(@PathVariable int aNum) {
        String loginId = (String) session.getAttribute("loginId"); // 세션에서 loginId 가져오기
        if (loginId == null || loginId.isEmpty()) { // 로그인 상태가 아니면 401 Unauthorized 상태 코드 반환
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            applyService.increaseAStatus(aNum);
            return new ResponseEntity<>("신청 상태 변경 성공", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("신청 상태 변경 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/myAppliedBoardList")
    public ResponseEntity<List<ApplyDTO>> myAppliedBoardList() {
        String loginId = (String) session.getAttribute("loginId"); // 세션에서 loginId 가져오기
        if (loginId == null || loginId.isEmpty()) { // 로그인 상태가 아니면 401 Unauthorized 상태 코드 반환
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<ApplyDTO> myAppliedBoards = applyService.getMyAppliedBoards();
        System.out.println(myAppliedBoards + ": dto로 변환된 myAppliedBoards를 확인하라!");

        if (myAppliedBoards.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(myAppliedBoards, HttpStatus.OK);
        }
    }

    // bNum으로 조회
    @GetMapping("/list/{bNum}")
    public ResponseEntity<?> getApplyListByBNum(@PathVariable int bNum) {
        System.out.println(bNum);
        try {
            List<ApplyDTO> applyList = applyService.getApplyListByBNum(bNum);
            System.out.println(applyList + "applyList");
            return new ResponseEntity<>(applyList, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "게시글 조회 실패: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/courseList/{bNum}")
    public ResponseEntity<?> getCourseListByBNum(@PathVariable int bNum) {
        try {
            System.out.println(bNum + "courselist bnum확인!");
            List<CourseDTO> courseList = courseService.getCourseListByBNum(bNum);
            System.out.println(courseList);
            return new ResponseEntity<>(courseList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/checkApply")
    public ResponseEntity<?> checkApplyStatus(@RequestParam("BNum") int bNum, HttpSession session) {

        try {
            List<ApplyDTO> applyList = applyService.checkApplyStatus(bNum);
            System.out.println(applyList + "applyList");
            return new ResponseEntity<>(applyList, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "게시글 조회 실패: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 신청 거절
    @PostMapping("/deleteApplication/{aNum}") // 엔드포인트 이름 변경 (예시)
    public ResponseEntity<String> deleteApplication(@PathVariable int aNum) {
        String loginId = (String) session.getAttribute("loginId");
        if (loginId == null || loginId.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            applyService.deleteApplication(aNum); // 삭제 메서드 호출
            return new ResponseEntity<>("신청 삭제 성공", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("신청 삭제 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/updatePStatus") // New endpoint URL
    @ResponseBody // Added @ResponseBody to return String directly
    public ResponseEntity<String> updatePStatus(
            @RequestParam("bNum") int bNum,
            @SessionAttribute("loginId") String loginId // Get userId from session
    ) {
        try {
            applyService.updatePStatus(bNum, loginId); // Call the service method to update PStatus
            return new ResponseEntity<>("결제 상태 업데이트 성공", HttpStatus.OK); // Return success message
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // Return error message for bad request
        } catch (Exception e) {
            return new ResponseEntity<>("서버 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR); // Return generic server error
        }
    }
}
