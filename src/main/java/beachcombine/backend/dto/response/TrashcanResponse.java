package beachcombine.backend.dto.response;

import beachcombine.backend.domain.Trashcan;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@Builder
public class TrashcanResponse {

    private String lat;
    private String lng;

    public TrashcanResponse(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
    public TrashcanResponse(Trashcan trashcan) {
        this.lat = String.valueOf(trashcan.getLat());
        this.lng = String.valueOf(trashcan.getLng());
    }
}
