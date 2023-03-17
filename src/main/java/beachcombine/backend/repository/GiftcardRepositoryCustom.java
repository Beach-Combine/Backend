package beachcombine.backend.repository;

import beachcombine.backend.domain.Giftcard;

import java.util.List;

public interface GiftcardRepositoryCustom {

    List<Giftcard> findByMember(Long memberId);
}
