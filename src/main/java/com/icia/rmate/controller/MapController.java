package com.icia.rmate.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icia.rmate.dao.CourseRepository;
import com.icia.rmate.dao.NodeDao;
import com.icia.rmate.dto.CourseDTO;
import com.icia.rmate.dto.CourseEntity;
import com.icia.rmate.dto.Node;
import com.icia.rmate.dto.NodeCost;
import com.icia.rmate.parameter.NodeCostParam;
import com.icia.rmate.service.NodeCostService;
import com.icia.rmate.service.NodeService;
import com.icia.rmate.util.JsonResult;
import com.icia.rmate.util.KakaoApiUtil;
import com.icia.rmate.util.KakaoApiUtil.Point;
import com.icia.rmate.util.kakao.KakaoDirections;
import com.icia.rmate.util.kakao.KakaoDirections.Route;
import com.icia.rmate.util.kakao.KakaoDirections.Route.Section;
import com.icia.rmate.util.kakao.KakaoDirections.Route.Section.Road;
import com.icia.rmate.util.kakao.KakaoDirections.Route.Summary;
import com.icia.rmate.util.kakao.KakaoDirections.Route.Summary.Fare;
import com.icia.rmate.vrp.VrpResult;
import com.icia.rmate.vrp.VrpService;
import com.icia.rmate.vrp.VrpVehicleRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MapController {

  @Autowired
  private NodeService nodeService;
  @Autowired
  private NodeCostService nodeCostService;
  @Autowired
  private CourseRepository course;
    @Autowired
    private NodeDao nodeDao;

  @GetMapping("/map")
  public String getMain() {
    return "map";
  }


  @GetMapping("/cpoi")
  @ResponseBody
  public JsonResult processCpoi(@RequestParam int bNum) throws IOException, InterruptedException {
    System.out.println(bNum+"bnum 확인 ");
    List<CourseEntity> courseList = course.getCourseListByBNum(bNum);

    List<Point> pointList = new ArrayList<>();
    if (courseList != null) {
      for (CourseEntity course : courseList) {
        Point point = new Point();
        point.setId(String.valueOf(course.getId()));
        point.setName(course.getName());
        point.setPhone(course.getPhone());
        point.setRoadAddress(course.getAddress());
        point.setJibunAddress(course.getAddress());
        point.setX(course.getX());
        point.setY(course.getY());
        pointList.add(point);
      }
    }

    System.out.println(pointList+"포인트리스트확인 ");

    List<Node> nodeList = new ArrayList<>();
    for (Point point : pointList) {
      Node node = nodeService.getOne(Long.valueOf(point.getId()));
      if (node == null) {
        node = new Node();
        node.setId(Long.valueOf(point.getId()));// 노드id
        node.setName(point.getName());
        node.setPhone(point.getPhone());// 전화번호
        // 주소 설정 로직 수정
        if (point.getRoadAddress() != null && !point.getRoadAddress().isEmpty()) {
          node.setAddress(point.getRoadAddress()); // 도로명 주소 사용
        } else if (point.getJibunAddress() != null && !point.getJibunAddress().isEmpty()) {
          node.setAddress(point.getJibunAddress()); // 지번 주소 사용
        } else {
          node.setAddress(null); // 또는 null (상황에 맞게)
        }
        node.setX(point.getX());// 경도
        node.setY(point.getY());// 위도
        node.setRegDt(new Date());// 등록일시
        node.setModDt(new Date());// 수정일시
        nodeService.add(node);
      }
      nodeList.add(node);
    }

    int totalDistance = 0;
    int totalDuration = 0;
    List<Point> totalPathPointList = new ArrayList<>();
    for (int i = 1; i < nodeList.size(); i++) {
      Node prev = nodeList.get(i - 1);
      Node next = nodeList.get(i);

      NodeCost nodeCost = getNodeCost(prev, next);
      if (nodeCost == null) {
        System.out.println("노트코스트가없습니다.");
        continue;
      }
      totalDistance += nodeCost.getDistanceMeter();
      totalDuration += nodeCost.getDurationSecond();
      try {
        totalPathPointList.addAll(new ObjectMapper().readValue(nodeCost.getPathJson(), new TypeReference<List<Point>>() {
        }));
      }catch (Exception e){
        System.out.println("파싱에러" + e);
      }
    }
    JsonResult jsonResult = new JsonResult();
    jsonResult.addData("totalDistance", totalDistance);// 전체이동거리
    jsonResult.addData("totalDuration", totalDuration);// 전체이동시간
    jsonResult.addData("totalPathPointList", totalPathPointList);// 전체이동경로
    jsonResult.addData("nodeList", nodeList);// 방문지목록
    return jsonResult.success();
  }
  @GetMapping("/poi")
  @ResponseBody
  public JsonResult getPoi(@RequestParam String keyword, @RequestParam double x, @RequestParam double y) throws IOException, InterruptedException {
    Point center = new Point(x, y);// 중심좌표
    List<Point> pointList = KakaoApiUtil.getPointByKeyword(keyword, center);
    System.out.println(pointList+"포인트리스트확인 ");



    List<Node> nodeList = new ArrayList<>();
    for (Point point : pointList) {
      Node node = nodeService.getOne(Long.valueOf(point.getId()));
      if (node == null) {
        node = new Node();
        node.setId(Long.valueOf(point.getId()));// 노드id
        node.setName(point.getName());
          node.setPhone(point.getPhone());// 전화번호
        // 주소 설정 로직 수정
        if (point.getRoadAddress() != null && !point.getRoadAddress().isEmpty()) {
          node.setAddress(point.getRoadAddress()); // 도로명 주소 사용
        } else if (point.getJibunAddress() != null && !point.getJibunAddress().isEmpty()) {
          node.setAddress(point.getJibunAddress()); // 지번 주소 사용
        } else {
          node.setAddress(null); // 또는 null (상황에 맞게)
        }
        node.setX(point.getX());// 경도
        node.setY(point.getY());// 위도
        node.setRegDt(new Date());// 등록일시
        node.setModDt(new Date());// 수정일시
        nodeService.add(node);
      }
      nodeList.add(node);
    }



    int totalDistance = 0;
    int totalDuration = 0;
    List<Point> totalPathPointList = new ArrayList<>();
    for (int i = 1; i < nodeList.size(); i++) {
      Node prev = nodeList.get(i - 1);
      Node next = nodeList.get(i);

      NodeCost nodeCost = getNodeCost(prev, next);
      if (nodeCost == null) {

        System.out.println("노트코스트가없습니다.");
        continue;
      }

      totalDistance += nodeCost.getDistanceMeter();
      totalDuration += nodeCost.getDurationSecond();
      totalPathPointList.addAll(new ObjectMapper().readValue(nodeCost.getPathJson(), new TypeReference<List<Point>>() {
      }));
    }
    JsonResult jsonResult = new JsonResult();
    jsonResult.addData("totalDistance", totalDistance);// 전체이동거리
    jsonResult.addData("totalDuration", totalDuration);// 전체이동시간
    jsonResult.addData("totalPathPointList", totalPathPointList);// 전체이동경로
    jsonResult.addData("nodeList", nodeList);// 방문지목록
    return jsonResult;
  }




  private NodeCost getNodeCost(Node prev, Node next) throws IOException, InterruptedException {
    NodeCostParam nodeCostParam = new NodeCostParam();
    nodeCostParam.setStartNodeId(prev.getId());
    nodeCostParam.setEndNodeId(next.getId());
    NodeCost nodeCost = nodeCostService.getOneByParam(nodeCostParam);

    if (nodeCost == null) {
      KakaoDirections kakaoDirections = KakaoApiUtil.getKakaoDirections(new Point(prev.getX(), prev.getY()),
              new Point(next.getX(), next.getY()));
      List<Route> routes = kakaoDirections.getRoutes();
      Route route = routes.get(0);
      List<Point> pathPointList = new ArrayList<Point>();
      List<Section> sections = route.getSections();

      if (sections == null) {
        // {"trans_id":"018e3d7f7526771d9332cb717909be8f","routes":[{"result_code":104,"result_msg":"출발지와
        // 도착지가 5 m 이내로 설정된 경우 경로를 탐색할 수 없음"}]}
        pathPointList.add(new Point(prev.getX(), prev.getY()));
        pathPointList.add(new Point(next.getX(), next.getY()));
        nodeCost = new NodeCost();
        nodeCost.setStartNodeId(prev.getId());// 시작노드id
        nodeCost.setEndNodeId(next.getId());// 종료노드id
        nodeCost.setDistanceMeter(0l);// 이동거리(미터)
        nodeCost.setDurationSecond(0l);// 이동시간(초)
        nodeCost.setTollFare(0);// 통행 요금(톨게이트)
        nodeCost.setTaxiFare(0);// 택시 요금(지자체별, 심야, 시경계, 복합, 콜비 감안)
        nodeCost.setPathJson(new ObjectMapper().writeValueAsString(pathPointList));// 이동경로json [[x,y],[x,y]]
        nodeCost.setRegDt(new Date());// 등록일시
        nodeCost.setModDt(new Date());// 수정일시
        nodeCostService.saveNodeCost(nodeCost);
        return null;
      }
      List<Road> roads = sections.get(0).getRoads();
      for (Road road : roads) {
        List<Double> vertexes = road.getVertexes();
        for (int q = 0; q < vertexes.size(); q++) {
          pathPointList.add(new Point(vertexes.get(q), vertexes.get(++q)));
        }
      }
      Summary summary = route.getSummary();
      Integer distance = summary.getDistance();
      Integer duration = summary.getDuration();
      Fare fare = summary.getFare();
      Integer taxi = fare.getTaxi();
      Integer toll = fare.getToll();

      nodeCost = new NodeCost();
      nodeCost.setStartNodeId(prev.getId());// 시작노드id
      nodeCost.setEndNodeId(next.getId());// 종료노드id
      nodeCost.setDistanceMeter(distance.longValue());// 이동거리(미터)
      nodeCost.setDurationSecond(duration.longValue());// 이동시간(초)
      nodeCost.setTollFare(toll);// 통행 요금(톨게이트)
      nodeCost.setTaxiFare(taxi);// 택시 요금(지자체별, 심야, 시경계, 복합, 콜비 감안)
      nodeCost.setPathJson(new ObjectMapper().writeValueAsString(pathPointList));// 이동경로json [[x,y],[x,y]]
      nodeCost.setRegDt(new Date());// 등록일시
      nodeCost.setModDt(new Date());// 수정일시
      nodeCostService.saveNodeCost(nodeCost);
    }
    return nodeCost;
  }
  @PostMapping("/vrp1")
  @ResponseBody
  public JsonResult postVrp1(@RequestBody List<CourseDTO> courseList) throws IOException, InterruptedException {
    // 입력된 courseList가 null 또는 빈 리스트면 오류 반환
    if (courseList == null || courseList.isEmpty()) {
      return JsonResult.fail(new IllegalArgumentException("courseList가 비어 있습니다."));
    }

    // === 1. 노드 준비 및 분류 ===
    // fixedNodes: 고정(내집,친구집) 노드, vrpTargetNodes: 일반 노드(관광지), nodeMap: id별 노드 참조용 맵
    List<Node> fixedNodes = new ArrayList<>();
    List<Node> vrpTargetNodes = new ArrayList<>();
    Map<String, Node> nodeMap = new HashMap<>();
    Node startingNode = null;

    // 각 CourseDTO를 Node로 변환하여 고정 노드(cStatus==1)와 일반 노드로 분리
    for (CourseDTO courseListDTO : courseList) {
      Node node = createNodeFromCourseDTO(courseListDTO);
      nodeMap.put(String.valueOf(node.getId()), node);

      if (courseListDTO.getCStatus() == 1) {
        fixedNodes.add(node);
        // 첫번째 고정 노드를 시작 노드로 설정
        if (startingNode == null) {
          startingNode = node;
        }
      } else {
        vrpTargetNodes.add(node);
      }
    }

    // 시작 노드가 설정되지 않은 경우, 일반 노드에서 첫 번째 노드를 시작 노드로 사용
    if (startingNode == null) {
      startingNode = vrpTargetNodes.isEmpty() ? null : vrpTargetNodes.get(0);
      if (startingNode == null) {
        return JsonResult.fail(new IllegalArgumentException("유효한 시작 노드를 찾을 수 없습니다."));
      }
    }
    String startingNodeId = String.valueOf(startingNode.getId());

    // === 2. 모든 노드 간 비용(nodeCost) 계산 ===
    // nodeCostMap: [출발노드ID -> (도착노드ID -> 비용 정보)] 구조로 구성
    Map<String, Map<String, NodeCost>> nodeCostMap = new HashMap<>();
    for (Node startNode : nodeMap.values()) {
      for (Node endNode : nodeMap.values()) {
        if (startNode.equals(endNode)) continue;

        NodeCost nodeCost = getNodeCost(startNode, endNode);
        if (nodeCost == null) {
          // 비용 정보가 없으면 기본값(0) 할당
          nodeCost = new NodeCost();
          nodeCost.setDistanceMeter(0L);
          nodeCost.setDurationSecond(0L);
        }

        String startId = String.valueOf(startNode.getId());
        String endId = String.valueOf(endNode.getId());
        nodeCostMap.computeIfAbsent(startId, k -> new HashMap<>())
                .put(endId, nodeCost);
      }
    }

    // === 3. 고정 노드에 대한 전방 최적화 (Forward VRP) ===
    VrpService forwardVrpService = new VrpService();
    forwardVrpService.addVehicle("차량01", startingNodeId);

    // 시작 노드를 제외한 고정 노드에 대해 배송 요청(Shipment) 등록
    for (Node node : fixedNodes) {
      if (node.equals(startingNode)) continue;
      forwardVrpService.addShipement(node.getName(), startingNodeId, String.valueOf(node.getId()));
    }

    // 고정 노드들 간 이동 비용 등록
    for (Node startNode : fixedNodes) {
      for (Node endNode : fixedNodes) {
        if (startNode.equals(endNode)) continue;
        NodeCost nodeCost = nodeCostMap.get(String.valueOf(startNode.getId())).get(String.valueOf(endNode.getId()));
        if (nodeCost != null) {
          forwardVrpService.addCost(String.valueOf(startNode.getId()),
                  String.valueOf(endNode.getId()),
                  nodeCost.getDurationSecond(),
                  nodeCost.getDistanceMeter());
        }
      }
    }

    // VRP 최적화 결과 획득 및 결과 경로 추출 (동일 노드 연속 방문은 제거)
    VrpResult forwardVrpResult = forwardVrpService.getVrpResult();
    List<Node> optimizedForwardFixedNodes = new ArrayList<>();
    String prevLocationIdForward = null;
    for (VrpVehicleRoute vrpVehicleRoute : forwardVrpResult.getVrpVehicleRouteList()) {
      String locationId = vrpVehicleRoute.getLocationId();
      if (prevLocationIdForward == null || !locationId.equals(prevLocationIdForward)) {
        Node node = nodeMap.get(locationId);
        if (node != null && fixedNodes.contains(node)) {
          optimizedForwardFixedNodes.add(node);
        }
      }
      prevLocationIdForward = locationId;
    }

    // === 4. 고정 노드에 대한 역순 최적화 (Reverse VRP) ===
    VrpService reverseVrpService = new VrpService();
    reverseVrpService.addVehicle("차량01", startingNodeId);

    // 전방 최적화 결과를 역순으로 정렬하여 등록
    List<Node> reversedOptimizedFixedNodesList = new ArrayList<>(optimizedForwardFixedNodes);
    Collections.reverse(reversedOptimizedFixedNodesList);
    for (Node node : reversedOptimizedFixedNodesList) {
      if (!node.equals(startingNode)) {
        reverseVrpService.addShipement(node.getName(), startingNodeId, String.valueOf(node.getId()));
      }
    }
    // 비용 정보 등록 (고정 노드 간)
    for (Node startNode : fixedNodes) {
      for (Node endNode : fixedNodes) {
        if (startNode.equals(endNode)) continue;
        NodeCost nodeCost = nodeCostMap.get(String.valueOf(startNode.getId())).get(String.valueOf(endNode.getId()));
        if (nodeCost != null) {
          reverseVrpService.addCost(String.valueOf(startNode.getId()),
                  String.valueOf(endNode.getId()),
                  nodeCost.getDurationSecond(),
                  nodeCost.getDistanceMeter());
        }
      }
    }

    // 최적화 결과 추출 (연속 중복 제거)
    VrpResult reverseVrpResult = reverseVrpService.getVrpResult();
    List<Node> optimizedReverseFixedNodes = new ArrayList<>();
    String prevLocationIdReverse = null;
    for (VrpVehicleRoute vrpVehicleRoute : reverseVrpResult.getVrpVehicleRouteList()) {
      String locationId = vrpVehicleRoute.getLocationId();
      if (prevLocationIdReverse == null || !locationId.equals(prevLocationIdReverse)) {
        Node node = nodeMap.get(locationId);
        if (node != null && fixedNodes.contains(node)) {
          optimizedReverseFixedNodes.add(node);
        }
      }
      prevLocationIdReverse = locationId;
    }

    // === 5. 일반 노드에 대한 VRP 최적화 ===
    VrpService vrpService = new VrpService();
    vrpService.addVehicle("차량01", startingNodeId);

    // 일반 노드(배송 대상) 등록
    for (Node node : vrpTargetNodes) {
      vrpService.addShipement(node.getName(), startingNodeId, String.valueOf(node.getId()));
    }
    // 전체 노드(고정 + 일반) 간 비용 정보 등록
    for (Node startNode : nodeMap.values()) {
      for (Node endNode : nodeMap.values()) {
        if (startNode.equals(endNode)) continue;
        NodeCost nodeCost = nodeCostMap.get(String.valueOf(startNode.getId())).get(String.valueOf(endNode.getId()));
        if (nodeCost != null) {
          vrpService.addCost(String.valueOf(startNode.getId()),
                  String.valueOf(endNode.getId()),
                  nodeCost.getDurationSecond(),
                  nodeCost.getDistanceMeter());
        }
      }
    }
    // 최적화 결과 및 경로 추출
    VrpResult vrpResult = vrpService.getVrpResult();
    List<Node> optimizedVrpNodes = new ArrayList<>();
    String prevLocationIdVrp = null;
    for (VrpVehicleRoute vrpVehicleRoute : vrpResult.getVrpVehicleRouteList()) {
      String locationId = vrpVehicleRoute.getLocationId();
      if (prevLocationIdVrp == null || !locationId.equals(prevLocationIdVrp)) {
        Node node = nodeMap.get(locationId);
        if (node != null && vrpTargetNodes.contains(node)) {
          optimizedVrpNodes.add(node);
        }
      }
      prevLocationIdVrp = locationId;
    }

    // === 6. 최종 경로 구성 ===
    // 경로: 시작노드 → 전방 최적화 고정노드(시작노드 제외) → 일반 노드 최적화 결과 → 역순 최적화 고정노드(시작노드 제외) → 시작노드(복귀)
    List<Node> calculatedRoute = new ArrayList<>();
    calculatedRoute.add(startingNode);

    for (Node node : optimizedForwardFixedNodes) {
      if (!node.equals(startingNode)) {
        calculatedRoute.add(node);
      }
    }
    calculatedRoute.addAll(optimizedVrpNodes);
    for (Node node : optimizedReverseFixedNodes) {
      if (!node.equals(startingNode)) {
        calculatedRoute.add(node);
      }
    }
    calculatedRoute.add(startingNode);

    // === 7. 총 이동 거리, 시간 및 경로 좌표 계산 ===
    int totalDistance = 0;
    int totalDuration = 0;
    List<Point> totalPathPointList = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();

    for (int i = 1; i < calculatedRoute.size(); i++) {
      Node prev = calculatedRoute.get(i - 1);
      Node next = calculatedRoute.get(i);

      NodeCost nodeCost = nodeCostMap.get(String.valueOf(prev.getId())).get(String.valueOf(next.getId()));
      if (nodeCost == null) {
        // 비용 정보 누락 시 로그 출력 후 건너뜀
        System.err.println("Error: NodeCost not found for " + prev.getId() + " -> " + next.getId());
        continue;
      }

      totalDistance += nodeCost.getDistanceMeter();
      totalDuration += nodeCost.getDurationSecond();

      String pathJson = nodeCost.getPathJson();
      if (pathJson != null) {
        totalPathPointList.addAll(objectMapper.readValue(pathJson, new TypeReference<List<Point>>() {}));
      }
    }

    // 최종 결과를 JsonResult 객체에 담아 반환
    JsonResult jsonResult = new JsonResult();
    jsonResult.addData("totalDistance", totalDistance);
    jsonResult.addData("totalDuration", totalDuration);
    jsonResult.addData("totalPathPointList", totalPathPointList);
    jsonResult.addData("nodeList", calculatedRoute);
    return jsonResult;
  }

  // CourseDTO를 Node로 변환하는 헬퍼 메서드
  private Node createNodeFromCourseDTO(CourseDTO courseDTO) {
    Node node = new Node();
    node.setId(courseDTO.getId());
    node.setName(courseDTO.getName());
    node.setAddress(courseDTO.getAddress());
    node.setPhone(courseDTO.getPhone());
    node.setX(courseDTO.getX());
    node.setY(courseDTO.getY());
    node.setRegDt(new Date());
    node.setModDt(new Date());
    return node;
  }


  @PostMapping("/vrp")
  @ResponseBody
  public JsonResult postVrp(@RequestBody List<Node> nodeList) throws IOException, InterruptedException {
    VrpService vrpService = new VrpService();

    // 첫 번째 노드를 시작 노드로 설정
    Node firstNode = nodeList.get(0);
    String firstNodeId = String.valueOf(firstNode.getId());
    vrpService.addVehicle("차량01", firstNodeId);

    // 노드 및 비용 정보를 담기 위한 맵 초기화
    Map<String, Node> nodeMap = new HashMap<>();
    Map<String, Map<String, NodeCost>> nodeCostMap = new HashMap<>();

    // 모든 노드에 대해 배송 요청(Shipment) 등록 및 참조맵 구성
    for (Node node : nodeList) {
      String nodeId = String.valueOf(node.getId());
      vrpService.addShipement(node.getName(), firstNodeId, nodeId);
      nodeMap.put(nodeId, node);
    }

    // 각 노드 쌍에 대한 이동 비용 계산 및 등록
    for (int i = 0; i < nodeList.size(); i++) {
      Node startNode = nodeList.get(i);
      for (int j = 0; j < nodeList.size(); j++) {
        Node endNode = nodeList.get(j);
        if (i == j) continue;

        NodeCost nodeCost = getNodeCost(startNode, endNode);
        if (nodeCost == null) {
          nodeCost = new NodeCost();
          nodeCost.setDistanceMeter(0L);
          nodeCost.setDurationSecond(0L);
        }
        String startNodeId = String.valueOf(startNode.getId());
        String endNodeId = String.valueOf(endNode.getId());

        vrpService.addCost(startNodeId, endNodeId, nodeCost.getDurationSecond(), nodeCost.getDistanceMeter());
        nodeCostMap.computeIfAbsent(startNodeId, k -> new HashMap<>())
                .put(endNodeId, nodeCost);
      }
    }

    // VRP 최적화 수행 후 결과 경로 추출
    List<Node> vrpNodeList = new ArrayList<>();
    VrpResult vrpResult = vrpService.getVrpResult();

    // 연속된 동일 위치 건너뛰면서 경로 구성
    String prevLocationId = null;
    for (VrpVehicleRoute vrpVehicleRoute : vrpResult.getVrpVehicleRouteList()) {
      System.out.println(vrpVehicleRoute);
      String locationId = vrpVehicleRoute.getLocationId();
      if (prevLocationId == null) {
        prevLocationId = locationId;
      } else if (locationId.equals(prevLocationId)) {
        continue;
      }
      prevLocationId = locationId;
      vrpNodeList.add(nodeMap.get(locationId));
    }

    // 총 이동 거리, 시간, 경로 좌표 계산
    int totalDistance = 0;
    int totalDuration = 0;
    List<Point> totalPathPointList = new ArrayList<>();
    for (int i = 1; i < vrpNodeList.size(); i++) {
      Node prev = vrpNodeList.get(i - 1);
      Node next = vrpNodeList.get(i);
      NodeCost nodeCost = nodeCostMap.get(String.valueOf(prev.getId())).get(String.valueOf(next.getId()));
      if (nodeCost == null) {
        continue;
      }
      totalDistance += nodeCost.getDistanceMeter();
      totalDuration += nodeCost.getDurationSecond();
      String pathJson = nodeCost.getPathJson();
      if (pathJson != null) {
        totalPathPointList.addAll(new ObjectMapper().readValue(pathJson, new TypeReference<List<Point>>() {}));
      }
    }

    // 결과 데이터 구성 및 반환
    JsonResult jsonResult = new JsonResult();
    jsonResult.addData("totalDistance", totalDistance); // 전체 이동 거리
    jsonResult.addData("totalDuration", totalDuration); // 전체 이동 시간
    jsonResult.addData("totalPathPointList", totalPathPointList); // 전체 경로 좌표 리스트
    jsonResult.addData("nodeList", vrpNodeList); // 방문 노드 순서
    return jsonResult;
  }



  @DeleteMapping("/deleteNode/{nodeId}")
  public ResponseEntity<String> deleteNode(@PathVariable Long nodeId) {
    try {
      System.out.println(nodeId + "노드아이디 확인");
      nodeDao.deleteById(nodeId);
      course.deletebynodeId(nodeId);
      return new ResponseEntity<>("삭제 성공", HttpStatus.OK);
    } catch (Exception e) {
      System.out.println("에러발생"+e.getMessage());
      return new ResponseEntity<>("삭제 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}