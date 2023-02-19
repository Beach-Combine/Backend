package beachcombine.backend.dto.response;

import lombok.*;
import beachcombine.backend.common.entity.Coordinates;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrashcanResponse {

    private List<Coordinates> trashcans;

}
