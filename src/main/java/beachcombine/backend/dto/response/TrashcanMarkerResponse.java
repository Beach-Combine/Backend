package beachcombine.backend.dto.response;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrashcanMarkerResponse {

    private String lat;
    private String lng;
    private Long id;
}
