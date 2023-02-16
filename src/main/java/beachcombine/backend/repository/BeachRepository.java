package beachcombine.backend.repository;

import beachcombine.backend.domain.Beach;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeachRepository extends JpaRepository<Beach, Long> {
}
