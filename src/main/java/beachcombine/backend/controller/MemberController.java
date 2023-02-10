package beachcombine.backend.controller;

import beachcombine.backend.dto.member.MemberResponse;
import beachcombine.backend.dto.member.MemberUpdateRequest;
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
public class MemberController {

    private final MemberService memberService;

    // 회원 정보 조회
    @GetMapping("/members")
    public ResponseEntity<MemberResponse> findMemberInfo() {
        // @AuthenticationPrincipal CustomUserDetails userDetails

//        MemberResponse memberResponse = memberService.findMemberInfo(userDetails.getMember().getId());
        MemberResponse memberResponse = memberService.findMemberInfo(1);

        return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
    }

    // 회원 정보 수정
    @PatchMapping("/members")
    public ResponseEntity<Void> updateMemberInfo(@RequestBody MemberUpdateRequest dto)  {
        memberService.updateMemberInfo(Long.valueOf(1), dto);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 닉네임 중복확인
    @GetMapping("/members/nickname-check/{nickname}")
    public ResponseEntity<Void> checkNicknameDuplicate(@PathVariable("nickname") String nickname){
        memberService.checkNicknameDuplicate(nickname);
        return new ResponseEntity(HttpStatus.OK);
    }

}
