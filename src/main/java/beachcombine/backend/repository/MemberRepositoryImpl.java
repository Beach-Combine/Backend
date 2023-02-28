package beachcombine.backend.repository;

import beachcombine.backend.domain.Member;
import beachcombine.backend.dto.response.MemberRankingResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.function.Function;

import static beachcombine.backend.domain.QMember.member;

public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Member> findByTotalPointRanking(int pageSize, Long lastId, Integer lastPoint) {

        return queryFactory
                .select(member)
                .from(member)
                .where(whereClauseTotal(lastId, lastPoint))
                .orderBy(member.totalPoint.desc(), member.id.asc())
                .limit(pageSize)
                .fetch();
    }

    @Override
    public List<Member> findByMonthPointRanking(int pageSize, Long lastId, Integer lastPoint) {

        return queryFactory
                .select(member)
                .from(member)
                .where(whereClauseMonth(lastId, lastPoint))
                .orderBy(member.monthPoint.desc(), member.id.asc())
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression whereClauseTotal(Long lastId, Integer lastPoint) {

        if(lastId != null && lastPoint != null) {
            return member.totalPoint.loe(lastPoint).and(member.id.gt(lastId));
        }
        return null;
    }

    private BooleanExpression whereClauseMonth(Long lastId, Integer lastPoint) {

        if(lastId != null && lastPoint != null) {
            return member.monthPoint.loe(lastPoint).and(member.id.gt(lastId));
        }
        return null;
    }
}
