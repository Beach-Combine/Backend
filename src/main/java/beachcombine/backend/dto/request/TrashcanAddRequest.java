package beachcombine.backend.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrashcanAddRequest {

    private BigDecimal lat;
    private BigDecimal  lng;
    private String image;
}
