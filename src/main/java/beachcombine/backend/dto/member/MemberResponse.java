package beachcombine.backend.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {

    private Long id;
    private String email;
    private String nickname;
    private String image;
    private Integer totalPoint; // 전체 포인트
    private Integer monthPoint; // 월간 포인트
    private Boolean profilePublic; // 프로필 공개 여부
}
