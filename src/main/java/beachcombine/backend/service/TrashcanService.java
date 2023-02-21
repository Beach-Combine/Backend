package beachcombine.backend.service;

import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.domain.Member;
import beachcombine.backend.domain.Trashcan;
import beachcombine.backend.dto.request.TrashcanSaveRequest;
import beachcombine.backend.dto.response.TrashcanResponse;
import beachcombine.backend.repository.MemberRepository;
import beachcombine.backend.repository.TrashcanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TrashcanService {

    private final TrashcanRepository trashcanRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    // (지도) 쓰레기통 위치 조회
    @Transactional(readOnly = true)
    public List<TrashcanResponse> findCertifiedTrashcanCoords() {

        List<TrashcanResponse> trashcanResponseList  = trashcanRepository.findByIsCertified(true); //  인증된(isCertified=true) 쓰레기통들의 좌표 반환
        
        return trashcanResponseList;
    }

    // 쓰레기통 신고하기
    public Long saveTrashcan(Long memberId, TrashcanSaveRequest request) throws IOException {

        // 예외 처리
        Member findMember = getMemberOrThrow(memberId);
        checkExistsImage(request.getImage());

        // 이미지 업로드
        String uuid = imageService.uploadImage(request.getImage());

        Trashcan trashcan = Trashcan.builder()
                .lat(request.getLat())
                .lng(request.getLng())
                .image(uuid)
                .isCertified(false)
                .isAddedByUser(true)
                .member(findMember)
                .build();

        trashcanRepository.save(trashcan);

        return trashcan.getId();
    }

    // 예외 처리 - 존재하는 member인지
    private Member getMemberOrThrow(Long id) {

        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    // 예외 처리 - 이미지 있는지
    private void checkExistsImage(MultipartFile image) {

        if (image.isEmpty()) {
            throw new CustomException(ErrorCode.SHOULD_EXIST_IMAGE);
        }
    }
}
