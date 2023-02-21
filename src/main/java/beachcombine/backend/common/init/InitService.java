package beachcombine.backend.common.init;

import beachcombine.backend.domain.Beach;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class InitService {

    private final EntityManager em;

    @Transactional
    public void initDatabase() {

        Beach beach1 = Beach.builder()
                .id(1L)
                .name("Haeundae Beach")
                .lat(BigDecimal.valueOf(35.158645))
                .lng(BigDecimal.valueOf(129.160920))
                .badgeImage("imageUrl")
                .build();

        em.merge(beach1);
    }
}
