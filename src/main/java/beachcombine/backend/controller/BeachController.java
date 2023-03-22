package beachcombine.backend.controller;

import beachcombine.backend.dto.request.NearBeachRequest;
import beachcombine.backend.dto.response.BeachMarkerResponse;
import beachcombine.backend.dto.response.BeachLatestRecordResponse;
import beachcombine.backend.service.BeachService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("beaches")
public class BeachController {

    private final BeachService beachService;

    // 해변 상세조회 (최근 청소 기록 제공)
    @GetMapping("{beachId}")
    public ResponseEntity<BeachLatestRecordResponse> findLatestRecord(@PathVariable("beachId") Long beachId) {

        BeachLatestRecordResponse response = beachService.findLatestRecord(beachId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // (지도) 전체 해변 위치 조회
    @GetMapping("/map")
    public ResponseEntity<List<BeachMarkerResponse>> findBeachMarkers() {

        List<BeachMarkerResponse> beachMarkerResponse = beachService.findBeachMarkers();
        return ResponseEntity.status(HttpStatus.OK).body(beachMarkerResponse);
    }

    // 해변 근처 인증하기
    @GetMapping("{beachId}/range")
    public ResponseEntity<Void> verifyNearBeach(@PathVariable("beachId") Long beachId, @Valid NearBeachRequest dto) {

        beachService.verifyNearBeach(beachId, new BigDecimal(dto.getLat()), new BigDecimal(dto.getLng()));
        return new ResponseEntity(HttpStatus.OK);
    }
}
