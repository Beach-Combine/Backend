package beachcombine.backend.repository;

import beachcombine.backend.domain.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    List<Feed> findAllByOrderByCreatedDateDesc();
}
