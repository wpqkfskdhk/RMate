package com.icia.rmate.util;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.icia.rmate.util.kakao.KakaoDirections;
import com.icia.rmate.util.kakao.Document;
import com.icia.rmate.util.kakao.KakaoAddress;
import com.icia.rmate.util.kakao.KakaoDirections;
import com.icia.rmate.util.kakao.KakaoDirections.Route.Section.Road;
import lombok.Getter;
import lombok.Setter;

public class KakaoApiUtil {
  private static final String REST_API_KEY = "e648255bea576865f2dd35278781b934";

  /**
   * 키워드 장소 검색
   *
   * @return 좌표
   * @paramaddress주소
   */
  public static List<Point> getPointByKeyword(String keyword, Point center) throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    String url = "https://dapi.kakao.com/v2/local/search/keyword.json";
    url += "?query=" + URLEncoder.encode(keyword, "UTF-8");
    /*url += "&category_group_code=AT4";*/
    url += "&x=" + center.getX();
    url += "&y=" + center.getY();
//    url += "&sort=distance"; // 거리순 정렬
    HttpRequest request = HttpRequest.newBuilder()//
            .header("Authorization", "KakaoAK " + REST_API_KEY)//
            .uri(URI.create(url))//
            .GET()//
            .build();

    System.out.println(request.headers());

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    String responseBody = response.body();
    System.out.println(responseBody);

    KakaoAddress kakaoAddress = new ObjectMapper().readValue(responseBody, KakaoAddress.class);
    List<Document> documents = kakaoAddress.getDocuments();
    if (documents.isEmpty()) {
      return null;
    }

    List<Point> pointList = new ArrayList<>();
    for (Document document : documents) {
      Point point = new Point(document.getX(), document.getY());
      point.setName(document.getPlaceName());
      point.setPhone(document.getPhone() == null ? "전화번호가 없습니다" : document.getPhone());
//      point.setAdddress(document.getRoadAddressName());
      point.setId(document.getId());
      // 주소 설정 부분 수정: roadAddress와 jibunAddress 필드에 각각 저장
      point.setRoadAddress(document.getRoadAddressName()); // 도로명 주소 설정
      point.setJibunAddress(document.getAddressName());   // 지번 주소 설정;
      pointList.add(point);
    }
    return pointList;
  }

  /**
   * 자동차 길찾기
   *
   * @param from 출발지
   * @param to   도착지
   * @return 이동 좌표 목록
   * @throws InterruptedException
   * @throws IOException
   */
  public static List<Point> getVehiclePaths(Point from, Point to) throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    String url = "https://apis-navi.kakaomobility.com/v1/directions";
    url += "?origin=" + from.getX() + "," + from.getY();
    url += "&destination=" + to.getX() + "," + to.getY();
    HttpRequest request = HttpRequest.newBuilder()//
            .header("Authorization", "KakaoAK " + REST_API_KEY)//
            .header("Content-Type", "application/json")//
            .uri(URI.create(url))//
            .GET()//
            .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    String responseBody = response.body();

    List<Point> pointList = new ArrayList<Point>();

    KakaoDirections kakaoDirections = new ObjectMapper().readValue(responseBody, KakaoDirections.class);
    List<Road> roads = kakaoDirections.getRoutes().get(0).getSections().get(0).getRoads();
    for (Road road : roads) {
      List<Double> vertexes = road.getVertexes();
      for (int i = 0; i < vertexes.size(); i++) {
        pointList.add(new Point(vertexes.get(i), vertexes.get(++i)));
      }
    }

    return pointList;

  }

  /**
   * 자동차 길찾기
   *
   * @param from 출발지
   * @param to   도착지
   * @return 길찾기 결과 정보 KakaoDirections
   * @throws InterruptedException
   * @throws IOException
   */
  public static KakaoDirections getKakaoDirections(Point from, Point to) throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    String url = "https://apis-navi.kakaomobility.com/v1/directions";
    url += "?origin=" + from.getX() + "," + from.getY();
    url += "&destination=" + to.getX() + "," + to.getY();
    HttpRequest request = HttpRequest.newBuilder()//
            .header("Authorization", "KakaoAK " + REST_API_KEY)//
            .header("Content-Type", "application/json")//
            .uri(URI.create(url))//
            .GET()//
            .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    String responseBody = response.body();
    System.out.println(responseBody);

    KakaoDirections kakaoDirections = new ObjectMapper().readValue(responseBody, KakaoDirections.class);

    return kakaoDirections;
  }

  /**
   * 주소 -> 좌표 변환
   *
   * @param address 주소
   * @return 좌표
   */
  public static Point getPointByAddress(String address) throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    String url = "https://dapi.kakao.com/v2/local/search/address.json";
    url += "?query=" + URLEncoder.encode(address, "UTF-8");
    HttpRequest request = HttpRequest.newBuilder()//
            .header("Authorization", "KakaoAK " + REST_API_KEY)//
            .uri(URI.create(url))//
            .GET()//
            .build();

    System.out.println(request.headers());

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    String responseBody = response.body();
    System.out.println(responseBody);

    KakaoAddress kakaoAddress = new ObjectMapper().readValue(responseBody, KakaoAddress.class);
    List<Document> documents = kakaoAddress.getDocuments();
    if (documents.isEmpty()) {
      return null;
    }
    Document document = documents.get(0);
    return new Point(document.getX(), document.getY());
  }

  @Setter
  @Getter
  public static class Point {
    private Double x;
    private Double y;
    @JsonIgnore
    private String name;
    @JsonIgnore
    private String phone;
    @JsonIgnore
    private String roadAddress; // 도로명 주소 필드
    @JsonIgnore
    private String jibunAddress; // 지번 주소 필드
    @JsonIgnore
    private String id;

    public Point() {

    }

    public Point(Double x, Double y) {
      this.x = x;
      this.y = y;
    }
  }
}
