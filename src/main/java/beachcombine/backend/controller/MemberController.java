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
    public ResponseEntity<Void> updateMemberInfo(@RequestBody MemberUpdateRequest dto)  {
        memberService.updateMemberInfo(Long.valueOf(1), dto);

        return new ResponseEntity(HttpStatus.OK);
    }
}
