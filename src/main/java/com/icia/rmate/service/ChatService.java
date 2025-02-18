//package com.icia.rmate.service;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.icia.rmate.dto.MessagesDTO;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.expression.Messages;
//
//@Service
//public class ChatService {
//
//    private final DatabaseReference databaseReference;
//
//    public ChatService() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        this.databaseReference = database.getReference("messages");  // 메시지를 저장할 경로
//    }
//
//    // 메시지 보내기
//    public void sendMessage(String userId, String messageContent) {
//        String messageId = databaseReference.push().getKey();
//        MessagesDTO message = new MessagesDTO(userId, messageContent);
//        databaseReference.child(messageId).setValueAsync(message);
//    }
//
//    // 메시지 읽기 - 실시간으로 데이터가 변경될 때마다 자동으로 호출
//    public void listenForMessages() {
//        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
//            @Override
//            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
//                // 데이터가 변경될 때마다 처리하는 로직
//                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    MessagesDTO message = snapshot.getValue(MessagesDTO.class);
//                    System.out.println("New Message: " + message.getMessageContent());
//                }
//            }
//
//            @Override
//            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
//                System.out.println("Failed to read value: " + databaseError.toException());
//            }
//        });
//    }
//}
