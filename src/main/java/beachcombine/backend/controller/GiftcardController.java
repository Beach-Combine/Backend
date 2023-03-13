package beachcombine.backend.controller;

import beachcombine.backend.common.auth.PrincipalDetails;
import beachcombine.backend.dto.response.GiftcardResponse;
import beachcombine.backend.dto.response.IdResponse;
import beachcombine.backend.service.GiftcardService;
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
@RequestMapping("giftcards")
public class GiftcardController {

    private final GiftcardService giftcardService;

    // 카드 목록 조회
    @GetMapping("")
    public ResponseEntity<List<GiftcardResponse>> getGiftcardList() {

        List<GiftcardResponse> response = giftcardService.getGiftcardList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 카드 구매하기
    @PostMapping("{giftcardId}/purchase")
    public ResponseEntity<IdResponse> purchaseGiftcard(@AuthenticationPrincipal PrincipalDetails userDetails,
                                                       @PathVariable("giftcardId") Long giftcardId) {

        Long purchaseId = giftcardService.purchaseGiftcard(userDetails.getMember().getId(), giftcardId);

        IdResponse response = IdResponse.builder()
                .id(purchaseId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 카드 구매 목록 조회


}
