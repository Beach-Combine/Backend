package beachcombine.backend.dto.response;

import beachcombine.backend.domain.Trashcan;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrashcanResponse {

    private String lat;
    private String lng;

    public TrashcanResponse(Trashcan trashcan) {
        this.lat = String.valueOf(trashcan.getLat());
        this.lng = String.valueOf(trashcan.getLng());
    }
}
