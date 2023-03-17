package beachcombine.backend.repository;

import beachcombine.backend.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long>, RecordRepositoryCustom {

    Record findTopByBeachIdOrderByCreatedDateDesc (Long beachId);
    List<Record> findAllByMemberId(Long memberId);
}
