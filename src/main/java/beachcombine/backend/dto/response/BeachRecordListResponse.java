package beachcombine.backend.dto.response;

import beachcombine.backend.domain.Beach;
import beachcombine.backend.domain.Record;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeachRecordListResponse {

    private BeachDto beach;
    private List<RecordDto> recordList;

    @Data
    @Builder
    public static class BeachDto {

        private String name;

        public static BeachRecordListResponse.BeachDto from(Beach beach) {

            return BeachRecordListResponse.BeachDto.builder()
                    .name(beach.getName())
                    .build();
        }
    }

    @Data
    @Builder
    public static class RecordDto {

        private Long id;
        @JsonFormat(pattern = "yy.MM.dd")
        private LocalDateTime date;
        private Time time;
        private Long range;
        private String beforeImage;
        private String afterImage;

        public static RecordDto from(Record record, String beforeImage, String afterImage) {

            return BeachRecordListResponse.RecordDto.builder()
                    .id(record.getId())
                    .date(record.getCreatedDate())
                    .time(record.getDuration())
                    .range(record.getDistance())
                    .beforeImage(beforeImage)
                    .afterImage(afterImage)
                    .build();
        }
    }
}
