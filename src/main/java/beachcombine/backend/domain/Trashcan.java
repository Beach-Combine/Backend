package beachcombine.backend.domain;

import beachcombine.backend.common.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trashcan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trashcan_id")
    private Long id;

    @Column(nullable = false, precision =10, scale = 8)
    private BigDecimal lat;
    @Column(nullable = false, precision =11, scale = 8)
    private BigDecimal  lng;

    private Boolean certified;
    private Boolean userAdded;

    private String image; // 사용자가 쓰레기통 등록할 때 업로드하는 사진. 나중에 쓰레기통 인증할 때 사용

}
