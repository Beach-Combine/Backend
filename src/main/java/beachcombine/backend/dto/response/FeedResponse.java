package beachcombine.backend.dto.response;

import beachcombine.backend.domain.Record;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedResponse {

    private Long id;
    private String review;
    private Long recordId;
    private Long memberId;
    private String nickname;
    private String beforeImage;
    private String afterImage;
    private String beachName;
}
