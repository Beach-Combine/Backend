package beachcombine.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Giftcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "giftcard_id")
    private Long id;

    private Integer cost;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_id")
    private Store store; // 기프트카드의 소속 스토어 (Giftcard:Store=다:1)
}
