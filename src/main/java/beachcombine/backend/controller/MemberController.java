package beachcombine.backend.controller;

import beachcombine.backend.common.auth.PrincipalDetails;
import beachcombine.backend.dto.response.MemberResponse;
import beachcombine.backend.dto.request.MemberUpdateRequest;
import beachcombine.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("members")
public class MemberController {

    private final MemberService memberService;

    // 회원 정보 조회
    @GetMapping("")
    public ResponseEntity<MemberResponse> findMemberInfo(@AuthenticationPrincipal PrincipalDetails userDetails) {

        MemberResponse memberResponse = memberService.findMemberInfo(userDetails.getMember().getId());

        return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
    }

    // 회원 정보 수정
    @PatchMapping("")
    public ResponseEntity<Void> updateMemberInfo(@AuthenticationPrincipal PrincipalDetails userDetails, @RequestBody MemberUpdateRequest dto)  {

        memberService.updateMemberInfo(userDetails.getMember().getId(), dto);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 닉네임 중복확인
    @GetMapping("nickname-check/{nickname}")
    public ResponseEntity<Void> checkNicknameDuplicate(@PathVariable("nickname") String nickname){

        memberService.checkNicknameDuplicate(nickname);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 프로필 공개여부 지정
    @PatchMapping("profile-public/{option}")
    public ResponseEntity<Void> UpdateProfilePublic(@AuthenticationPrincipal PrincipalDetails userDetails, @PathVariable("option") Boolean option){

        memberService.UpdateProfilePublic(userDetails.getMember().getId(), option);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 포인트 받기
    @PatchMapping("point/{option}")
    public ResponseEntity<Void> UpdateMemberPoint(@AuthenticationPrincipal PrincipalDetails userDetails, @PathVariable("option") int option) {

        memberService.UpdateMemberPoint(userDetails.getMember().getId(), option);
        return new ResponseEntity(HttpStatus.OK);
    }
}
