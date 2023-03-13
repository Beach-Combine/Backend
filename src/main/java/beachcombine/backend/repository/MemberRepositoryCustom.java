package beachcombine.backend.repository;

import beachcombine.backend.domain.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    List<Member> findByTotalPointRanking(int pageSize, Long lastId, Integer lastPoint);
    List<Member> findByMonthPointRanking(int pageSize, Long lastId, Integer lastPoint);
}
