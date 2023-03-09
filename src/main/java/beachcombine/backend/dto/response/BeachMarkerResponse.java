package beachcombine.backend.dto.response;

import beachcombine.backend.domain.Beach;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeachMarkerResponse {

    private Long id;
    private String name;
    private String lat;
    private String lng;
    private String image; // 최근 청소기록 유저 프로필이미지
}
