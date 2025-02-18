package com.icia.rmate.dao;

import com.icia.rmate.dto.BoardEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {

//    @Query(value = "SELECT B.B_NUM, B.B_TITLE, B.B_CONTENT, B.B_DATE, C.CATEGORY_NAME, " +
//            "R.R_ADDR, M.CNT_NAME, B.B_GENDER, M.CNT_NAME " +
//            "FROM BOARD B " +
//            "JOIN CATEGORY C ON B.CATEGORY_NUM = C.CATEGORY_NUM " +
//            "JOIN REGION R ON B.R_NUM = R.R_NUM " +
//            "JOIN MEMBER_COUNT M ON B.CNT_NUM = M.CNT_NUM " +
//            "WHERE (:regionId IS NULL OR B.R_NUM = :regionId) " +             // 지역 필터
//            "AND (:ageRange IS NULL OR B.A_NUM = :ageRange) " +               // 연령대 필터
//            "AND (:gender IS NULL OR B.B_GENDER = :gender) " +                // 성별 필터
//            "AND (:memberCount IS NULL OR B.CNT_NUM = :memberCount) " +       // 인원수 필터
//            "AND (:categoryType IS NULL OR B.CATEGORY_NUM = :categoryType) " + // 카테고리 타입 필터
//            "AND (:keyword IS NULL OR B.B_TITLE LIKE %:keyword% OR B.B_CONTENT LIKE %:keyword%) " +  // 키워드 필터
//            "ORDER BY B.B_DATE DESC", nativeQuery = true)
//    List<Object[]> searchBoards(@Param("regionId") Integer regionId,
//                                @Param("ageRange") Integer ageRange,
//                                @Param("memberCount") Integer memberCount,
//                                @Param("categoryType") Integer categoryType,
//                                @Param("keyword") String keyword);

    @Query("SELECT b FROM BoardEntity b WHERE b.UserId = :UserId")
    List<BoardEntity> findByUserIdQuery(@Param("UserId") String UserId);

    @Query(value = "SELECT B.B_NUM, B.B_TITLE, B.B_CONTENT, B.B_DATE, B.B_NICKNAME, B.B_GENDER, " +
            "C.CATEGORY_NAME, U.USER_ID, U.USER_NAME, M.CNT_NAME, B.B_FILENAME, RD.R_ADDR_DETAIL, RD.REGION_X, RD.REGION_Y," +
            "B.B_START, B.B_END, B.B_RP, MA.MATE_TYPE " +
            "FROM BOARD B " +
            "JOIN CATEGORY C ON B.CATEGORY_NUM = C.CATEGORY_NUM " +
            "JOIN REGION_DETAIL RD ON B.DETAIL_NUM = RD.DETAIL_NUM " +
            "JOIN USER_INFO U ON B.USER_ID = U.USER_ID " +                      // USER_INFO 조인
            "JOIN MEMBER_COUNT M ON B.CNT_NUM = M.CNT_NUM " +                  // MEMBER_COUNT 조인
            "JOIN MATE MA ON B.MATE_NUM = MA.MATE_NUM " +                  // MEMBER_COUNT 조인
            "WHERE (:regionId IS NULL OR B.DETAIL_NUM = :regionId) " +              // 지역 필터
            "AND (:ageRange IS NULL OR B.A_NUM = :ageRange) " +                // 연령대 필터
            "AND (:mateType IS NULL OR MA.MATE_NUM = :mateType) " +                // 연령대 필터
            "AND (:categoryType IS NULL OR B.CATEGORY_NUM = :categoryType) " + // 카테고리 타입 필터
            "AND (:keyword IS NULL OR B.B_TITLE LIKE %:keyword% OR B.B_CONTENT LIKE %:keyword%) " + // 키워드 필터
            "ORDER BY B.B_DATE DESC", nativeQuery = true)
    List<Object[]> searchBoards2(@Param("regionId") Integer regionId,
                                 @Param("ageRange") Integer ageRange,
                                 @Param("mateType") Integer mateType,
                                 @Param("categoryType") Integer categoryType,
                                 @Param("keyword") String keyword);


    //B_FILENAME,B_TITLE,R_NUM을통해 REGEION 테이블에서 받아오는 R.ADDR, CNT_NUM을 FK을 통해 MEMBER_COUNT 테이블에서 받아오는 CNT_NAME 를 조회하는 메서드

    @Query(value = "SELECT B.B_TITLE, " +
            " M.CNT_NAME, B.B_FILENAME, RD.R_ADDR_DETAIL, " +
            "B.B_START, B.B_END " +
            "FROM BOARD B " +
            "JOIN CATEGORY C ON B.CATEGORY_NUM = C.CATEGORY_NUM " +
            "JOIN REGIONDETAIL RD ON B.R_NUM = RD.DETAIL_NUM " +
            "JOIN USER_INFO U ON B.USER_ID = U.USER_ID " + // USER_INFO 조인
            "JOIN MEMBER_COUNT M ON B.CNT_NUM = M.CNT_NUM " + // MEMBER_COUNT 조인
            "ORDER BY B.B_DATE DESC", nativeQuery = true)
    List<Object[]> searchBoards3();

    @Query(value = "SELECT B.B_NUM, B.B_TITLE, B.CNT_NUM, B.COUNT_PEOPLE, B.B_FILENAME, RD.DETAIL_NUM, B.B_START, B.B_END, RD.R_NUM, R.R_ADDR " +
            "FROM BOARD B " +
            "JOIN CATEGORY C ON B.CATEGORY_NUM = C.CATEGORY_NUM " +
            "JOIN REGION_DETAIL RD ON B.DETAIL_NUM = RD.DETAIL_NUM " +
            "JOIN USER_INFO U ON B.USER_ID = U.USER_ID " +
            "JOIN REGION R ON RD.R_NUM = R.R_NUM " +
            "LEFT JOIN TAG T ON B.T_NUM = T.T_NUM " +
            "WHERE (:rNum IS NULL OR RD.R_NUM = :rNum) " +
            "AND (:memberCount IS NULL OR B.CNT_NUM = :memberCount) " +
            "AND (:categoryType IS NULL OR B.CATEGORY_NUM = :categoryType) " +
            "AND (:keyword IS NULL OR B.B_TITLE LIKE '%' || :keyword || '%' OR B.B_CONTENT LIKE '%' || :keyword || '%' OR T.T_NAME LIKE '%' || :keyword || '%') " +
            "AND (:tNum IS NULL OR B.T_NUM = :tNum) " +
            "AND B.CATEGORY_NUM != 4 " + //추가된 부분
            "AND B.IS_BLOCKED = 0 " +
            "ORDER BY B.B_DATE DESC", nativeQuery = true)
    List<Object[]> searchBoardsWithJoin(@Param("rNum") Integer rNum, // 수정된 부분
                                        @Param("memberCount") Integer memberCount,
                                        @Param("categoryType") Integer categoryType,
                                        @Param("keyword") String keyword,
                                        @Param("tNum") Integer tNum);


    @Query(value = "SELECT B.B_NUM, B.B_TITLE, " +
            " B.CNT_NUM,B.COUNT_PEOPLE, B.B_FILENAME, RD.DETAIL_NUM, " +
            " B.B_START, B.B_END, RD.R_NUM, R.R_ADDR " +
            "FROM BOARD B " +
            "JOIN CATEGORY C ON B.CATEGORY_NUM = C.CATEGORY_NUM " +
            "JOIN REGION_DETAIL RD ON B.DETAIL_NUM = RD.DETAIL_NUM " +
            "JOIN USER_INFO U ON B.USER_ID = U.USER_ID " +  // USER_INFO 조인
            "JOIN REGION R ON RD.R_NUM = R.R_NUM " +
            "WHERE C.CATEGORY_NUM = 2 " +
            "ORDER BY B.B_DATE DESC", nativeQuery = true)
    List<Object[]> Board2();

    @Query(value = "SELECT B.B_NUM, B.B_TITLE, " +
            " B.CNT_NUM,B.COUNT_PEOPLE, B.B_FILENAME, RD.DETAIL_NUM, " +
            " B.B_START, B.B_END, RD.R_NUM, R.R_ADDR " +
            "FROM BOARD B " +
            "JOIN CATEGORY C ON B.CATEGORY_NUM = C.CATEGORY_NUM " +
            "JOIN REGION_DETAIL RD ON B.DETAIL_NUM = RD.DETAIL_NUM " +
            "JOIN USER_INFO U ON B.USER_ID = U.USER_ID " +  // USER_INFO 조인
            "JOIN REGION R ON RD.R_NUM = R.R_NUM " +
            "WHERE C.CATEGORY_NUM = 3 " +
            "ORDER BY B.B_DATE DESC", nativeQuery = true)
    List<Object[]> Board3();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO BOARD (B_NUM, B_CONTENT, B_DATE, B_END, B_FILENAME, B_RP, B_START, B_TITLE, CATEGORY_NUM, CNT_NUM, DETAIL_NUM, USER_ID, M_NUM) " +
            "VALUES (UB_SEQ.NEXTVAL, :bContent, :bDate, :bEnd, :bFileName, :bRp, :bStart, :bTitle, :categoryNum, :cntNum, :rdNum, :userId, :mNum)",
            nativeQuery = true)
    void saveBoard(@Param("bContent") String bContent,             // 게시글 내용
                   @Param("bDate") LocalDateTime bDate,             // 게시글 작성 날짜
                   @Param("bEnd") LocalDate bEnd,                   // 종료일
                   @Param("bRp") Integer bRp,                       // B_RP (예: 게시글 추천수)
                   @Param("bStart") LocalDate bStart,               // 시작일
                   @Param("bTitle") String bTitle,                  // 게시글 제목
                   @Param("aNum") Integer aNum,                     // 연령대
                   @Param("categoryNum") Integer categoryNum,       // 카테고리 번호
                   @Param("cntNum") Integer cntNum,                 // 회원 수
                   @Param("rdNum") Integer rNum,                     // 지역 번호
                   @Param("userId") String userId);                 // 사용자 ID

    @Query(value = "SELECT B.B_NUM, C.CATEGORY_NAME, " +
            "TO_CHAR(B.B_CONTENT), B.B_DATE, B.B_END, B.B_FILENAME, " +
            "B.B_NICKNAME, B.B_RP, B.B_START, B.B_TITLE, B.B_UPDATE, " +
            "B.COUNT_PEOPLE, B.M_TYPE, B.TAG_NAME,   B.USER_ID, " +
            "R.R_ADDR, T.T_NAME, B.CNT_NUM, C.CATEGORY_NUM, B.T_NUM, B.DETAIL_NUM,B.TAG_NAME ,RD.R_ADDR_DETAIL, R.R_ADDR AS RAddr " +  // R.R_ADDR AS RAddr로 수정 (만약 R_ADDR이 원하는 주소 정보 컬럼이라면)
            "FROM BOARD B " +
            "JOIN CATEGORY C ON B.CATEGORY_NUM = C.CATEGORY_NUM " +
            "JOIN REGION_DETAIL RD ON B.DETAIL_NUM = RD.DETAIL_NUM " +
            "JOIN USER_INFO U ON B.USER_ID = U.USER_ID " +
            "JOIN REGION R ON RD.R_NUM = R.R_NUM " +
            "JOIN TAG T ON B.T_NUM = T.T_NUM " +
            "WHERE B.B_NUM = :bNum " +
            "AND B.IS_BLOCKED = 0 " +
            "ORDER BY B.B_DATE DESC", nativeQuery = true)
    Object[] boardDetail(@Param("bNum") int bNum);


    // B_RP 증가 쿼리 (수동으로 @Query로 정의)
    @Modifying
    @Transactional
    @Query("UPDATE BoardEntity b SET b.BRp = b.BRp + 1 WHERE b.BNum = :bnum")
    void incrementBRp(@Param("bnum") int BNum);  // Integer로 매핑

    @Query("SELECT b FROM BoardEntity b WHERE b.BNum IN :bNumList")
    List<BoardEntity> findByBNumIn(@Param("bNumList") List<Integer> bNumList);

    @Query("SELECT b FROM BoardEntity b WHERE b.UserId = :UserId")
    List<BoardEntity> findByUserId(@Param("UserId")String loginId);

    @Modifying
    @Transactional
    @Query("UPDATE BoardEntity b SET b.CategoryNum = 4 WHERE b.BNum = :bNum")
    void boardToHistory(@Param("bNum") int bNum);

    // searchBoardsByCategory (기존 코드와 동일)
    @Query(value = "SELECT B.B_NUM, B.B_TITLE, B.CNT_NUM, B.COUNT_PEOPLE, B.B_FILENAME, RD.DETAIL_NUM, B.B_START, B.B_END, RD.R_NUM, R.R_ADDR " +
            "FROM BOARD B " +
            "JOIN CATEGORY C ON B.CATEGORY_NUM = C.CATEGORY_NUM " +
            "JOIN REGION_DETAIL RD ON B.DETAIL_NUM = RD.DETAIL_NUM " +
            "JOIN USER_INFO U ON B.USER_ID = U.USER_ID " +
            "JOIN REGION R ON RD.R_NUM = R.R_NUM " +
            "LEFT JOIN TAG T ON B.T_NUM = T.T_NUM " +
            "WHERE B.CATEGORY_NUM = :categoryNum " + // 카테고리 번호 조건 추가
            "AND B.IS_BLOCKED = 0 " +
            "ORDER BY B.B_DATE DESC", nativeQuery = true)
    List<Object[]> searchBoardsByCategory(@Param("categoryNum") Integer categoryNum);
    @Transactional
    @Modifying
    @Query("DELETE FROM BoardEntity b WHERE b.BNum = :bnum")
    void deleteBybnum(@Param("bnum") int bnum);

    @Query(value = "SELECT B.B_NUM, B.B_TITLE, B.CNT_NUM, B.COUNT_PEOPLE, B.B_FILENAME, RD.DETAIL_NUM, B.B_START, B.B_END, RD.R_NUM, R.R_ADDR " +
            "FROM BOARD B " +
            "JOIN CATEGORY C ON B.CATEGORY_NUM = C.CATEGORY_NUM " +
            "JOIN REGION_DETAIL RD ON B.DETAIL_NUM = RD.DETAIL_NUM " +
            "JOIN USER_INFO U ON B.USER_ID = U.USER_ID " +
            "JOIN REGION R ON RD.R_NUM = R.R_NUM " +
            "LEFT JOIN TAG T ON B.T_NUM = T.T_NUM " +
            "WHERE B.CATEGORY_NUM = :categoryNum " +
            "AND ((:category = 'UserNickname' AND B.B_NICKNAME LIKE %:keyword%) " +
            "OR (:category = 'RevTitle' AND B.B_TITLE LIKE %:keyword%) " +
            "OR (:category = 'RevContent' AND B.B_CONTENT LIKE %:keyword%)) " +
            "AND B.IS_BLOCKED = 0 " +
            "ORDER BY B.B_DATE DESC", nativeQuery = true)
    List<Object[]> searchBoardsByKeyword(@Param("categoryNum") Integer categoryNum,
                                         @Param("category") String category,
                                         @Param("keyword") String keyword);
}



