package com.icia.rmate.dto;
import java.util.Date;
/**
 * 노드비용  Class
 */
public class NodeCost {
    private Long id;//노드비용id
    private Long startNodeId;//시작노드id
    private Long endNodeId;//종료노드id
    private Long distanceMeter;//이동거리(미터)
    private Long durationSecond;//이동시간(초)
    private Integer tollFare;//통행 요금(톨게이트)
    private Integer taxiFare;//택시 요금(지자체별, 심야, 시경계, 복합, 콜비 감안)
    private Integer fuelPrice;//해당 시점의 전국 평균 유류비와 연비를 감안한 유류비
    private String pathJson;//이동경로json [[x,y],[x,y]]
    private Date regDt;//등록일시
    private Date modDt;//수정일시
    private int CourseNum;   // 코스번호(FK)

   /**
    * 노드비용id 조회
    * @return id
    */
    public Long getId() {
        return this.id;
    }
   /**
    * 노드비용id 설정
    * @return id
    */
    public void setId(Long id) {
        this.id = id;
    }
   /**
    * 시작노드id 조회
    * @return startNodeId
    */
    public Long getStartNodeId() {
        return this.startNodeId;
    }
   /**
    * 시작노드id 설정
    * @return startNodeId
    */
    public void setStartNodeId(Long startNodeId) {
        this.startNodeId = startNodeId;
    }
   /**
    * 종료노드id 조회
    * @return endNodeId
    */
    public Long getEndNodeId() {
        return this.endNodeId;
    }
   /**
    * 종료노드id 설정
    * @return endNodeId
    */
    public void setEndNodeId(Long endNodeId) {
        this.endNodeId = endNodeId;
    }
   /**
    * 이동거리(미터) 조회
    * @return distanceMeter
    */
    public Long getDistanceMeter() {
        return this.distanceMeter;
    }
   /**
    * 이동거리(미터) 설정
    * @return distanceMeter
    */
    public void setDistanceMeter(Long distanceMeter) {
        this.distanceMeter = distanceMeter;
    }
   /**
    * 이동시간(초) 조회
    * @return durationSecond
    */
    public Long getDurationSecond() {
        return this.durationSecond;
    }
   /**
    * 이동시간(초) 설정
    * @return durationSecond
    */
    public void setDurationSecond(Long durationSecond) {
        this.durationSecond = durationSecond;
    }
   /**
    * 통행 요금(톨게이트) 조회
    * @return tollFare
    */
    public Integer getTollFare() {
        return this.tollFare;
    }
   /**
    * 통행 요금(톨게이트) 설정
    * @return tollFare
    */
    public void setTollFare(Integer tollFare) {
        this.tollFare = tollFare;
    }
   /**
    * 택시 요금(지자체별, 심야, 시경계, 복합, 콜비 감안) 조회
    * @return taxiFare
    */
    public Integer getTaxiFare() {
        return this.taxiFare;
    }
   /**
    * 택시 요금(지자체별, 심야, 시경계, 복합, 콜비 감안) 설정
    * @return taxiFare
    */
    public void setTaxiFare(Integer taxiFare) {
        this.taxiFare = taxiFare;
    }
   /**
    * 해당 시점의 전국 평균 유류비와 연비를 감안한 유류비 조회
    * @return fuelPrice
    */
    public Integer getFuelPrice() {
        return this.fuelPrice;
    }
   /**
    * 해당 시점의 전국 평균 유류비와 연비를 감안한 유류비 설정
    * @return fuelPrice
    */
    public void setFuelPrice(Integer fuelPrice) {
        this.fuelPrice = fuelPrice;
    }
   /**
    * 이동경로json [[x,y],[x,y]] 조회
    * @return pathJson
    */
    public String getPathJson() {
        return this.pathJson;
    }
   /**
    * 이동경로json [[x,y],[x,y]] 설정
    * @return pathJson
    */
    public void setPathJson(String pathJson) {
        this.pathJson = pathJson;
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
    /**
     * 코스번호 조회
     * @return CourseNum
     */
    public int getCourseNum() {
        return CourseNum;
    }
    /**
     * 코스번호 설정
     * @return CourseNum
     */
    public void setCourseNum(int courseNum) {
        CourseNum = courseNum;
    }
}