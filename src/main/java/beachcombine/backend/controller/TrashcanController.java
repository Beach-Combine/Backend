package beachcombine.backend.controller;

import beachcombine.backend.dto.request.MemberUpdateRequest;
import beachcombine.backend.dto.request.TrashcanLocationRequest;
import beachcombine.backend.dto.response.TrashcanResponse;
import beachcombine.backend.service.RecordService;
import beachcombine.backend.service.TrashcanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
