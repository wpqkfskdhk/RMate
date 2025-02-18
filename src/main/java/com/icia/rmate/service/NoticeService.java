package com.icia.rmate.service;

import com.icia.rmate.dto.NoticeDTO;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoticeService {
    void saveNotice(NoticeDTO noticeDTO);
    List<NoticeDTO> getAllNotices();
}