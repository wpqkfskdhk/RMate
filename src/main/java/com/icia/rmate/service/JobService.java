package com.icia.rmate.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icia.rmate.dao.JobDao;
import com.icia.rmate.dto.Job;
import com.icia.rmate.service.base.BaseService;
@Transactional(readOnly = true)
@Service
public class JobService extends BaseService<Job, Long, JobDao> {
}
