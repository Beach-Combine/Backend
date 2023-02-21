package beachcombine.backend.controller;

import beachcombine.backend.common.auth.PrincipalDetails;
import beachcombine.backend.dto.request.TrashcanSaveRequest;
import beachcombine.backend.dto.response.TrashcanResponse;
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

    // (지도) 쓰레기통 위치 조회
    @GetMapping("/maps/trashcans")
    public ResponseEntity<List<TrashcanResponse>> findTrashcanLocation(){

        List<TrashcanResponse> trashcanResponse = trashcanService.findCertifiedTrashcanCoords();

        return ResponseEntity.status(HttpStatus.OK).body(trashcanResponse);
    }

    // 쓰레기통 신고하기
    @PostMapping("/trashcans")
    public ResponseEntity<Long> saveTrashcan (@AuthenticationPrincipal PrincipalDetails userDetails,
                                              TrashcanSaveRequest request) throws IOException {

        Long trashcanId = trashcanService.saveTrashcan(userDetails.getMember().getId(), request);

        return ResponseEntity.status(HttpStatus.OK).body(trashcanId);
    }
}
