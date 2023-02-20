package beachcombine.backend.controller;

import beachcombine.backend.common.auth.PrincipalDetails;
import beachcombine.backend.dto.request.RecordSaveRequest;
import beachcombine.backend.dto.response.IdResponse;
import beachcombine.backend.service.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("records")
public class RecordController {

    private final RecordService recordService;

    // 청소 기록하기
    @PostMapping("")
    public ResponseEntity<IdResponse> saveRecord (@AuthenticationPrincipal PrincipalDetails userDetails,
                                                  RecordSaveRequest request) throws IOException {

        Long recordId = recordService.saveRecord(userDetails.getMember().getId(), request);

        IdResponse response = IdResponse.builder()
                .id(recordId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
