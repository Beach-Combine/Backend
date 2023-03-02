package beachcombine.backend.repository;

import beachcombine.backend.domain.Member;
import beachcombine.backend.dto.response.MemberRankingResponse;
import com.querydsl.core.types.dsl.NumberPath;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;

public interface MemberRepositoryCustom {

    List<Member> findByTotalPointRanking(int pageSize, Long lastId, Integer lastPoint);
    List<Member> findByMonthPointRanking(int pageSize, Long lastId, Integer lastPoint);
}
