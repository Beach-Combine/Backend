package beachcombine.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GiftcardResponse {

    private Long id;
    private String name;
    private String location;
    private String image;
    private Integer cost;
}
