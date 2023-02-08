package beachcombine.backend.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String loginId; // 소셜로그인 구분 & 로컬 로그인 확장 대비
    private String email;
    private String nickname;
    private String image;

    private Integer totalPoint; // 전체 포인트
    private Integer monthPoint; // 월간 포인트
    private Integer purchasePoint; // 구매 포인트

    @Builder.Default
    private Boolean profilePublic = true; // 프로필 공개 여부
}
