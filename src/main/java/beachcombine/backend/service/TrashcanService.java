package beachcombine.backend.service;

import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.domain.Member;
import beachcombine.backend.domain.Trashcan;
import beachcombine.backend.dto.request.TrashcanSaveRequest;
import beachcombine.backend.dto.response.TrashcanMarkerResponse;
import beachcombine.backend.repository.MemberRepository;
import beachcombine.backend.repository.TrashcanRepository;
import beachcombine.backend.util.GeocodingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TrashcanService {

    private final TrashcanRepository trashcanRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final GeocodingUtil geocodingUtil;

    // (지도) 인증된 쓰레기통 위치 조회
    @Transactional(readOnly = true)
    public List<TrashcanMarkerResponse> findCertifiedTrashcanMarkers() {

        List<Trashcan> findTrashcanList  = trashcanRepository.findByIsCertified(true); //  인증된(isCertified=true) 쓰레기통들의 좌표 반환
        List<TrashcanMarkerResponse> responseList = findTrashcanList.stream()
                .map(m -> TrashcanMarkerResponse.builder()
                        .id(m.getId())
                        .lat(String.valueOf(m.getLat()))
                        .lng(String.valueOf(m.getLng()))
                        .address(m.getAddress())
                        .build())
                .collect(Collectors.toList());
        
        return responseList;
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

    // 쓰레기통 인증하기
    public void certifyTrashcan(Long memberId, Long trashcanId) {

        // 관리자 인증
        Member member = getMemberOrThrow(memberId);
        if(!member.getRole().equals("ROLE_ADMIN")){
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        Trashcan findTrashcan = getTrashcanOrThrow(trashcanId);
        if(findTrashcan.getIsCertified()) {
            throw new CustomException(ErrorCode.ALREADY_CERTIFIED_TRASHCAN);
        }

        // 좌표 -> 상세주소 변환
        String address = geocodingUtil.getAddressByCoords(findTrashcan.getLat(), findTrashcan.getLng());
        findTrashcan.certifyTrashcan(address);

        // 추가 포인트 70점 지급
        Member findMember = findTrashcan.getMember();
        findMember.updateMemberPoint(2);
    }

    // 쓰레기통 인증 요청 목록 조회
    public List<TrashcanMarkerResponse> findUncertifiedTrashcans(Long memberId) {

        // 관리자 인증
        Member member = getMemberOrThrow(memberId);
        if(!member.getRole().equals("ROLE_ADMIN")){
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        List<Trashcan> findTrashcanList  = trashcanRepository.findByIsCertified(false);
        List<TrashcanMarkerResponse> responseList = findTrashcanList.stream()
                .map(m -> TrashcanMarkerResponse.builder()
                        .id(m.getId())
                        .lat(String.valueOf(m.getLat()))
                        .lng(String.valueOf(m.getLng()))
                        .address(m.getAddress())
                        .build())
                .collect(Collectors.toList());

        return responseList;
    }

    // 예외 처리 - 존재하는 member인지
    private Member getMemberOrThrow(Long memberId) {

        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    // 예외 처리 - 존재하는 trashcan인지
    private Trashcan getTrashcanOrThrow(Long trashcanId) {

        return trashcanRepository.findById(trashcanId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TRASHCAN));
    }

    // 예외 처리 - 이미지 있는지
    private void checkExistsImage(MultipartFile image) {

        if (image.isEmpty()) {
            throw new CustomException(ErrorCode.SHOULD_EXIST_IMAGE);
        }
    }
}
