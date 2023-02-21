package beachcombine.backend.controller;

import beachcombine.backend.dto.response.BeachBadgeResponse;
import beachcombine.backend.service.BeachService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("beaches")
public class BeachController {

    private final BeachService beachService;

    // 해변 뱃지 조회
    @GetMapping("badge/{beachId}")
    public ResponseEntity<BeachBadgeResponse> findBadgeImage(@PathVariable("beachId") Long beachId) {

        BeachBadgeResponse beachBadgeResponse = beachService.findBadgeImage(beachId);
        return ResponseEntity.status(HttpStatus.OK).body(beachBadgeResponse);
    }
}
