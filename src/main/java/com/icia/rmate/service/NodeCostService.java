package com.icia.rmate.service;

import com.icia.rmate.dao.NodeCostDao;
import com.icia.rmate.dto.NodeCost;
import com.icia.rmate.service.base.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class NodeCostService extends BaseService<NodeCost, Long, NodeCostDao> {
    public void saveNodeCost(NodeCost nodeCost) {
    }
}
