package beachcombine.backend.dto.response;

import beachcombine.backend.domain.Beach;
import beachcombine.backend.domain.Member;
import beachcombine.backend.domain.Record;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Time;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeachLatestRecordResponse {

    private BeachDto beach;
    private RecordDto record;
    private MemberDto member;

    @Data
    @Builder
    public static class BeachDto {

        private Long id;
        private String name;

        public static BeachDto from(Beach beach) {

            return BeachDto.builder()
                    .id(beach.getId())
                    .name(beach.getName())
                    .build();
        }
    }

    @Data
    @Builder
    public static class RecordDto {

        private Long id;
        private String nickname;
        @JsonFormat(pattern = "yy.MM.dd")
        private LocalDateTime date;
        private Time time;
        private Long range;
        private String beforeImage;
        private String afterImage;

        public static RecordDto from(Record record, String beforeImage, String afterImage) {

            return RecordDto.builder()
                    .id(record.getId())
                    .nickname(record.getMember().getNickname())
                    .date(record.getCreatedDate())
                    .time(record.getDuration())
                    .range(record.getDistance())
                    .beforeImage(beforeImage)
                    .afterImage(afterImage)
                    .build();
        }
    }

    @Data
    @Builder
    public static class MemberDto {

        private Long id;
        private String nickname;

        public static MemberDto from(Member member) {

            return MemberDto.builder()
                    .id(member.getId())
                    .nickname(member.getNickname())
                    .build();
        }
    }
}
