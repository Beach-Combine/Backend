package beachcombine.backend.domain;

import beachcombine.backend.common.entity.BaseEntity;
import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.dto.response.BeachBadgeResponse;
import beachcombine.backend.repository.RecordRepository;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

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


    public BeachBadgeResponse getBeachBadgeImage() {

        return BeachBadgeResponse.builder()
                .id(id)
                .badgeImage(badgeImage)
                .build();
    }
}
