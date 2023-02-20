package beachcombine.backend.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeachResponse {

    private Long id;
    private String name;
    private String badgeImage;
    private BigDecimal lat;
    private BigDecimal  lng;
    private Long recordId;
}