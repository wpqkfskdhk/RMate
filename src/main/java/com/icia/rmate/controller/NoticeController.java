package com.icia.rmate.controller;

import com.icia.rmate.dto.NoticeDTO;
import com.icia.rmate.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDateTime;

// NoticeController
@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService snsvc;

    // 공지사항 저장 요청 처리
    @PostMapping("/admin/saveNotice")
    @ResponseBody
    public ResponseEntity<String> saveNotice(
            @RequestParam String title,
            @RequestParam String content,
            @SessionAttribute(value = "loginId", required = false) String loginId) {

        if (!"ADMIN".equalsIgnoreCase(loginId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자만 접근할 수 있습니다.");
        }

        // NoticeDTO에 값을 설정
        NoticeDTO notice = new NoticeDTO();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setAuthor(loginId);  // 작성자 설정
        notice.setCreatedDate(LocalDateTime.now()); // 작성 일시 설정

        // 공지사항 저장 로직
        snsvc.saveNotice(notice);

        return ResponseEntity.ok("공지사항 저장 완료");
    }
}
