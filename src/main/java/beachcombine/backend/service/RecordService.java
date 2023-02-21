package beachcombine.backend.service;

import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.domain.Beach;
import beachcombine.backend.domain.Member;
import beachcombine.backend.domain.Record;
import beachcombine.backend.dto.request.RecordSaveRequest;
import beachcombine.backend.repository.BeachRepository;
import beachcombine.backend.repository.MemberRepository;
import beachcombine.backend.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RecordService {

    private final RecordRepository recordRepository;
    private final ImageService imageService;
    private final MemberRepository memberRepository;
    private final BeachRepository beachRepository;

    // 청소 기록하기
    public Long saveRecord(Long memberId, RecordSaveRequest request) throws IOException {

        // 예외 처리
        Member findMember = getMemberOrThrow(memberId);
        Beach findBeach = getBeachOrThrow(request.getBeachId());
        checkExistsImage(request);

        // 이미지 업로드
        String beforeUuid = imageService.uploadImage(request.getBeforeImage());
        String afterUuid = imageService.uploadImage(request.getAfterImage());

        Record record = Record.builder()
                .duration(request.getDuration())
                .distance(request.getDistance())
                .beforeImage(beforeUuid)
                .afterImage(afterUuid)
                .build();

        record.setMember(findMember);
        record.setBeach(findBeach);
        recordRepository.save(record);

        return record.getId();
    }

    // 예외 처리 - 존재하는 member인지
    private Member getMemberOrThrow(Long id) {

        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    // 예외 처리 - 존재하는 beach인지
    private Beach getBeachOrThrow(Long id) {

        return beachRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BEACH));
    }

    // 예외 처리 - 이미지 있는지
    private void checkExistsImage(RecordSaveRequest request) {

        if (request.getAfterImage().isEmpty() || request.getBeforeImage().isEmpty()) {
            throw new CustomException(ErrorCode.SHOULD_EXIST_IMAGE);
        }
    }

}
