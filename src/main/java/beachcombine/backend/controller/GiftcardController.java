package beachcombine.backend.controller;

import beachcombine.backend.dto.response.GiftcardResponse;
import beachcombine.backend.service.GiftcardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
