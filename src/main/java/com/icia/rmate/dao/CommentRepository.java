package com.icia.rmate.dao;

import com.icia.rmate.dto.BoardEntity;
import com.icia.rmate.dto.CommentEntity;
import com.icia.rmate.dto.UserInfoEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    @Query(value = "SELECT U.USER_ID, C.C_CONTENT, C.C_RP, B.B_NUM " +
            "FROM COMMENTS C " +
            "JOIN USER_INFO U ON C.USER_ID = U.USER_ID " +
            "JOIN BOARD B ON C.B_Num = B.B_NUM " +
            "WHERE B.B_NUM = :BNum " +
            "ORDER BY C.C_NUM", nativeQuery = true)
    List<Object[]> CommentList(@Param("BNum") int BNum);  // BNum 파라미터로 받아서 사용


    @Query("SELECT c FROM CommentEntity c WHERE c.UserInfo.UserId = :UserId")
    List<CommentEntity> findByUserId(@Param("UserId") String UserId);


    List<CommentEntity> findAllByBoard_BNum(int bNum);

    @Modifying
    @Transactional
    @Query("UPDATE CommentEntity c SET c.CRp = c.CRp + 1 WHERE c.CNum = :cnum")
    void incrementCRp(@Param("cnum") int cNum);

    @Transactional
    @Modifying
    @Query("DELETE FROM CommentEntity c WHERE c.board.BNum = :bnum")
    void deleteBybnum(@Param("bnum") int bnum);
}
