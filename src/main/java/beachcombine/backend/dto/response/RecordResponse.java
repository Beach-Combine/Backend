package beachcombine.backend.dto.response;

import beachcombine.backend.domain.Record;
import lombok.*;

import java.sql.Time;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordResponse {

    private Long recordId;
    private Time time;
    private LocalDateTime date; // created_date , datetime(6)
    private Long range;
    private String beforeImage;
    private String afterImage;
    private Boolean isWritten;
    private Long beachId;
    private String beachName;
}