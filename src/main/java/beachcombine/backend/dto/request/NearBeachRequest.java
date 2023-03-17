package beachcombine.backend.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NearBeachRequest {

    @NotNull
    private Long beachId;

    @NotNull
    @Range(min = -90, max = 90)
    private String lat;

    @NotNull
    @Range(min = -180, max = 180)
    private String lng;
}
