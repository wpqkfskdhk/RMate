package com.icia.rmate.dao;

import com.icia.rmate.dto.ApplyEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplyRepository extends JpaRepository<ApplyEntity, Integer> {
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM APPLY WHERE B_NUM = :bNum AND USER_ID = :userId", nativeQuery = true)
    Integer existsByBNumAndUserId(@Param("bNum") int bNum, @Param("userId") String userId);


    @Query("SELECT a FROM ApplyEntity a WHERE a.BNum IN :bNumList")
    List<ApplyEntity> findByBNumIn(@Param("bNumList") List<Integer> bNumList);

    @Query("SELECT a FROM ApplyEntity a WHERE a.UserId = :UserId")
    List<ApplyEntity> findByUserId(@Param("UserId") String UserId);

    @Query("SELECT a FROM ApplyEntity a WHERE a.BNum = :bNum AND a.AStatus = 1")
    List<ApplyEntity> findByBNumAndAStatusOne(@Param("bNum") int bNum); // bNum으로 여러 ApplyEntity 조회 (a_status = 1)

    @Query("SELECT a FROM ApplyEntity a WHERE a.BNum = :bNum AND a.AStatus = 1")
    List<ApplyEntity> findByBNumAndAStatus(@Param("bNum") int bNum);

    @Modifying
    @Transactional
    @Query("UPDATE ApplyEntity a SET a.RStatus = 1 WHERE a.BNum = :bNum and a.UserId = :UserId")
    void saveRStatus(@Param("bNum") int bNum,@Param("UserId") String UserId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ApplyEntity a WHERE a.BNum = :bnum")
    void deleteBybnum(@Param("bnum") int bnum);

    @Transactional
    @Modifying
    @Query("UPDATE ApplyEntity a SET a.PStatus = :pstatus WHERE a.BNum = :bnum AND a.UserId = :userId")
    int updatePStatusByBNumAndUserId(
            @Param("bnum") int bNum,
            @Param("userId") String userId,
            @Param("pstatus") int pStatus
    );
    @Query("SELECT a FROM ApplyEntity a WHERE a.BNum = :bNum and a.UserId = :UserId")
    ApplyEntity findByBNumAndUserId(@Param("bNum") int bNum,@Param("UserId") String UserId);
}
