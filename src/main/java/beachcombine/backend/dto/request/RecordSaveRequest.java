package beachcombine.backend.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordSaveRequest {

    private Time time;
    private Long range;
    private MultipartFile beforeImage;
    private MultipartFile afterImage;
}
