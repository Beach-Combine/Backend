package beachcombine.backend.repository;

import beachcombine.backend.domain.Feed;
import beachcombine.backend.domain.Member;
import beachcombine.backend.domain.MemberPreferredFeed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPreferredFeedRepository extends JpaRepository<MemberPreferredFeed, Long> {

    Boolean existsByMemberAndFeed(Member member, Feed feed);
    void deleteByMemberAndFeed(Member member, Feed feed);
}
