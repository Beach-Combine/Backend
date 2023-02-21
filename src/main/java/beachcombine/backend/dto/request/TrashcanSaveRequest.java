package beachcombine.backend.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrashcanSaveRequest {

    private BigDecimal lat;
    private BigDecimal lng;
    private MultipartFile image;
}
