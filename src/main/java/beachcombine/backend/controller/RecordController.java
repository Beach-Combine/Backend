package beachcombine.backend.controller;

import beachcombine.backend.common.auth.PrincipalDetails;
import beachcombine.backend.dto.request.RecordSaveRequest;
import beachcombine.backend.dto.response.IdResponse;
import beachcombine.backend.dto.response.RecordResponse;
import beachcombine.backend.service.RecordService;
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
@RequestMapping("records")
public class RecordController {

    private final RecordService recordService;

    // 청소 기록하기
    @PostMapping("{beachId}")
    public ResponseEntity<IdResponse> saveRecord (@AuthenticationPrincipal PrincipalDetails userDetails,
                                                  @PathVariable("beachId") Long beachId,
                                                  RecordSaveRequest request) throws IOException {

        Long recordId = recordService.saveRecord(userDetails.getMember().getId(), request, beachId);

        IdResponse response = IdResponse.builder()
                .id(recordId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 청소기록 목록 조회
    @GetMapping("")
    public ResponseEntity<List<RecordResponse>> getRecordList(@AuthenticationPrincipal PrincipalDetails userDetails) {

        List<RecordResponse> response = recordService.getRecordList(userDetails.getMember().getId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
