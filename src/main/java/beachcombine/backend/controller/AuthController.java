package beachcombine.backend.controller;

import beachcombine.backend.dto.request.AuthJoinRequest;
import beachcombine.backend.dto.request.AuthLoginRequest;
import beachcombine.backend.dto.response.AuthJoinResponse;
import beachcombine.backend.dto.response.AuthTokenResponse;
import beachcombine.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;

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
}