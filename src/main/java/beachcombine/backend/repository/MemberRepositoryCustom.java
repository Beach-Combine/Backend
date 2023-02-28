package beachcombine.backend.repository;

import beachcombine.backend.dto.response.MemberRankingResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberRepositoryCustom {

    List<MemberRankingResponse> findByTotalPointRanking(int pageSize, Long lastId, Integer lastPoint);
}
