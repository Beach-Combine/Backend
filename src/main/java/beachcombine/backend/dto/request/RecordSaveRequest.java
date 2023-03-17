package beachcombine.backend.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordSaveRequest {

    @NotNull
    private Time time;
    @NotNull
    private Long range;
    @NotNull
    private MultipartFile beforeImage;
    @NotNull
    private MultipartFile afterImage;
}
