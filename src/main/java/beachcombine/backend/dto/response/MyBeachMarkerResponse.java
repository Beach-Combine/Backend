package beachcombine.backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyBeachMarkerResponse {

    private Long id;
    private String name;
    private String lat;
    private String lng;
}
