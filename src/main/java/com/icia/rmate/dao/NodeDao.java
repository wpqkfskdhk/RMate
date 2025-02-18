package com.icia.rmate.dao;
import com.icia.rmate.dao.base.BaseDao;
import com.icia.rmate.dto.NodeDTO;
import jakarta.transaction.Transactional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Mapper
public interface NodeDao extends BaseDao<NodeDTO, Long> {
//    @Transactional
//    @Modifying
//    @Query("DELETE FROM NodeEntity n WHERE n.id = :nodeId")
//    void deleteById(@Param("nodeId") Long nodeId);

    void deleteById(@Param("nodeId") Long nodeId);
}

