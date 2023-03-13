package beachcombine.backend.repository;

import beachcombine.backend.domain.Giftcard;
import beachcombine.backend.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static beachcombine.backend.domain.QGiftcard.giftcard;
import static beachcombine.backend.domain.QPurchase.purchase;
import static beachcombine.backend.domain.QMember.member;

public class GiftcardRepositoryImpl implements GiftcardRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public GiftcardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Giftcard> findByMember(Long memberId) {

        return queryFactory
                .select(giftcard)
                .from(giftcard, purchase)
                .where(purchase.giftcard.id.eq(giftcard.id)
                .and(purchase.member.id.eq(memberId)))
                .fetch();
    }
}
