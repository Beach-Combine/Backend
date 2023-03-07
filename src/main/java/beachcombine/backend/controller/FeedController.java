package beachcombine.backend.controller;

import beachcombine.backend.common.auth.PrincipalDetails;
import beachcombine.backend.dto.request.FeedSaveRequest;
import beachcombine.backend.dto.response.FeedResponse;
import beachcombine.backend.dto.response.IdResponse;
import beachcombine.backend.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                                               FeedSaveRequest request) {

        Long feedId = feedService.saveFeed(userDetails.getMember().getId(), request, recordId);

        IdResponse response = IdResponse.builder()
                .id(feedId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 피드 목록 조회
    @GetMapping("")
    public ResponseEntity<List<FeedResponse>> getFeedList() {

        List<FeedResponse> response = feedService.getFeedList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 피드 삭제하기
    @DeleteMapping("{feedId}")
    public ResponseEntity<Void> deleteFeed(@AuthenticationPrincipal PrincipalDetails userDetails,
                                           @PathVariable("feedId") Long feedId) {

        feedService.deleteFeed(userDetails.getMember().getId(), feedId);

        return new ResponseEntity(HttpStatus.OK);
    }
}
