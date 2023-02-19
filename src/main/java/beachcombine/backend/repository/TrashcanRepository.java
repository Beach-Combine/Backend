package beachcombine.backend.repository;

import beachcombine.backend.common.entity.Coordinates;
import beachcombine.backend.domain.Trashcan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrashcanRepository extends JpaRepository<Trashcan, Long> {

    List<Coordinates> findCoordsByCertified(Boolean certified);
}
