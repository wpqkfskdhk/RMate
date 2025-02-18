package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "node_cost")
public class NodeCostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UB_SEQ_GEN")
    @SequenceGenerator(name = "UB_SEQ_GEN", sequenceName = "UB_SEQ", allocationSize = 1)
    @Column(name = "id")
    private Long id;            //노드비용id

    @Column(name = "start_node_id", nullable = false)
    private Long startNodeId;   //시작노드id

    @ManyToOne
    @JoinColumn(name = "start_node_id", referencedColumnName = "id", insertable = false, updatable = false)
    private NodeEntity startNode;

    @Column(name = "end_node_id", nullable = false)
    private Long endNodeId;     //종료노드id

    @ManyToOne
    @JoinColumn(name = "end_node_id", referencedColumnName = "id", insertable = false, updatable = false)
    private NodeEntity endNode;

    @Column(name = "distance_meter")
    private Long distanceMeter;     //이동거리(미터)

    @Column(name = "duration_second")
    private Long durationSecond;    //이동시간(초)

    @Column(name = "toll_fare")
    private Integer tollFare;       //통행 요금(톨게이트)

    @Column(name = "taxi_fare")
    private Integer taxiFare;       //택시 요금(지자체별, 심야, 시경계, 복합, 콜비 감안)

    @Column(name = "fuel_price")
    private Integer fuelPrice;      //해당 시점의 전국 평균 유류비와 연비를 감안한 유류비

    @Lob
    @Column(name = "path_json")
    private String pathJson;    //이동경로json [[x,y],[x,y]]

    @CreationTimestamp
    @Column(name = "reg_dt", nullable = false, updatable = false)
    private LocalDateTime regDt;         //등록일시

    @CreationTimestamp
    @Column(name = "mod_dt", insertable = false)
    private LocalDateTime modDt;         //수정일시

    public static NodeCostEntity toEntity(NodeCostDTO dto) {
        NodeCostEntity entity = new NodeCostEntity();
        entity.setId(dto.getId());
        entity.setStartNodeId(dto.getStartNodeId());
        entity.setEndNodeId(dto.getEndNodeId());
        entity.setDistanceMeter(dto.getDistanceMeter());
        entity.setDurationSecond(dto.getDurationSecond());
        entity.setTollFare(dto.getTollFare());
        entity.setTaxiFare(dto.getTaxiFare());
        entity.setFuelPrice(dto.getFuelPrice());
        entity.setPathJson(dto.getPathJson());
        entity.setRegDt(dto.getRegDt());
        entity.setModDt(dto.getModDt());

        return entity;
    }


}
