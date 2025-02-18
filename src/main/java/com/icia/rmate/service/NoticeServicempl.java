package com.icia.rmate.service;

import com.icia.rmate.dao.NoticeRepository;
import com.icia.rmate.dto.NoticeDTO;
import com.icia.rmate.dto.NoticeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeServicempl implements NoticeService {

    private final NoticeRepository nrepo;
    private static final Logger log = LoggerFactory.getLogger(NoticeServicempl.class);

    // 공지사항 - 공지사항 작성시 Notices에서 저장
    @Override
    @Transactional
    public void saveNotice(NoticeDTO noticeDTO) {
        NoticeEntity noticeEntity = new NoticeEntity();
        try {
            // DTO에서 필요한 정보 복사
            noticeEntity.setAuthor(noticeDTO.getAuthor());
            noticeEntity.setContent(noticeDTO.getContent());
            noticeEntity.setTitle(noticeDTO.getTitle());
            // 현재 시간으로 createdDate 설정
            noticeEntity.setCreatedDate(LocalDateTime.now());
            log.info("saveNotice before save : " + noticeEntity.toString());

            nrepo.save(noticeEntity);
            log.info("saveNotice after save : " + noticeEntity.toString());
            noticeDTO.setId(noticeEntity.getId());

        } catch (Exception e) {
            log.error("공지사항 저장 중 오류 발생: " + e.getMessage(), e);
            throw e;
        }
    }

    // 공지사항 - 공지사항 저장 후 <th:>문에서 전체 공지사항 확인
    @Override
    public List<NoticeDTO> getAllNotices() {
        List<NoticeEntity> noticeEntities = nrepo.findAll();
        return noticeEntities.stream()
                .map(NoticeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public NoticeEntity getNoticeById(Long noticeId) {
        return nrepo.findById(noticeId).orElse(null);
    }
}