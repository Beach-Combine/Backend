package beachcombine.backend.repository;

import beachcombine.backend.domain.Trashcan;
import beachcombine.backend.dto.response.TrashcanResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrashcanRepository extends JpaRepository<Trashcan, Long> {

    List<TrashcanResponse> findByCertified(Boolean certified);
}
