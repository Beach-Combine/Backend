package beachcombine.backend.domain;


import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "beach_id")
    private Beach beach;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private Time duration; // 밀리세컨드 값
    private Long distance;
    private String beforeImage;
    private String afterImage;
    private Date date;


    // 생성 메서드
    public static Record createRecord(Member member) {
        Record record = new Record();
        record.setMember(member);
        return record;
    }
}