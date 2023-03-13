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
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 구매한 사람 (Purchase:Member=다:1)

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "giftcard_id")
    private Giftcard giftcard; // 구매한 기프트카드 (Purchase:Giftcard=다:1)

    // 연관관계 메서드
    public void setMember(Member member) {

        this.member = member;
        member.getPurchaseList().add(this);
    }
}