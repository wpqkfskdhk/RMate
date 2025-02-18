package com.icia.rmate.service;

import com.icia.rmate.dao.ApplyRepository;
import com.icia.rmate.dao.ReviewRepository;
import com.icia.rmate.dto.RevDTO;
import com.icia.rmate.dto.RevEntity;
//import com.icia.rmate.dto.SearchDTO;

import com.icia.rmate.dto.SearchDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository rrepo;
    private final ReviewRepository reviewRepository;
    private final ApplyRepository arepo;
    private ModelAndView mav;

    Path path = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/upload");

    private final HttpServletRequest request;

    private final
    HttpServletResponse response;

    private final HttpSession session;
    //리뷰 작성 메소드
    public ModelAndView RWrite(RevDTO rev) {
        System.out.println("[2] controller → service : " + rev);
        mav = new ModelAndView();

        // 파일 가져오기
        MultipartFile RFile = rev.getRevFile();
        String savePath = "";

        if(!RFile.isEmpty()){
            String uuid = UUID.randomUUID().toString().substring(0,8);
            String fileName = RFile.getOriginalFilename();
            String RFileName = uuid + "_" + fileName;

            rev.setRevFileName(RFileName);

            savePath = path + "\\" + RFileName;
        } else {
            rev.setRevFileName("default.jpg");
        }

        try {
            RevEntity entity = RevEntity.toEntity(rev);

            System.out.println("entity : " + entity);

            rrepo.save(entity);
            RFile.transferTo(new File(savePath));
        } catch (Exception e) {
            System.out.println("리뷰 등록 실패!");
        }




        arepo.saveRStatus(rev.getBNum(),rev.getUserId());
        System.out.println("리뷰 중복 쓰기 금지"+rev.getBNum()+rev.getUserId());

        mav.setViewName("redirect:/RList");

        return mav;
    }
    // 게시판 불러오는 메소드
    public List<RevDTO> revList() {
        List<RevDTO> dtoList = new ArrayList<>();

        // Repositories에서 내림차순 정렬된 엔티티 리스트를 가져옵니다.
        List<RevEntity> entityList = rrepo.findAllByOrderByREVNumDesc();

        // 엔티티 리스트를 DTO 리스트로 변환합니다.
        for (RevEntity entity : entityList) {
            dtoList.add(RevDTO.toDTO(entity));
        }

        // 변환된 DTO 리스트를 반환합니다.
        return dtoList;
    }
    //쿼리문 사용을 위한 메소드
    @Transactional // 추가
    public List<RevDTO> searchList(SearchDTO search) {
        List<RevDTO> dtoList = new ArrayList<>();
        List<RevEntity> entityList = new ArrayList<>();

        // category : UserNickname,RevTitle,RevContent
        if(search.getCategory().equals("UserNickname")){
            entityList =rrepo.findByUserNicknameContainingOrderByREVNumDesc(search.getKeyword());
            // SELECT * FROM Rev WHERE UserNickname LIKE '%keyword%' ORDER BY BNUM DESC
        } else if(search.getCategory().equals("RevTitle")) {
            entityList = rrepo.findByRevTitleContainingOrderByREVNumDesc(search.getKeyword());
        } else if(search.getCategory().equals("RevContent")) {
            entityList = rrepo.findByRevContentContainingOrderByREVNumDesc(search.getKeyword());
        }

        for(RevEntity entity : entityList){
            dtoList.add(RevDTO.toDTO(entity));
        }

        return dtoList;
    }
    //상세보기 메소드
    public ModelAndView RView(int REVNum) {
        System.out.println("[2] controller → service : " + REVNum);
        mav = new ModelAndView();

        // 로그인 아이디
        String loginId = (String)session.getAttribute("loginId");

//        // 로그인 여부 체크
//        if (loginId == null) {
//            request.setAttribute("alertMessage", "로그인 후 이용해주세요.");
//            mav.setViewName("redirect:/RList"); // 아이디가 없으면 회원가입 페이지로 리다이렉트
//            return mav;
//        }

        // 현재 쿠키를 담을 배열 생성
        Cookie[] cookies = request.getCookies();

        Cookie viewCookie = null;

        // 쿠키가 존재한다면
        if(cookies != null && cookies.length > 0){

            // 쿠키만큼 반복문을 실행 => cookie : cookies[i]
            for(Cookie cookie : cookies){

                // ex) 쿠키 이름이 "cookie_Geust_1" 이라면
                if(cookie.getName().equals("cookie_" + loginId + "_" + REVNum)){
                    System.out.println(cookie.getName() + "존재!");

                    // viewCookie에 "cookie_Geust_1" 을 담는다
                    viewCookie = cookie;
                }
            }
        }

        // 쿠키가 존재하지 않으면 새로운 쿠키를 만든다.
        if(viewCookie == null){
            String cookie = "cookie_" + loginId + "_" + REVNum;
            Cookie newCookie = new Cookie(cookie, cookie);

            newCookie.setMaxAge(60 * 60 * 1);
            response.addCookie(newCookie);

            System.out.println("새로운 쿠기 생성 : " + newCookie.getName());

            // 방법(1) 쿼리문을 작성해서 조회수 1증가
            rrepo.incrementBHit(REVNum);

            // 방법(2) 게시글 번호로 데이터 조회 후 조회수 1증가 후 다시 저장
            // Optional<revEntity> entity = brepo.findById(BNum);
            // entity.get().setBHit(entity.get().getBHit() + 1);
            // brepo.save(entity.get());

        }

        Optional<RevEntity> entity = rrepo.findById(REVNum);
        if(entity.isPresent()){
            RevDTO dto = RevDTO.toDTO(entity.get());
            mav.addObject("view", dto);
            mav.setViewName("review/reviewdetail");
        } else {
            mav.setViewName("redirect:/RList");
        }

        return mav;
    }
    //리뷰 신고 메소드
    public ModelAndView RModify(RevDTO rev){
        System.out.println("[2] controller → service : " + rev);
        mav = new ModelAndView();

        // 기존 파일 삭제하기
        if(rev.getRevFileName() != null){
            String delPath = path + "\\" + rev.getRevFileName();

            File delFile = new File(delPath);
            if(delFile.exists()){
                delFile.delete();

            }
        }

        // 파일 가져오기
        MultipartFile bFile = rev.getRevFile();
        String savePath = "";

        if(!bFile.isEmpty()){
            String uuid = UUID.randomUUID().toString().substring(0,8);
            String fileName = bFile.getOriginalFilename();
            String bFileName = uuid + "_" + fileName;

            rev.setRevFileName(bFileName);

            savePath = path + "\\" + bFileName;
        } else {
            rev.setRevFileName("default.jpg");
        }

        try {
            RevEntity entity = RevEntity.toEntity(rev);
            rrepo.save(entity);
            bFile.transferTo(new File(savePath));
            mav.setViewName("redirect:/RList");
        } catch (Exception e) {
            System.out.println("게시글 수정 실패!");
            mav.setViewName("redirect:/index");
        }

        return mav;
    }

    //리뷰 삭제 메소드
    public ModelAndView RDelete(RevDTO rev) {
        System.out.println("[2] controller → service : " +rev);
        mav = new ModelAndView();
        // 기존 파일 삭제하기
        if(rev.getRevFileName() != null){
            String delPath = path + "\\" + rev.getRevFileName();

            File delFile = new File(delPath);
            if(delFile.exists()){
                delFile.delete();

            }
        }
        rrepo.deleteById(rev.getREVNum());
        mav.setViewName("redirect:/RList");

        return mav;
    }
    //리뷰 신고 메소드
    @Transactional
    public String reportReview(int REVNum, String loginId, HttpServletRequest request, HttpServletResponse response) {
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
                if (cookie.getName().equals("cookie_" + loginId + "_" + REVNum)) {
                    viewCookie = cookie;
                    break;
                }
            }
        }

        // 쿠키가 존재하지 않으면 신고 처리
        if (viewCookie == null) {
            String cookieName = "cookie_" + loginId + "_" + REVNum;
            Cookie newCookie = new Cookie(cookieName, cookieName);
            newCookie.setMaxAge(60 * 60 * 24 * 365); // 1년 동안 유효
            response.addCookie(newCookie);

            // 신고 처리 (R_RP 증가)
            rrepo.incrementRRp(REVNum);

            return "신고가 정상적으로 처리되었습니다.";
        } else {
            return "이미 신고한 게시글입니다.";
        }
    } // 서비스

    public List<RevDTO> getMyReviews(String loginId) {
        System.out.println("userId : " + loginId);

        if (loginId == null) {
            System.out.println("userId가 null입니다.");
            return Collections.emptyList();
        }
        if (loginId.isEmpty()) {
            System.out.println("userId가 비어있습니다.");
            return Collections.emptyList();
        }
        try {
            List<RevEntity> myReviews = reviewRepository.findByUserId(loginId);
            if (myReviews.isEmpty()) {
                System.out.println("작성한 리뷰가 없습니다.");
                return Collections.emptyList();
            }
            for (RevEntity review : myReviews) {
                System.out.println("리뷰 번호 : " + review.getREVNum() + ", 게시글 번호 : " + review.getBNum());
            }
            return myReviews.stream()
                    .map(RevDTO::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<RevEntity> checkReviewExists(int bNum,String loginId) {
        return rrepo.existsByBNumAndUserId(bNum,loginId);
    }
}