package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ORDERS")
@Data
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_SEQ_GEN")
    @SequenceGenerator(name = "ORDER_SEQ_GEN", sequenceName = "ORDER_SEQ", allocationSize = 1, initialValue = 999)
    @Column(name = "ORDER_ID")
    private int Order_Id; // 주문 고유 번호 (PK)

    @Column(name = "MERCHANT_UID", nullable = false, unique = true, length = 30)
    private String Merchant_Uid; // 주문 고유 번호

    @Column(name = "PRODUCT_NAME", nullable = false, length = 30)
    private String Product_Name; // 상품명 (유류비)

    @Column(name = "AMOUNT", nullable = false)
    private int Amount; // 총금액

    @Column(name = "BUYER_NAME", nullable = false, length = 30)
    private String Buyer_Name; // 구매자명

    @Column(name = "BUYER_TEL", nullable = false, length = 30)
    private String Buyer_Tel; // 구매자 연락처

    @Column(name = "BUYER_ADDR", nullable = false, length = 30)
    private String Buyer_Addr; // 구매자 주소

    @Column(name = "USER_ID")
    private String UserId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    private UserInfoEntity UserInfo; // 사용자 아이디 (FK)


    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private LocalDateTime Created_At;

    // DTO -> Entity로 변환하는 메서드
    public static OrderEntity toEntity(OrderDTO dto) {
        OrderEntity entity = new OrderEntity();

        entity.setOrder_Id(dto.getOrder_Id());
        entity.setMerchant_Uid(dto.getMerchant_Uid());
        entity.setProduct_Name(dto.getProduct_Name());
        entity.setAmount(dto.getAmount());
        entity.setBuyer_Name(dto.getBuyer_Name());
        entity.setBuyer_Tel(dto.getBuyer_Tel());
        entity.setBuyer_Addr(dto.getBuyer_Addr());
        entity.setUserId(dto.getUserId());

        entity.setCreated_At(dto.getCreated_At());

        return entity;
    }
}
