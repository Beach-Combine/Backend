package beachcombine.backend.common.jwt.provider;

import beachcombine.backend.common.jwt.JwtProperties;
import beachcombine.backend.common.jwt.dto.TokenDto;
import beachcombine.backend.domain.Member;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.secret_refresh}")
    private String refreshSecretKey;

    private final UserDetailsService userDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public TokenDto createToken(Member member) {

        String accessToken = JWT.create()
                .withSubject(member.getLoginId())
                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME)) // 시간 제대로 동작하는지 확인 필요함
                .withClaim("id", member.getId())
                .withClaim("username", member.getLoginId())
                .withClaim("role", member.getRole())
                .sign(Algorithm.HMAC512(secretKey)); // Base64로 인코딩 굳이 필요한지, 없어도 되는지 확인 필요함

        String refreshToken = JWT.create()
                .withSubject(member.getLoginId())
                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME))
                .withClaim("id", member.getId())
                .withClaim("username", member.getLoginId())
                .withClaim("role", member.getRole())
                .sign(Algorithm.HMAC512(refreshSecretKey));

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .key(member.getLoginId()).build();
    }
}
