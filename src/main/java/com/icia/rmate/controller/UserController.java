package com.icia.rmate.controller;

import com.icia.rmate.dto.UserInfoDTO;
import com.icia.rmate.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService usvc;

    private final HttpSession session;

    // index : 처음페이지로 이동
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    // 유저 이용약관
    @PostMapping("/mTermAgree")
    public String termForm(@RequestParam List<Integer> TermsId, HttpSession session) {
        // 약관 동의 내역을 세션에 저장
        session.setAttribute("TermsId", TermsId);

        // 약관 동의 완료 후 메인 페이지로 리다이렉트, URL 파라미터 추가
        return "redirect:/?showModal=true";  // URL에 showModal=true 파라미터 추가
    }

    // mTermForm : 회원가입 약관페이지 이동
    @GetMapping("/mTermForm")
    public String mTermForm() {
        return "termForm";
    }


    // mJoinForm : 회원가입 페이지로 이동
    @GetMapping("/mJoinForm")
    public String mJoinForm() {
        return "join";
    }

    // mLoginForm : 로그인 페이지로 이동
    @GetMapping("/mLoginForm")
    public String mLoginForm() {
        return "login";
    }

    // mJoin : 회원가입
    @PostMapping("/mJoin")
    public ModelAndView mJoin(@ModelAttribute @Valid UserInfoDTO user, BindingResult bindingResult, HttpSession session) {
        List<Integer> TermsId = (List<Integer>) session.getAttribute("TermsId");

        // 약관 동의 여부 확인
        if (TermsId == null || TermsId.isEmpty()) {
            throw new RuntimeException("약관에 동의해야 합니다.");
        }

        // 유효성 검사 에러 확인
        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("joinForm"); // 회원가입 폼으로 다시 이동
            mav.addObject("user", user); // 기존 입력 데이터 유지
            mav.addObject("errors", bindingResult.getAllErrors()); // 에러 정보 추가
            return mav;
        }

        System.out.println("폼 작동 확인" + user + ", " + TermsId);
        return usvc.mJoin(user, TermsId);
    }

    // mdJoin : 회원수정
    @PostMapping("/mdJoin")
    public ModelAndView mdJoin(@ModelAttribute UserInfoDTO user) {
        System.out.println("폼 작동 확인" + user);
        return usvc.mdJoin(user);
    }

    // 로그인 요청을 처리하는 컨트롤러 메서드
    @PostMapping("/mLogin")
    public ModelAndView mLogin(@ModelAttribute UserInfoDTO user, RedirectAttributes redirectAttributes) {
        return usvc.mLogin(user, redirectAttributes);  // 서비스에서 수정된 mLogin 메서드를 호출
    }


    // mLogout :로그아웃
    @GetMapping("/mLogout")
    public String mLogout() {
        session.invalidate();
        return "index";
    }

    // 루트 URL로 접근하면 request-payment.html을 템플릿으로 렌더링하여 반환
    @GetMapping("/payForm")
    public String showPaymentPage() {
        // templates 폴더 내 pay.html 파일을 찾음
        return "pay";
    }


    //마이페이지로 이동
    @GetMapping("/myPageForm")
    public String myPage() {
        return "myPage";
    }

    //회원 수정페이지로이동
    @GetMapping("/mdJoinForm")
    public String mdJoin() {
        return "mdJoin";
    }


    @GetMapping("/deleteUser")
    public String deleteUser(HttpServletRequest request) {
        String loginId = (String) request.getSession().getAttribute("loginId");

        if (loginId != null) {
            usvc.deleteUserAndRelatedData(loginId);
            request.getSession().invalidate();
            return "redirect:/"; // 홈페이지로 리다이렉트
        } else {
            return "redirect:/login"; // 로그인 페이지로 리다이렉트 (또는 다른 적절한 페이지)
        }
    }


}


