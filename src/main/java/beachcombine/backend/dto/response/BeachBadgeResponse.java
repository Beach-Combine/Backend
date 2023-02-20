package beachcombine.backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeachBadgeResponse {

    private Long id;
    private String badgeImage;
}
