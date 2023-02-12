package beachcombine.backend.domain;


import beachcombine.backend.dto.response.MemberResponse;
import beachcombine.backend.dto.request.MemberUpdateRequest;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nickname"}, name = "unique_data")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    // 소셜 로그인
    @Column(unique = true)
    private String loginId; // 소셜로그인 구분 & 로컬 로그인 확장 대비. username 역할
    private String password;
    private String providerId;
    private String provider;

    // 회원 기본 정보
    private String email;
    @Column(nullable =false)
    private String nickname;
    private String image;
    private String role; // USER 혹은 ADMIN

    // 회원 추가 정보
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

    public List<String> getRoleList() {

        if(this.role.length() > 0) {
            return Arrays.asList(this.role.split(","));
        }
        return new ArrayList<>();
    }
}
