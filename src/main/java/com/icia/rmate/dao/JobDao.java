package com.icia.rmate.dao;
import org.apache.ibatis.annotations.Mapper;
import com.icia.rmate.dao.base.BaseDao;
import com.icia.rmate.dto.Job;
@Mapper
public interface JobDao extends BaseDao<Job, Long> {
}
