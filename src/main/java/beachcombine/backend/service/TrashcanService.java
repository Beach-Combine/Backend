package beachcombine.backend.service;

import beachcombine.backend.common.entity.Coordinates;
import beachcombine.backend.domain.Trashcan;
import beachcombine.backend.dto.response.TrashcanResponse;
import beachcombine.backend.repository.TrashcanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TrashcanService {

    private final TrashcanRepository trashcanRepository;

    // (지도) 쓰레기통 위치 조회
    @Transactional(readOnly = true)
    public TrashcanResponse findCertifiedTrashcanCoords() {

        List<Coordinates> result = trashcanRepository.findCoordsByCertified(true); //  인증된(certified=true) 쓰레기통들의 좌표 반환
        return TrashcanResponse.builder()
                .trashcans(result)
                .build();
    }
}
