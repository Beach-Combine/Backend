package beachcombine.backend.service;

import beachcombine.backend.domain.Giftcard;
import beachcombine.backend.dto.response.GiftcardResponse;
import beachcombine.backend.repository.GiftcardRepository;
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
    private final ImageService imageService;

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
}
