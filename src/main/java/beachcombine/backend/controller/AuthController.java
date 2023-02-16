package beachcombine.backend.controller;

import beachcombine.backend.common.jwt.dto.TokenDto;
import beachcombine.backend.dto.request.AuthJoinRequest;
import beachcombine.backend.dto.request.AuthLoginRequest;
import beachcombine.backend.dto.response.AuthJoinResponse;
import beachcombine.backend.dto.response.AuthRecreateTokenResponse;
import beachcombine.backend.dto.response.AuthTokenResponse;
import beachcombine.backend.service.AuthService;
import beachcombine.backend.service.RefreshTokenService;
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
    private final RefreshTokenService refreshTokenService;

    // 일반 회원가입 (테스트용)
    @PostMapping("join")
    public ResponseEntity<AuthJoinResponse> join(@RequestBody AuthJoinRequest authJoinRequest) {

        AuthJoinResponse authJoinResponse = authService.saveMember(authJoinRequest);

        return ResponseEntity.status(HttpStatus.OK).body(authJoinResponse);
    }

    // 일반 로그인 (테스트용)
    @PostMapping("login")
    public ResponseEntity<AuthTokenResponse> login(@RequestBody AuthLoginRequest authLoginRequest) {

        AuthTokenResponse authTokenResponse = authService.login(authLoginRequest);

        return ResponseEntity.status(HttpStatus.OK).body(authTokenResponse);
    }

    // 구글 로그인
    @PostMapping("google")
    public ResponseEntity<AuthTokenResponse> googleLogin(@RequestBody Map<String, Object> data) {

        System.out.println("jwtCreate 실행됨");
        System.out.println(data.get("profileObj"));

        AuthTokenResponse authTokenResponse = authService.googleLogin(data);

        return ResponseEntity.status(HttpStatus.OK).body(authTokenResponse);
    }

    // accessToken 재발급
    @PostMapping("token")
    public ResponseEntity<AuthRecreateTokenResponse> refresh(@RequestBody Map<String, String> refreshToken){

        //Refresh Token 검증 및 AccessToken 재발급
        AuthRecreateTokenResponse authRecreateTokenResponse = authService.refresh(refreshToken.get("refreshToken"));

        return ResponseEntity.status(HttpStatus.OK).body(authRecreateTokenResponse);
    }
}