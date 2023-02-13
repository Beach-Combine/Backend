package beachcombine.backend.repository;

import beachcombine.backend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsByNickname(String nickname);
}