package com.icia.rmate.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserTermsId implements Serializable {

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "TERMS_ID")
    private int termsId;

    public UserTermsId() {}

    public UserTermsId(String userId, int termsId) {
        this.userId = userId;
        this.termsId = termsId;
    }

    // equals()와 hashCode() 구현 필요
    // 복합 키 비교: JPA에서 복합 키를 사용한 엔티티 객체들을 비교하려면 equals()가 필수
    // 정확한 객체 동등성 판단: equals()를 사용하여 두 객체가 실제 데이터가 동일한지 판단 가능
    // 컬렉션에서 객체 관리: HashSet, HashMap과 같은 컬렉션에서 객체를 비교하고 관리하려면 equals()가 필요
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTermsId that = (UserTermsId) o;
        return termsId == that.termsId && userId.equals(that.userId);
    }

    // 객체 비교와 효율적인 검색을 위해 필수적
    // hashCode()는 컬렉션에서 객체를 효율적으로 관리하고 검색하기 위해 필요
    // equals()와 함께 사용되어 객체의 동등성을 비교
    // 복합 키가 포함된 엔티티에서는 hashCode()가 제대로 구현되지 않으면 JPA가 객체를 올바르게 비교하거나 관리할 수 없음
    @Override
    public int hashCode() {
        return 31 * userId.hashCode() + termsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//    public int getTermsId() {
//        return termsId;
//    }
//
//    public void setTermsId(int termsId) {
//        this.termsId = termsId;
//    }
}
