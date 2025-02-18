package com.icia.rmate.service;

import com.icia.rmate.dao.ApplyRepository;
import com.icia.rmate.dao.BoardRepository;
import com.icia.rmate.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.servlet.http.Cookie;
@Service
@RequiredArgsConstructor
public class BoardService {

    private  final BoardRepository brepo;
    public ModelAndView reportBoard;
    private final HttpSession session;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    @Autowired
    private ApplyRepository arepo;

    // 저장 경로
    Path path = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/boardUpload");

    private static ModelAndView mav;
    @Autowired
    private ApplyRepository applyRepository;

    public List<BoardDTO> getMyBoards() {
        String userId = (String) session.getAttribute("loginId");
        System.out.println("userId : " + userId);

        if (userId == null) {
            System.out.println("userId가 null입니다.");
            return null;
        }
        if (userId.isEmpty()) {
            System.out.println("userId가 비어있습니다.");
            return null;
        }
        try{
            List<BoardEntity> result = brepo.findByUserIdQuery(userId);
            if (result == null) {
                System.out.println("findByUserId 결과가 null 입니다.");
                return null;
            }
            // System.out.println(result); // toString() 호출 제거
            for(BoardEntity board : result){
                System.out.println("게시글 번호 :"+board.getBNum() + ", 게시글 제목 :"+board.getBTitle()); // 필요한 정보만 로그로 출력
            }

            return result.stream()
                    .map(BoardDTO::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Transactional
    public String reportBoard(int BNum, String loginId, HttpServletRequest request, HttpServletResponse response) {
        // 로그인 아이디 없으면 "Guest"로 설정
        if (loginId == null) {
            loginId = "Guest";
        }

        // 현재 쿠키를 담을 배열 생성
        Cookie[] cookies = request.getCookies();
        Cookie viewCookie = null;

        // 쿠키가 존재한다면
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cookie_" + loginId + "_" + BNum)) {
                    viewCookie = cookie;
                    break;
                }
            }
        }

        // 쿠키가 존재하지 않으면 신고 처리
        if (viewCookie == null) {
            String cookieName = "cookie_" + loginId + "_" + BNum;
            Cookie newCookie = new Cookie(cookieName, cookieName);
            newCookie.setMaxAge(60 * 60 * 24 * 365); // 1년 동안 유효
            response.addCookie(newCookie);

            // 신고 처리 (B_RP 증가)
            brepo.incrementBRp(BNum);

            return "신고가 정상적으로 처리되었습니다.";
        } else {
            return "이미 신고한 게시글입니다.";
        }
    }
    // boardList (categoryType 값에 따라 분기)
    public List<Object[]> boardList(@ModelAttribute SearchOptionDTO searchOptionDTO) {
        // SearchOptionDTO에서 값 추출
        Integer detailNum = searchOptionDTO.getDetailNum();
        Integer memberCount = searchOptionDTO.getMateType();
        Integer categoryType = searchOptionDTO.getCategoryType();
        String keyword = searchOptionDTO.getKeyword();
        Integer tNum = searchOptionDTO.getTNum();

        // categoryType 값에 따라 분기
        if (categoryType != null) {
            return brepo.searchBoardsByCategory(categoryType);
        } else {
            return brepo.searchBoardsWithJoin(detailNum, memberCount, categoryType, keyword, tNum);
        }
    }

    // boardListByCategory (기존 코드와 동일)
    public List<Object[]> boardListByCategory(Integer categoryNum) {
        return brepo.searchBoardsByCategory(categoryNum);
    }


    public List<Object[]> boardList2() {

        return brepo.Board2();
    }

    public List<Object[]> boardList3() {

        return brepo.Board3();
    }




    public ModelAndView boardForm(BoardDTO board) {

        mav = new ModelAndView();
        MultipartFile bProfile = board.getBFile();


        if (bProfile != null && !bProfile.isEmpty()) {

            String uuid = UUID.randomUUID().toString().substring(0,8);
            String fileName = bProfile.getOriginalFilename();
            String newFileName = uuid + "_" + fileName;

            // 파일 저장 경로 설정

            board.setBFileName(newFileName);
            String savePath = path + "\\" + newFileName;
            try {
                bProfile.transferTo(new File(savePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }else if(board.getBFileName() == null){
            board.setBFileName("default.png"); // 기본 이미지
        }

        BoardEntity entity = BoardEntity.toEntity(board);
        System.out.println("엔티티 저장전 확인"+entity);
        // (4) db저장
        try {
//            // 게시글 관련 데이터 추출
//            Integer categoryNum = board.getCategoryNum(); // 카테고리 번호
//            String userId = board.getUserId();        // 사용자 ID
//            String bNickname = board.getBNickName();  // 게시글 작성자 닉네임
//            LocalDateTime bDate = board.getBDate();   // 게시글 작성 날짜
//            LocalDateTime bUpdate = board.getBUpdate(); // 게시글 수정 날짜
//            Integer cntNum = board.getCntNum();       // 회원 수
//            String bTitle = board.getBTitle();        // 게시글 제목
//            String bContent = board.getBContent();   // 게시글 내용
//            String bFileName = board.getBFileName();  // 파일 이름
//            Integer detailNum = board.getDetailNum(); // 상세 번호 (rdNum → detailNum으로 수정)
//            LocalDate bStart = board.getBStart();     // 시작일
//            LocalDate bEnd = board.getBEnd();         // 종료일
//            Integer bRp = board.getBRp();             // 게시글 추천수
//            String mType = board.getMType();          // 게시글 유형
//            Integer tNum = board.getTNum();           // 지역 번호
//
//            // saveBoard 메서드 호출 (순서 맞게 매핑)
//            brepo.saveBoard(
//                    categoryNum,       // 카테고리 번호
//                    userId,            // 사용자 ID
//                    bNickname,         // 게시글 작성자 닉네임
//                    /*bDate,             // 게시글 작성 날짜
//                    bUpdate,           // 게시글 수정 날짜*/
//                    cntNum,            // 회원 수
//                    bTitle,            // 게시글 제목
//                    bContent,          // 게시글 내용
//                    bFileName,         // 게시글 파일명
//                    detailNum,         // 상세 번호 (rdNum → detailNum)
//                    bStart,            // 시작일
//                    bEnd,              // 종료일
//                    mType,             // 게시글 유형
//                    tNum               // 태그
//            );

// Tag 처리
            String tagName = board.getTagName();
            if (tagName != null && !tagName.isEmpty()) {
                // 쉼표로 구분된 태그 목록을 저장
                board.setTagName(tagName.trim());  // 중복 없이 저장할 경우 추가 처리 가능
            }


            BoardEntity savedEntity = brepo.save(entity);

            // Apply 테이블에 정보 저장 로직 추가
            if(session != null && session.getAttribute("loginId") != null) {
                String loginId = (String) session.getAttribute("loginId");
                int bNum = savedEntity.getBNum();

                // Apply 테이블에서 bNum과 userId로 조회
                ApplyEntity existingApply = arepo.findByBNumAndUserId(bNum, loginId);

                // 조회 결과가 없으면 (existingApply == null) ApplyEntity를 새로 저장
                if (existingApply == null) {
                    ApplyDTO applyDTO = new ApplyDTO();
                    applyDTO.setBNum(bNum); // 저장된 게시글 번호
                    applyDTO.setCategoryNum(savedEntity.getCategoryNum());
                    applyDTO.setUserId(loginId);// 로그인된 사용자 ID
                    applyDTO.setUserGender((String)session.getAttribute("loginGender"));// 로그인된 사용자 성별
                    applyDTO.setUserNickName((String)session.getAttribute("loginNickname"));// 로그인된 사용자 닉네임
                    applyDTO.setUserAddress((String)session.getAttribute("loginAddress"));
                    applyDTO.setAStatus(1); // 기본값 설정
                    applyDTO.setPStatus(1); // 기본값 설정
                    applyDTO.setRStatus(0); // 기본값 설정
                    applyDTO.setCntNum(savedEntity.getCntNum());
                    applyDTO.setBTitle(savedEntity.getBTitle());
                    applyDTO.setAContent("작성자는 작성할 수 없습니다."); // 작성자는 신청 불가 메시지 설정
                    // 신청 내용 AContent는 일단 null로 설정
                    ApplyEntity applyEntity = ApplyEntity.toEntity(applyDTO); // DTO를 Entity로 변환
                    arepo.save(applyEntity);
                } else {
                    System.out.println("ApplyEntity with bNum: " + bNum + " and userId: " + loginId + " already exists. Skipping save.");
                }
            }





            mav.setViewName("redirect:/");
        } catch (Exception e){
            mav.setViewName("redirect:/");
            throw new RuntimeException(e);
        }

        return mav;
    }


    public String boardToHistory(int bNum) {

        brepo.boardToHistory(bNum);
        // 완료 메시지 반환
        return "즐거운 여행 되셧나요? 리뷰를 작성해보세요!";
    }


    public int getCategoryNum(int bNum) {
        BoardEntity boardEntity = brepo.findById(bNum)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        return boardEntity.getCategoryNum();
    }
    public List<Object[]> searchList(Integer categoryNum, String category, String keyword) {
        return brepo.searchBoardsByKeyword(categoryNum, category, keyword);
    }
}
