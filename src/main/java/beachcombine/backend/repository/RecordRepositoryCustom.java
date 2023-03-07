package beachcombine.backend.repository;

import beachcombine.backend.domain.Beach;
import beachcombine.backend.domain.Record;

import java.util.List;

public interface RecordRepositoryCustom {

    List<Beach> findBeachList(Long memberId);
}
