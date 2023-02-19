package beachcombine.backend.service;

import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.domain.Beach;
import beachcombine.backend.dto.response.BeachBadgeResponse;
import beachcombine.backend.repository.BeachRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BeachService {

    private final BeachRepository beachRepository;

    // 해변 뱃지 조회
    @Transactional(readOnly = true)
    public BeachBadgeResponse findBadgeImage(Long beachId) {

        Beach findBeach = beachRepository.findById(beachId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BEACH));

        return findBeach.getBeachBadgeImage();
    }
}
