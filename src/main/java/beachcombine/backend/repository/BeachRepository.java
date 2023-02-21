package beachcombine.backend.repository;

import beachcombine.backend.domain.Beach;
import beachcombine.backend.dto.response.BeachMarkerResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BeachRepository extends JpaRepository<Beach, Long> {
}
