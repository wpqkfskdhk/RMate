package com.icia.rmate.parameter;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Setter
@Getter
public class NodeParam extends SearchParameter {
  private Long id;//노드id
  private String name;
  private String address;
  private String phone;
  private Double x;//경도
  private Double y;//위도
  private Date regDt;//등록일시
  private Date modDt;//수정일시

}
