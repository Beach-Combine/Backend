package beachcombine.backend.repository;

import beachcombine.backend.domain.Giftcard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftcardRepository extends JpaRepository<Giftcard, Long>, GiftcardRepositoryCustom {
}