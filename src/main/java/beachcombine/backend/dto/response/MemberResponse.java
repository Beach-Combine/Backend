package beachcombine.backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {

    private Long id;
    private String email;
    private String nickname;
    private String image;
    private Integer totalPoint; // 전체 포인트
    private Integer monthPoint; // 월간 포인트
    private Boolean profilePublic; // 프로필 공개 여부
}
