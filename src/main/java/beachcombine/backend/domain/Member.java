package beachcombine.backend.domain;


import beachcombine.backend.dto.member.MemberResponse;
import beachcombine.backend.dto.member.MemberUpdateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nickname"}, name = "unique_data")
})
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
    @Column(nullable =false)
    private String nickname;
    private String image;
    private String roles; // USER 혹은 ADMIN

    private Integer totalPoint; // 전체 포인트
    private Integer monthPoint; // 월간 포인트
    private Integer purchasePoint; // 구매 포인트

    @Builder.Default
    private Boolean profilePublic = true; // 프로필 공개 여부

    public MemberResponse getMemberInfo() {
        return new MemberResponse(id, email, nickname, image, totalPoint, monthPoint, profilePublic);
    }

    public void updateMemberInfo(MemberUpdateRequest dto) {
        this.nickname = dto.getNickname();
        this.image = dto.getImage();
    }
}
