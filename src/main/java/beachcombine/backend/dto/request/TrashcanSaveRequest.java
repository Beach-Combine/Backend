package beachcombine.backend.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrashcanSaveRequest {

    @NotNull
    @Range(min = -90, max = 90)
    private BigDecimal lat;

    @NotNull
    @Range(min = -180, max = 180)
    private BigDecimal lng;

    @NotNull
    private MultipartFile image;
}
