package beachcombine.backend.repository;

import beachcombine.backend.domain.Beach;
import beachcombine.backend.domain.Record;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static beachcombine.backend.domain.QRecord.record;

public class RecordRepositoryImpl implements RecordRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public RecordRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Beach> findBeachList(Long memberId) {

        return queryFactory
                .select(record.beach)
                .from(record)
                .where(record.member.id.eq(memberId))
                .groupBy(record.beach)
                .orderBy(OrderByNull.DEFAULT) // 최적화용
                .fetch();
    }

    @Override
    public List<Record> findMyBeachRecord(Long memberId, Long beachId) {

        return queryFactory
                .select(record)
                .from(record)
                .where(record.member.id.eq(memberId)
                .and(record.beach.id.eq(beachId)))
                .orderBy(record.createdDate.desc())
                .fetch();
    }
}
