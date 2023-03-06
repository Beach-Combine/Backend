package beachcombine.backend.domain;

import beachcombine.backend.common.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.sql.Time;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Record extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "beach_id")
    private Beach beach; // 청소한 곳 (Record:Beach=다:1)

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 청소한 사람 (Record:Member=다:1)

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "record_id", referencedColumnName = "id")
    private Feed feed;

    private Time duration; // time -> 00:00:00 (시, 분, 초)
    private Long distance; // range
    private String beforeImage;
    private String afterImage;

    // 연관관계 메서드
    public void setMember(Member member) {

        this.member = member;
        member.getRecords().add(this);
    }

    public void setBeach(Beach beach) {

        this.beach = beach;
        beach.getRecords().add(this);
    }

    public void setFeed(Feed feed) {

        this.feed = feed;
        feed.setRecord(this);
    }
}