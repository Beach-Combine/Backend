package beachcombine.backend.controller;

import beachcombine.backend.common.jwt.JwtProperties;
import beachcombine.backend.common.oauth.provider.GoogleUser;
import beachcombine.backend.common.oauth.provider.OAuthUserInfo;
import beachcombine.backend.domain.Member;
import beachcombine.backend.dto.request.AuthJoinRequest;
import beachcombine.backend.dto.request.AuthLoginRequest;
import beachcombine.backend.dto.response.AuthJoinResponse;
import beachcombine.backend.dto.response.AuthTokenResponse;
import beachcombine.backend.repository.MemberRepository;
import beachcombine.backend.service.AuthService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("auth")
public class AuthController {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthService authService;

    // 일반 회원가입 (테스트용)
    @PostMapping("join")
    public ResponseEntity<AuthJoinResponse> join(@RequestBody AuthJoinRequest authJoinRequest) {

        AuthJoinResponse authJoinResponse = authService.saveMember(authJoinRequest);

        return ResponseEntity.status(HttpStatus.OK).body(authJoinResponse);
    }

    // 일반 로그인 (테스트용)
    @PostMapping("/login")
    public ResponseEntity<AuthTokenResponse> login(@RequestBody AuthLoginRequest authLoginRequest) {

        AuthTokenResponse authJoinResponse = authService.login(authLoginRequest);

        return ResponseEntity.status(HttpStatus.OK).body(authJoinResponse);
    }

    // 구글 로그인
    @PostMapping("login/google")
    public String jwtCreate(@RequestBody Map<String, Object> data) {
        System.out.println("jwtCreate 실행됨");
        System.out.println(data.get("profileObj"));

        OAuthUserInfo googleUser = new GoogleUser((Map<String, Object>)data.get("profileObj"));

        Member memberEntity = memberRepository.findByLoginId(googleUser.getProvider()+"_"+googleUser.getProviderId());

        if(memberEntity == null) {
            Member memberRequest = Member.builder()
                    .loginId(googleUser.getProvider()+"_"+googleUser.getProviderId())
                    .password(bCryptPasswordEncoder.encode("beach4321"))
                    .email(googleUser.getEmail())
                    .provider(googleUser.getProvider())
                    .providerId(googleUser.getProviderId())
                    .role("ROLE_USER")
                    .build();

            memberEntity = memberRepository.save(memberRequest);
        }

        String jwtToken = JWT.create()
                .withSubject(memberEntity.getLoginId())
                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
                .withClaim("id", memberEntity.getId())
                .withClaim("username", memberEntity.getLoginId())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        return jwtToken;
    }
}