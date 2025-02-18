package com.icia.rmate.service;


import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhoneService {

    public String phoneCheck(String userPhone) {

        String uuid = null;

        // 인증번호
        uuid = UUID.randomUUID().toString().substring(0,8);
        DefaultMessageService messageService =  NurigoApp.INSTANCE.initialize("NCSC7REWFTNGMSEF", "NZ8FBIWZ51CXFGBMCV3NC6SJM2PRVR0C", "https://api.coolsms.co.kr");
// Message 패키지가 중복될 경우 net.nurigo.sdk.message.model.Message로 치환하여 주세요
        Message message = new Message();
        message.setFrom("01080125320");
        message.setTo(userPhone);
        message.setText("안녕하세요. 태우고입니다. 인증번호는 " + uuid +"입니다! ");

        try {
            // send 메소드로 ArrayList<Message> 객체를 넣어도 동작합니다!
            messageService.send(message);
        } catch (NurigoMessageNotReceivedException exception) {
            // 발송에 실패한 메시지 목록을 확인할 수 있습니다!
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        return uuid;

    }
}
