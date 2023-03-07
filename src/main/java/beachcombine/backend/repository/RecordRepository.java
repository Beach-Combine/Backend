package beachcombine.backend.repository;

import beachcombine.backend.domain.Record;
import beachcombine.backend.dto.response.RecordResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long>, RecordRepositoryCustom {

    Record findTopByBeachIdOrderByCreatedDateDesc (Long beachId);
    List<Record> findAllByMemberId(Long memberId);
    List<Record> findAllByMemberIdAndBeachIdOrderByCreatedDateDesc(Long memberId, Long BeachId);
}
