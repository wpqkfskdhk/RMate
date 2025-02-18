package com.icia.rmate.dao.base;

import com.icia.rmate.parameter.SearchParameter;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
public interface BaseDao<T, ID extends Serializable> {
  default List<T> selectList() {
    return selectList(null);
  }
  public List<T> selectList(SearchParameter searchParameter);
  public T selectOne(@Param("id") final ID id);
  public T selectOneByParam(SearchParameter searchParameter);
  public long selectListCount(SearchParameter searchParameter);
  default long selectListCount() {
    return selectListCount(null);
  }
  public int insert(@Param("entity") T t);
  public int update(@Param("entity") T t);
  /**
   * 물리적 삭제처리
   *
   * @paramid
   * @paramprettyLog
   * @return
   */
  public int delete(@Param("id") ID id);
  /**
   * 논리적 삭제 처리
   *
   * @paramid
   * @paramdelId
   * @paramprettyLog
   * @return
   */
  public int delete(@Param("id") ID id, @Param("delId") String delId);
}
