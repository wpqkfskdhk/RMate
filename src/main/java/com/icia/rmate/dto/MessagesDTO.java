package com.icia.rmate.dto;

import lombok.Data;

import java.io.Serializable;

// DTO는 직렬화 가능하도록 Serializable을 구현하는 것이 좋습니다.
@Data
public class MessagesDTO implements Serializable {

    private String userId;          // 메시지를 보낸 사용자 ID
    private String messageContent;   // 메시지 내용
    private long timestamp;         // 메시지가 전송된 시간 (옵션)
    private String roomId;

    public MessagesDTO(String userId, String messageContent) {
    }
}