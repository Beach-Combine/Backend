package beachcombine.backend.service;

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
    public List<TrashcanResponse> findCertifiedTrashcanCoords() {

        List<TrashcanResponse> trashcanResponseList  = trashcanRepository.findByCertified(true); //  인증된(certified=true) 쓰레기통들의 좌표 반환
        return trashcanResponseList;
    }
}
