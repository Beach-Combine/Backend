package beachcombine.backend.domain;

import beachcombine.backend.common.entity.BaseEntity;
import beachcombine.backend.dto.request.MemberUpdateRequest;
import lombok.*;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@BatchSize(size = 50)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    // 소셜 로그인
    private String loginId; // 로컬 로그인 확장 대비. username 역할
    private String password; // 일반 로그인 위함
    private String provider; // 소셜로그인 구분 위함.

    // 회원 기본 정보
    private String email;
    @Column(nullable = false, unique = true, length = 20)
    private String nickname;
    private String image; // Google Cloud Storage에 저장된 이미지 파일 이름
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
    private List<Record> records = new ArrayList<>();  // 청소 기록 리스트 (Record:Member=다:1)

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Purchase> purchaseList = new ArrayList<>();  // 구매 기록 리스트 (Purchase:Member=다:1)

    public List<String> getRoleAsList() {

        if (this.role.length() > 0) {
            return Arrays.asList(this.role);
        }
        return new ArrayList<>();
    }

    public void updateMember(MemberUpdateRequest dto, String imageUrl) {

        this.nickname = dto.getNickname();
        if (dto.getIsChanged()) { // image 수정 여부 체크. 변경한 이미지가 null 값으로 들어올 수도 있어서 null로 체크하면 안됨
            this.image = imageUrl;
        }
    }

    public void updateProfilePublic(Boolean option) {

        this.profilePublic = option;
    }

    public Boolean updateMemberPoint(int option) {

        if (option == 0) { // 기존 등록된 쓰레기통
            this.totalPoint += 100;
            this.monthPoint += 100;
            return true;
        }
        if (option == 1) {
            this.totalPoint += 30;
            this.monthPoint += 30;
            return true;
        }
        return false;
    }

    public boolean isUpdatedNickname(String nickname) {

        if (StringUtils.isNotBlank(nickname) && !nickname.equals(this.nickname)) {
            return true;
        }
        return false;
    }

}
