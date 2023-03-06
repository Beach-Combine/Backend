package beachcombine.backend.controller;

import beachcombine.backend.common.auth.PrincipalDetails;
import beachcombine.backend.dto.request.FeedSaveRequest;
import beachcombine.backend.dto.request.RecordSaveRequest;
import beachcombine.backend.dto.response.IdResponse;
import beachcombine.backend.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("feeds")
public class FeedController {

    private final FeedService feedService;

    // 피드 기록하기
    @PostMapping("{recordId}")
    public ResponseEntity<IdResponse> saveFeed(@AuthenticationPrincipal PrincipalDetails userDetails,
                                     @PathVariable("recordId") Long recordId,
                                     FeedSaveRequest request) throws IOException {

        Long feedId = feedService.saveFeed(userDetails.getMember().getId(), request, recordId);

        IdResponse response = IdResponse.builder()
                .id(feedId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}