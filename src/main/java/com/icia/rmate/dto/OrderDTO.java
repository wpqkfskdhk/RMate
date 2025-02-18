package com.icia.rmate.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class OrderDTO {

    private int Order_Id; // 주문 고유 번호 (PK)
    private String Merchant_Uid; // 주문 고유 번호
    private String Product_Name; // 상품명 (유류비)
    private int Amount; // 총금액
    private String Buyer_Name; // 구매자명
    private String Buyer_Tel; // 구매자 연락처
    private String Buyer_Addr; // 구매자 주소
    private String UserId; // 사용자 아이디 (FK)
    private LocalDateTime Created_At; // 생성일시

    // Entity -> DTO로 변환하는 메서드
    public static OrderDTO toDTO(OrderEntity entity) {
        OrderDTO dto = new OrderDTO();

        dto.setOrder_Id(entity.getOrder_Id());
        dto.setMerchant_Uid(entity.getMerchant_Uid());
        dto.setProduct_Name(entity.getProduct_Name());
        dto.setAmount(entity.getAmount());
        dto.setBuyer_Name(entity.getBuyer_Name());
        dto.setBuyer_Tel(entity.getBuyer_Tel());
        dto.setBuyer_Addr(entity.getBuyer_Addr());
        dto.setUserId(entity.getUserId()); // 외래 키는 해당 엔티티에서 ID만 가져오기
        dto.setCreated_At(entity.getCreated_At());

        return dto;
    }

}
