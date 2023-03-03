package beachcombine.backend.domain;

import beachcombine.backend.common.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    private String review;

    @OneToOne(mappedBy = "feed", fetch= LAZY)
    private Record record; // 피드 작성자 정보는 record 에서 조회

}
