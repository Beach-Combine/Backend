package beachcombine.backend.dto.request;

import lombok.*;

import beachcombine.backend.common.entity.Coordinates;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrashcanLocationRequest {

    private Coordinates northEast; // {lat, lng}
    private Coordinates southWest;
}
