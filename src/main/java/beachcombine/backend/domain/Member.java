package beachcombine.backend.domain;


import beachcombine.backend.common.entity.BaseEntity;
import beachcombine.backend.dto.response.BeachMarkerImageResponse;
import beachcombine.backend.dto.response.BeachResponse;
import beachcombine.backend.dto.response.MemberResponse;
import beachcombine.backend.dto.request.MemberUpdateRequest;
import lombok.*;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

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
    @Column(nullable = false, unique = true, length=20)
    private String nickname;
    private String image;
    private String role; // USER 혹은 ADMIN

    // 회원 추가 정보
    @Builder.Default
    private Integer totalPoint = 0; // 전체 포인트
    @Builder.Default
    private Integer monthPoint = 0; // 월간 포인트
    @Builder.Default
    private Integer purchasePoint = 0; // 구매 포인트

    @Builder.Default
    private Boolean profilePublic = true; // 프로필 공개 여부

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Record>  records = new ArrayList<>();  // 청소 기록 리스트

    public MemberResponse getMemberInfo() {

        return MemberResponse.builder()
                .id(id)
                .nickname(nickname)
                .image(image)
                .totalPoint(totalPoint)
                .monthPoint(monthPoint)
                .profilePublic(profilePublic)
                .build();
    }

    public void updateMemberInfo(MemberUpdateRequest dto) {

        this.nickname = dto.getNickname();
        this.image = dto.getImage();
    }

    public void updateProfilePublic(Boolean option) {

        this.profilePublic = option;
    }

    public boolean isUpdatedNickname(String nickname){

        if(StringUtils.isNotBlank(nickname) && !nickname.equals(this.nickname)){
            return true;
        }
        return false;
    }

    public List<String> getRoleList() {

        if (this.role.length() > 0) {
            return Arrays.asList(this.role.split(","));
        }
        return new ArrayList<>();
    }

    public Boolean updateMemberPoint(int option) {

        if(option ==0){ // 기존 등록된 쓰레기통
            this.totalPoint += 100;
            this.monthPoint += 100;
            return true;
        }
        else if(option ==1){
            this.totalPoint += 30;
            this.monthPoint += 30;
            return true;
        }
        return false;
    }

    public BeachMarkerImageResponse getProfileImage() {

        return BeachMarkerImageResponse.builder()
                .latestRecordImage(image)
                .build();
    }
}
