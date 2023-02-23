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
    private Long beachId;
    private Time duration;
    private LocalDateTime date; // created_date , datetime(6)
    private Long distance;
    private String beforeImage;
    private String afterImage;


    public RecordResponse(Record record) {
        this.recordId = record.getId();
        this.beachId = record.getBeach().getId();
        this.duration = record.getDuration();
        this.date = record.getCreatedDate();
        this.distance = record.getDistance();
        this.beforeImage = record.getBeforeImage();
        this.afterImage = record.getAfterImage();
    }
}
