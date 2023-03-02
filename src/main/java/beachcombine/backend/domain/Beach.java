package beachcombine.backend.domain;

import beachcombine.backend.common.entity.BaseEntity;
import beachcombine.backend.dto.response.BeachBadgeResponse;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beach extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "beach_id")
    private Long id;

    private String name;
    private String badgeImage;

    @Column(nullable = false, precision =10, scale = 8)
    private BigDecimal lat;
    @Column(nullable = false, precision =11, scale = 8)
    private BigDecimal  lng;

    @Builder.Default
    @OneToMany(mappedBy = "beach")
    private List<Record> records = new ArrayList<>();  // 청소 기록 리스트 (Record:Beach=다:1)

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "beachRange_id")
    private BeachRange beachRange;

    public BeachBadgeResponse getBeachBadgeImage() {

        return BeachBadgeResponse.builder()
                .id(id)
                .badgeImage(badgeImage)
                .build();
    }

    // 연관관계 메서드
    public void setBeachRange(BeachRange beachRange) {
        this.beachRange = beachRange;
        beachRange.setBeach(this);
    }

    public static Beach createBeach(Member member, BeachRange beachRange, List<Record> records) {
        Beach beach = new Beach();
        beach.setBeachRange(beachRange);
        beach.setRecords(records);
        return beach;
    }
}
