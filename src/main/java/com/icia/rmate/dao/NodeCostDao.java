package com.icia.rmate.dao;

import com.icia.rmate.dao.base.BaseDao;
import com.icia.rmate.dto.NodeCost;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NodeCostDao extends BaseDao<NodeCost, Long> {
}
