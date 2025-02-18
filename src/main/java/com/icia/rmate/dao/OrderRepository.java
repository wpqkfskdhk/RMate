package com.icia.rmate.dao;

import com.icia.rmate.dto.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    @Query("SELECT o FROM OrderEntity o WHERE o.UserId = :UserId")
    List<OrderEntity> findByUserId(@Param("UserId") String UserId);
}