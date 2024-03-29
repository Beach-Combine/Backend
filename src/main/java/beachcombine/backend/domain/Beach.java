package beachcombine.backend.domain;

import beachcombine.backend.common.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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

    @Column(nullable = false, precision = 10, scale = 8)
    private BigDecimal lat;
    @Column(nullable = false, precision = 11, scale = 8)
    private BigDecimal lng;

    private String beachRange;

    @Builder.Default
    @OneToMany(mappedBy = "beach")
    private List<Record> records = new ArrayList<>();  // 청소 기록 리스트 (Record:Beach=다:1)
}
