package beachcombine.backend.controller;

import beachcombine.backend.dto.request.AuthGoogleLoginRequest;
import beachcombine.backend.dto.request.AuthJoinRequest;
import beachcombine.backend.dto.request.AuthLoginRequest;
import beachcombine.backend.dto.response.AuthRecreateTokenResponse;
import beachcombine.backend.dto.response.AuthTokenResponse;
import beachcombine.backend.dto.response.IdResponse;
import beachcombine.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;

    // 일반 회원가입 (테스트용)
    @PostMapping("join")
    public ResponseEntity<IdResponse> join(@RequestBody AuthJoinRequest request) {

        Long memberId = authService.saveMember(request);

        IdResponse response = IdResponse.builder()
                .id(memberId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 일반 로그인 (테스트용)
    @PostMapping("login")
    public ResponseEntity<AuthTokenResponse> login(@RequestBody AuthLoginRequest request) {

        AuthTokenResponse response = authService.login(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 구글 로그인
    @PostMapping("google")
    public ResponseEntity<AuthTokenResponse> googleLogin(@RequestBody AuthGoogleLoginRequest request) {

        AuthTokenResponse response = authService.googleLogin(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // accessToken 재발급
    @PostMapping("token")
    public ResponseEntity<AuthRecreateTokenResponse> refresh(@RequestBody Map<String, String> refreshToken){

        //Refresh Token 검증 및 AccessToken 재발급
        AuthRecreateTokenResponse response = authService.refresh(refreshToken.get("refreshToken"));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 로그아웃
    @PostMapping("logout")
    public ResponseEntity<Void> logout(@RequestBody Map<String, String> accessToken) {

        authService.logout(accessToken.get("accessToken"));

        return new ResponseEntity(HttpStatus.OK);
    }
}