package beachcombine.backend.controller;

import beachcombine.backend.dto.response.BeachBadgeResponse;
import beachcombine.backend.dto.response.BeachMarkerResponse;
import beachcombine.backend.service.BeachService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BeachController {

    private final BeachService beachService;

    // 해변 뱃지 조회
    @GetMapping("/beaches/badge/{beachId}")
    public ResponseEntity<BeachBadgeResponse> findBadgeImage(@PathVariable("beachId") Long beachId) {

        BeachBadgeResponse beachBadgeResponse = beachService.findBadgeImage(beachId);
        return ResponseEntity.status(HttpStatus.OK).body(beachBadgeResponse);
    }

    // (지도) 전체 해변 위치 조회
    @GetMapping("/beaches/maps")
    public ResponseEntity<List<BeachMarkerResponse>> findBeachMarkers() {

        List<BeachMarkerResponse> beachMarkerResponse = beachService.findBeachMarkers();
        return ResponseEntity.status(HttpStatus.OK).body(beachMarkerResponse);
    }
}
