//package com.icia.rmate.controller;
//
//import com.icia.rmate.dto.InviteUserDTO;
//import com.icia.rmate.service.ChatService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//@RequestMapping("/chat")
//public class ChatController {
//
//    private final ChatService chatService;
//
//    @Autowired
//    public ChatController(ChatService chatService) {
//        this.chatService = chatService;
//    }
//
//    // 메시지 보내기
//    @PostMapping("/send")
//    public String sendMessage(@RequestParam String userId, @RequestParam String messageContent) {
//        chatService.sendMessage(userId, messageContent);
//        return "Message sent successfully!";
//    }
//
//    // 실시간 메시지 받기
//    @GetMapping("/listen")
//    public String listenForMessages() {
//        chatService.listenForMessages();  // 실시간으로 메시지를 받아오는 메서드
//        return "Listening for new messages...";
//    }
//
//
//    @PostMapping("/invite")
//    public ResponseEntity<?> inviteUser(@RequestBody InviteUserDTO inviteUser) {
//        // 초대 처리 로직
//        return ResponseEntity.ok("사용자 초대 성공");
//    }
//
//    @GetMapping
//    public String showChatPage() {
//        return "chat"; // templates/chat.html 파일을 반환
//}}
