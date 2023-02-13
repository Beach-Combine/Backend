package beachcombine.backend.repository;

import beachcombine.backend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByLoginId(String loginId);
    Boolean existsByNickname(String nickname);
}