package beachcombine.backend.controller;

import beachcombine.backend.common.auth.PrincipalDetails;
import beachcombine.backend.dto.request.TrashcanSaveRequest;
import beachcombine.backend.dto.response.IdResponse;
import beachcombine.backend.dto.response.TrashcanMarkerResponse;
import beachcombine.backend.service.TrashcanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TrashcanController {

    private final TrashcanService trashcanService;

    // (지도) 인증된 쓰레기통 위치 조회
    @GetMapping("/trashcans/map")
    public ResponseEntity<List<TrashcanMarkerResponse>> findCertifiedTrashcanMarkers(){

        List<TrashcanMarkerResponse> response = trashcanService.findCertifiedTrashcanMarkers();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 쓰레기통 신고하기
    @PostMapping("/trashcans")
    public ResponseEntity<IdResponse> saveTrashcan (@AuthenticationPrincipal PrincipalDetails userDetails,
                                                    TrashcanSaveRequest request) throws IOException {

        Long trashcanId = trashcanService.saveTrashcan(userDetails.getMember().getId(), request);

        IdResponse response = IdResponse.builder()
                .id(trashcanId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 쓰레기통 인증 요청 목록 조회
    @GetMapping("/admin/trashcans/certification-requests")
    public ResponseEntity<List<TrashcanMarkerResponse>> findUncertifiedTrashcans(@AuthenticationPrincipal PrincipalDetails userDetails) {

        List<TrashcanMarkerResponse> response = trashcanService.findUncertifiedTrashcans(userDetails.getMember().getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
