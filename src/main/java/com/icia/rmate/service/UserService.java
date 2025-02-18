package com.icia.rmate.service;

import com.icia.rmate.dao.*;
import com.icia.rmate.dto.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    // 이메일 인증
    private final JavaMailSender mailSender;

    // 저장 경로
    Path path = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/profile");

    // 비밀번호 암호화
    private BCryptPasswordEncoder pwEnc = new BCryptPasswordEncoder();

    // 로그인
    private final HttpSession session;

    private ModelAndView mav;

    private final UserTermsRepository utrepo;



    @Autowired
    private UserInfoRepository urepo;
    @Autowired
    private BoardRepository brepo;
    @Autowired
    private CommentRepository crepo;
    @Autowired
    private ReviewRepository rrepo;
    @Autowired
    private ApplyRepository arepo;
    @Autowired
    private CourseRepository course;

    @Autowired
    private OrderRepository orepo;

    public String idCheck(String userId) {
        String result = "";
        Optional<UserInfoEntity> entity = urepo.findById(userId);


        if (entity.isPresent()) {
            result = "NO";
        } else {
            result = "OK";
        }

        return result;
    }


    public String emailCheck(String mEmail) {
        String uuid = null;

        // 인증번호
        uuid = UUID.randomUUID().toString().substring(0, 8);

        // 이메일 발송
        MimeMessage mail = mailSender.createMimeMessage();

        String message = "<h2>안녕하세요. 태우고 입니다.</h2>"
                + "<p>인증번호는 <b>" + uuid + "</b> 입니다.</p>";

        try {
            // 이메일 전송 준비
            mail.setSubject("태우고 인증번호");
            mail.setText(message, "UTF-8", "html");
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(mEmail));

            // 이메일 전송
            // mailSender.send(mail);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return uuid;
    }

    public ModelAndView mdJoin(UserInfoDTO user) {
        ModelAndView mav = new ModelAndView();

        // 사용자 아이디를 통해 기존 데이터를 찾아와서 수정하는 부분
        String userId = user.getUserId();
        UserInfoEntity existingUser = urepo.findById(userId).orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        // 기존 프로필 사진 삭제
        String oldFilePath = existingUser.getUserProfileName(); // 기존 파일 경로 가져오기
        if (!oldFilePath.equals("default.png")) {  // 기본 파일이 아닌 경우에만 삭제
            File oldFile = new File(path + "\\" + oldFilePath);
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }

        // (1) 파일 업로드 처리
        MultipartFile mProfile = user.getUserProfile();
        if (!mProfile.isEmpty()) {
            String uuid = UUID.randomUUID().toString().substring(0, 8);  // UUID를 통해 고유 이름 생성
            String fileName = mProfile.getOriginalFilename();  // 원본 파일 이름
            String mProfileName = uuid + "_" + fileName;  // 새로운 파일 이름 생성

            user.setUserProfileName(mProfileName);

            String savePath = path + "\\" + mProfileName;
            System.out.println("savePath : " + savePath);   // 확인하고 주석처리

            try {
                mProfile.transferTo(new File(savePath));  // 파일을 지정된 경로에 저장
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            user.setUserProfileName(existingUser.getUserProfileName());  // 새 파일이 없으면 기존 프로필 사진 유지
        }

        // (2) 비밀번호 암호화
        if (user.getUserPw() != null && !user.getUserPw().isEmpty()) {
            user.setUserPw(pwEnc.encode(user.getUserPw()));  // 비밀번호 암호화
        } else {
            user.setUserPw(existingUser.getUserPw());  // 비밀번호가 없으면 기존 비밀번호 유지
        }

        // (3) DTO에서 Entity로 변환하여 DB에 저장
        UserInfoEntity updatedEntity = UserInfoEntity.toEntity(user);  // DTO를 엔티티로 변환
        try {
            urepo.save(updatedEntity);  // 사용자 정보 업데이트

            // 세션 종료 자동 로그아웃

            session.invalidate();
            mav.setViewName("redirect:/index");  // 수정 후 리다이렉트
        } catch (Exception e) {
            mav.setViewName("redirect:/");  // 에러 발생 시 다른 페이지로 리다이렉트
            throw new RuntimeException(e);
        }

        return mav;
    }

    public ModelAndView mJoin(UserInfoDTO user, List<Integer> TermsId) {
        mav = new ModelAndView();

        // (1) 파일 업로드 처리
        MultipartFile mProfile = user.getUserProfile();

        if (mProfile != null && !mProfile.isEmpty()) {
            String uuid = UUID.randomUUID().toString().substring(0, 8);
            String fileName = mProfile.getOriginalFilename();
            String mProfileName = uuid + "_" + fileName;

            user.setUserProfileName(mProfileName);

            String savePath = path + "\\" + mProfileName;
            System.out.println("savePath : " + savePath);

            try {
                mProfile.transferTo(new File(savePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            user.setUserProfileName("default.jpg");
        }

        // (2) 비밀번호 암호화 처리
        user.setUserPw(pwEnc.encode(user.getUserPw()));

        // (3) 사용자 정보를 엔티티로 변환하여 저장
        UserInfoEntity entity = UserInfoEntity.toEntity(user);
        System.out.println("엔티티 저장전 확인: " + entity);

        try {
            urepo.save(entity);  // 사용자 정보 DB에 저장

            // (4) 약관 동의 내역 저장
            for (Integer termsId : TermsId) {
                UserTermsDTO userTermsDTO = new UserTermsDTO();
                userTermsDTO.setUserId(user.getUserId());
                userTermsDTO.setTermsId(termsId);
                userTermsDTO.setAgreed(0);  // 동의 여부 0으로 설정 (동의)

                // 엔티티로 변환 후 저장
                UserTermsEntity userTermsEntity = UserTermsEntity.toEntity(userTermsDTO);
                utrepo.save(userTermsEntity);  // 약관 동의 내역 DB에 저장
            }

            mav.setViewName("redirect:/");  // 회원가입 완료 후 리다이렉트
        } catch (Exception e) {
            mav.setViewName("redirect:/");  // 예외 발생 시 리다이렉트
            throw new RuntimeException(e);
        }

        return mav;
    }

    // 로그인
    public ModelAndView mLogin(UserInfoDTO user, RedirectAttributes redirectAttributes) {
        mav = new ModelAndView();
        System.out.println("아이디 비밀번호 확인 :" + user);

        // 로그인 실패 메시지 초기화
        session.removeAttribute("loginError"); // 이전에 저장된 오류 메시지 삭제

        // (1) 아이디가 존재하는지 확인
        Optional<UserInfoEntity> entity = urepo.findById(user.getUserId());
        if (entity.isPresent()) {
            // (1-1) 해당 계정이 블락된 상태인지 확인
            if (entity.get().isBlocked()) {
                // 계정이 블락된 경우
                System.out.println("해당 계정은 블락된 상태입니다.");

                session.setAttribute("error", "해당계정은 블락된 상태입니다.");

                // 리다이렉트
                mav.setViewName("redirect:/");
                return mav;
            } else {
                // (2) 비밀번호가 일치하는지 확인
                if (pwEnc.matches(user.getUserPw(), entity.get().getUserPw())) {
                    // 로그인 성공 처리
                    UserInfoDTO login = UserInfoDTO.toDTO(entity.get());

                    // 관리자 여부 확인 추가
                    if (login.getUserId().equals("ADMIN")) {
                        // 관리자 로그인 성공
                        session.setAttribute("loginId", login.getUserId());
                        session.setAttribute("loginName", login.getUserName());
                        session.setAttribute("adminName", login.getUserId()); // 관리자 이름도 세션에 추가

                        // 관리자 페이지로 리디렉션
                        mav.setViewName("admin");
                        return mav;
                    }

                    // 일반 사용자 로그인 성공
                    session.setAttribute("loginId", login.getUserId());
                    session.setAttribute("loginPw", login.getUserPw());
                    session.setAttribute("loginGender", login.getUserGender());
                    session.setAttribute("loginBirth", login.getUserBirth());
                    session.setAttribute("loginEmail", login.getUserEmail());
                    session.setAttribute("loginPhone", login.getUserPhone());
                    session.setAttribute("loginAddress", login.getUserAddress());
                    session.setAttribute("loginProfile", login.getUserProfileName());
                    session.setAttribute("loginName", login.getUserName());
                    session.setAttribute("loginNickname", login.getUserNickname());
                    session.setAttribute("loginIsBlocked", login.isBlocked());

                    // 로그인 성공 후 리디렉션
                    mav.setViewName("redirect:/");
                    return mav;
                } else {
                    // 비밀번호 불일치
                    System.out.println("비밀번호가 일치하지 않습니다.");
                    session.setAttribute("loginError", "password");  // 세션에 비밀번호 불일치 메시지 저장
                }
            }
        } else {
            // 아이디 존재하지 않음
            System.out.println("아이디 존재하지 않습니다.");
            session.setAttribute("loginError", "notfound");  // 세션에 아이디 없음 메시지 저장
        }

        // 로그인 실패 시에도 항상 리디렉션
        mav.setViewName("redirect:/");
        return mav;
    }

    @Transactional
    public void deleteUserAndRelatedData(String loginId) {
        UserInfoEntity user = urepo.findById(loginId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with loginId: " + loginId));

        // 1. REPORT 삭제 (필요한 경우) - COMMENTS와 BOARD에 의존성

        // 2. COMMENTS 삭제 (BOARD가 삭제되기 전에 삭제되어야 함)
        List<CommentEntity> comments = crepo.findByUserId(loginId);
        crepo.deleteAll(comments);

        // 3. APPLY 삭제 (BOARD가 삭제되기 전에 삭제되어야 함)
        List<ApplyEntity> applies = arepo.findByUserId(loginId);
        arepo.deleteAll(applies);

        // 4. REV 삭제 (COMMENTS와 BOARD가 삭제된 후에 삭제 가능)
        List<RevEntity> reviews = rrepo.findByUserId(loginId);
        rrepo.deleteAll(reviews);


        // 5. Course 삭제 (Board 삭제 전)
        List<BoardEntity> boards = brepo.findByUserId(loginId);
        for (BoardEntity board : boards) {
            List<CourseEntity> courses = course.findByBNum(board.getBNum());
            course.deleteAll(courses);

        }


        // 7. Orders 삭제 (UserInfo 삭제 전) - 조건 검사 제거
        List<OrderEntity> orders = orepo.findByUserId(loginId);
        orepo.deleteAll(orders);

        List<UserTermsEntity> terms = utrepo.findByUserId(loginId);
        utrepo.deleteAll(terms);
        //서비스 안에서 sout 쓰지마세요 재귀호출됩니다.
        // 6. Board 삭제
        brepo.deleteAll(boards);

        // 10. USER_INFO 삭제
        urepo.delete(user);

    }

    // UserService
    public String nicknameCheck(String userNickname) {
        System.out.println("nicknameCheck called with: " + userNickname); // 로깅
        String result = "";
        Optional<UserInfoEntity> entity = urepo.findByUserNickname(userNickname);
        System.out.println("findByUserNickname result: " + entity); // 로깅

        if (entity.isPresent()) {
            result = "NO"; // 닉네임 중복
        } else {
            result = "OK"; // 닉네임 사용 가능
        }

        return result;
    }
}
