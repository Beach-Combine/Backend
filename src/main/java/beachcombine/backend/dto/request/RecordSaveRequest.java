package beachcombine.backend.dto.request;

import lombok.*;

import java.sql.Date;
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
    private String beforeImage;
    private String afterImage;
}
