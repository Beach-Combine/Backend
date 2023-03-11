package beachcombine.backend.controller;

import beachcombine.backend.common.auth.PrincipalDetails;
import beachcombine.backend.dto.response.IdResponse;
import beachcombine.backend.dto.response.MemberPointResponse;
import beachcombine.backend.dto.response.MemberRankingResponse;
import beachcombine.backend.dto.response.MemberResponse;
import beachcombine.backend.dto.request.MemberUpdateRequest;
import beachcombine.backend.repository.MemberRepository;
import beachcombine.backend.service.MemberService;
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
@RequestMapping("members")
public class MemberController {

    private final MemberService memberService;

    // 회원 정보 조회
    @GetMapping("")
    public ResponseEntity<MemberResponse> getMember(@AuthenticationPrincipal PrincipalDetails userDetails) {

        MemberResponse response = memberService.getMember(userDetails.getMember().getId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 회원 정보 수정
    @PatchMapping("")
    public ResponseEntity<Void> updateMember(@AuthenticationPrincipal PrincipalDetails userDetails, MemberUpdateRequest dto) throws IOException {

        memberService.updateMember(userDetails.getMember().getId(), dto);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 닉네임 중복확인
    @GetMapping("nickname-check/{nickname}")
    public ResponseEntity<Void> checkNicknameDuplicate(@PathVariable("nickname") String nickname) {

        memberService.checkNicknameDuplicate(nickname);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 프로필 공개여부 지정
    @PatchMapping("profile-public/{option}")
    public ResponseEntity<Void> updateProfilePublic(@AuthenticationPrincipal PrincipalDetails userDetails, @PathVariable("option") Boolean option) {

        memberService.updateProfilePublic(userDetails.getMember().getId(), option);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 포인트 받기
    @PatchMapping("point/{option}")
    public ResponseEntity<Void> updateMemberPoint(@AuthenticationPrincipal PrincipalDetails userDetails, @PathVariable("option") int option) {

        memberService.updateMemberPoint(userDetails.getMember().getId(), option);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 랭킹 조회
    @GetMapping("ranking")
    public ResponseEntity<List<MemberRankingResponse>> getMemberRanking(@RequestParam String range,
                                                                        @RequestParam int pageSize,
                                                                        @RequestParam(required = false) Long lastId,
                                                                        @RequestParam(required = false) Integer lastPoint) {

        List<MemberRankingResponse> response = memberService.getMemberRanking(range, pageSize, lastId, lastPoint);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 회원-피드 좋아요 관계 등록 (피드 좋아요하기)
    @PostMapping("preferred-feeds/{feedId}")
    public ResponseEntity<IdResponse> likeFeed(@AuthenticationPrincipal PrincipalDetails userDetails,
                                         @PathVariable("feedId") Long feedId) {

        Long memberPreferredFeedId = memberService.likeFeed(userDetails.getMember().getId(), feedId);

        IdResponse response = IdResponse.builder()
                .id(memberPreferredFeedId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 회원-피드 좋아요 관계 취소 (피드 좋아요 취소하기)
    @DeleteMapping("preferred-feeds/{feedId}")
    public ResponseEntity<Void> deleteLikeFeed(@AuthenticationPrincipal PrincipalDetails userDetails,
                                               @PathVariable("feedId") Long feedId) {

        memberService.deleteLikeFeed(userDetails.getMember().getId(), feedId);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 회원 전체 포인트 조회
    @GetMapping("point")
    public ResponseEntity<MemberPointResponse> getMemberPoint(@AuthenticationPrincipal PrincipalDetails userDetails) {

        Integer totalPoint = memberService.getMemberPoint(userDetails.getMember().getId());

        MemberPointResponse response = MemberPointResponse.builder()
                .totalPoint(totalPoint)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
