package com.icia.rmate.dto;
import java.util.Date;
/**
 * 업무  Class
 */
public class Job {
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
   /**
    * 업무id 조회
    * @return id
    */
    public Long getId() {
        return this.id;
    }
   /**
    * 업무id 설정
    * @return id
    */
    public void setId(Long id) {
        this.id = id;
    }
   /**
    * 회원id 조회
    * @return memberId
    */
    public Long getMemberId() {
        return this.memberId;
    }
   /**
    * 회원id 설정
    * @return memberId
    */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
   /**
    * 노드id 조회
    * @return nodeId
    */
    public Long getNodeId() {
        return this.nodeId;
    }
   /**
    * 노드id 설정
    * @return nodeId
    */
    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }
   /**
    * 업무일자(yyyymmdd) 조회
    * @return jobDate
    */
    public String getJobDate() {
        return this.jobDate;
    }
   /**
    * 업무일자(yyyymmdd) 설정
    * @return jobDate
    */
    public void setJobDate(String jobDate) {
        this.jobDate = jobDate;
    }
   /**
    * 업무시작시각(hhmm) 조회
    * @return jobStartTime
    */
    public String getJobStartTime() {
        return this.jobStartTime;
    }
   /**
    * 업무시작시각(hhmm) 설정
    * @return jobStartTime
    */
    public void setJobStartTime(String jobStartTime) {
        this.jobStartTime = jobStartTime;
    }
   /**
    * 업무종료시각(hhmm) 조회
    * @return jobEndTime
    */
    public String getJobEndTime() {
        return this.jobEndTime;
    }
   /**
    * 업무종료시각(hhmm) 설정
    * @return jobEndTime
    */
    public void setJobEndTime(String jobEndTime) {
        this.jobEndTime = jobEndTime;
    }
   /**
    * 업무작업시간(분단위) 조회
    * @return jobDuration
    */
    public Integer getJobDuration() {
        return this.jobDuration;
    }
   /**
    * 업무작업시간(분단위) 설정
    * @return jobDuration
    */
    public void setJobDuration(Integer jobDuration) {
        this.jobDuration = jobDuration;
    }
   /**
    * 물건수량 조회
    * @return itemQty
    */
    public Long getItemQty() {
        return this.itemQty;
    }
   /**
    * 물건수량 설정
    * @return itemQty
    */
    public void setItemQty(Long itemQty) {
        this.itemQty = itemQty;
    }
   /**
    * 물건수량단위 조회
    * @return itemQtyUnit
    */
    public String getItemQtyUnit() {
        return this.itemQtyUnit;
    }
   /**
    * 물건수량단위 설정
    * @return itemQtyUnit
    */
    public void setItemQtyUnit(String itemQtyUnit) {
        this.itemQtyUnit = itemQtyUnit;
    }
   /**
    * 필요차종 조회
    * @return requireVehicleType
    */
    public String getRequireVehicleType() {
        return this.requireVehicleType;
    }
   /**
    * 필요차종 설정
    * @return requireVehicleType
    */
    public void setRequireVehicleType(String requireVehicleType) {
        this.requireVehicleType = requireVehicleType;
    }
   /**
    * 메모 조회
    * @return memo
    */
    public String getMemo() {
        return this.memo;
    }
   /**
    * 메모 설정
    * @return memo
    */
    public void setMemo(String memo) {
        this.memo = memo;
    }
   /**
    * 등록일시 조회
    * @return regDt
    */
    public Date getRegDt() {
        return this.regDt;
    }
   /**
    * 등록일시 설정
    * @return regDt
    */
    public void setRegDt(Date regDt) {
        this.regDt = regDt;
    }
   /**
    * 수정일시 조회
    * @return modDt
    */
    public Date getModDt() {
        return this.modDt;
    }
   /**
    * 수정일시 설정
    * @return modDt
    */
    public void setModDt(Date modDt) {
        this.modDt = modDt;
    }
}