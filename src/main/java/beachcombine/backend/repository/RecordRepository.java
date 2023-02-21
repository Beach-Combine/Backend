package beachcombine.backend.repository;

import beachcombine.backend.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {

    Record findTopByBeachIdOrderByCreatedDateDesc (Long beachId);
}
