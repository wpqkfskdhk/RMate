package com.icia.rmate.dto;
import java.util.Date;
/**
 * 노드  Class
 */
public class Node {
    private Long id;//노드id
    private String name;
    private String address;
    private String phone;
    private Double x;//경도
    private Double y;//위도
    private Date regDt;//등록일시
    private Date modDt;//수정일시
   /**
    * 노드id 조회
    * @return id
    */
    public Long getId() {
        return this.id;
    }
   /**
    * 노드id 설정
    * @return id
    */
    public void setId(Long id) {
        this.id = id;
    }
   /**
    *  조회
    * @return name
    */
    public String getName() {
        return this.name;
    }
   /**
    *  설정
    * @return name
    */
    public void setName(String name) {
        this.name = name;
    }
   /**
    *  조회
    * @return address
    */
    public String getAddress() {
        return this.address;
    }
   /**
    *  설정
    * @return address
    */
    public void setAddress(String address) {
        this.address = address;
    }
   /**
    *  조회
    * @return phone
    */
    public String getPhone() {
        return this.phone;
    }
   /**
    *  설정
    * @return phone
    */
    public void setPhone(String phone) {
        this.phone = phone;
    }
   /**
    * 경도 조회
    * @return x
    */
    public Double getX() {
        return this.x;
    }
   /**
    * 경도 설정
    * @return x
    */
    public void setX(Double x) {
        this.x = x;
    }
   /**
    * 위도 조회
    * @return y
    */
    public Double getY() {
        return this.y;
    }
   /**
    * 위도 설정
    * @return y
    */
    public void setY(Double y) {
        this.y = y;
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