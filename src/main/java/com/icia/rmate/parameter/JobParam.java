package com.icia.rmate.parameter;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Setter
@Getter
public class JobParam extends SearchParameter {
  private Long id;//업무id
  private Long memberId;//회원id
  private Long nodeId;//노드id
  private String jobDate;//업무일자(yyyymmdd)
  private String jobStartTime;//업무시작시각(hhmm)
  private String jobEndTime;//업무종료시각(hhmm)
  private Integer jobDuration;//업무작업시간(분단위)
  private Long itemQty;//물건수량
  private String itemQtyUnit;//물건수량단위
  private String requireVehicleType;//필요차종
  private String memo;//메모
  private Date regDt;//등록일시
  private Date modDt;//수정일시

}
