package beachcombine.backend.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NearBeachRequest {

    private Long beachId;
    private String lat;
    private String lng;
}
