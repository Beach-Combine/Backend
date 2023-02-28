package beachcombine.backend.repository;

import beachcombine.backend.dto.response.MemberRankingResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import java.util.List;

import static beachcombine.backend.domain.QMember.member;

public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MemberRankingResponse> findByTotalPointRanking(int pageSize, Long lastId, Integer lastPoint) {

        return queryFactory
                .select(Projections.fields(MemberRankingResponse.class,
                        member.id,
                        member.nickname,
                        member.image,
                        member.totalPoint.as("point")))
                .from(member)
                .where(
                        whereClause(lastId, lastPoint)
                )
                .orderBy(member.totalPoint.desc(), member.id.asc())
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression whereClause(Long lastId, Integer lastPoint) {

        if(lastId != null && lastPoint != null) {
            return member.totalPoint.loe(lastPoint).and(member.id.gt(lastId));
        }
        return null;
    }
}
