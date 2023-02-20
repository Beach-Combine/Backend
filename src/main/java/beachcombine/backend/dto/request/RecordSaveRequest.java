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

    private Long beachId;
    private Time duration;
    private Long distance;
    private MultipartFile beforeImage;
    private MultipartFile afterImage;
}
