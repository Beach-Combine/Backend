package beachcombine.backend.domain;


import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Time duration; // 밀리세컨드 값
    private Long distance;
    private String beforeImage;
    private String afterImage;
    private Date date;

    // 연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getRecords().add(this);
    }

    // 생성 메서드
    public static Record createRecord(Member member) {
        Record record = new Record();
        record.setMember(member);
        return record;
    }
}