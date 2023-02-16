package beachcombine.backend.repository;

import beachcombine.backend.domain.Trashcan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrashcanRepository extends JpaRepository<Trashcan, Long> {
}
