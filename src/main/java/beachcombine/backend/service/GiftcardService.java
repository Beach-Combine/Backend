package beachcombine.backend.service;

import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.domain.Giftcard;
import beachcombine.backend.domain.Member;
import beachcombine.backend.domain.Purchase;
import beachcombine.backend.dto.response.GiftcardResponse;
import beachcombine.backend.repository.GiftcardRepository;
import beachcombine.backend.repository.MemberRepository;
import beachcombine.backend.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GiftcardService {

    private final GiftcardRepository giftcardRepository;
    private final MemberRepository memberRepository;
    private final PurchaseRepository purchaseRepository;
    private final ImageService imageService;

    // 카드 목록 조회
    public List<GiftcardResponse> getGiftcardList() {

        List<Giftcard> giftcardList = giftcardRepository.findAll();
        List<GiftcardResponse> response = new ArrayList<>();

        for (Giftcard giftcard : giftcardList) {
            String imageUrl = imageService.processImage(giftcard.getStore().getImage());
            response.add(GiftcardResponse.builder()
                            .id(giftcard.getId())
                            .name(giftcard.getStore().getName())
                            .location(giftcard.getStore().getLocation())
                            .image(imageUrl)
                            .cost(giftcard.getCost())
                            .build());
        }

        return response;
    }

    // 카드 구매하기
    public Long purchaseGiftcard(Long memberId, Long giftcardId) {

        Member findMember = getMemberOrThrow(memberId);
        Giftcard findGiftcard = getGiftcardOrThrow(giftcardId);

        Purchase purchase = Purchase.builder()
                .giftcard(findGiftcard)
                .build();
        purchase.setMember(findMember);
        purchaseRepository.save(purchase);

        if(!findMember.updatePurchasePoint(findGiftcard.getCost())) {
            throw new CustomException(ErrorCode.NOT_FOUND_POINT);
        }

        return purchase.getId();
    }

    // 카드 구매 목록 조회
    public List<GiftcardResponse> getPurchasedGiftcardList(Long memberId) {

        List<Giftcard> giftcardList = giftcardRepository.findByMember(memberId);
        List<GiftcardResponse> response = new ArrayList<>();

        for (Giftcard giftcard : giftcardList) {
            String imageUrl = imageService.processImage(giftcard.getStore().getImage());
            response.add(GiftcardResponse.builder()
                    .id(giftcard.getId())
                    .name(giftcard.getStore().getName())
                    .location(giftcard.getStore().getLocation())
                    .image(imageUrl)
                    .cost(giftcard.getCost())
                    .build());
        }

        return response;
    }

    // 예외 처리 - 존재하는 member 인가
    private Member getMemberOrThrow(Long id) {

        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    // 예외 처리 - 존재하는 beach 인가
    private Giftcard getGiftcardOrThrow(Long id) {

        return giftcardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GIFTCARD));
    }
}
