package beachcombine.backend.common.jwt;

import beachcombine.backend.common.jwt.dto.TokenDto;
import beachcombine.backend.domain.Member;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.secret_refresh}")
    private String refreshSecretKey;

    // 토큰 생성
    public TokenDto createToken(Member member) {

        String accessToken = JWT.create()
                .withSubject(member.getLoginId())
                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
                .withClaim("id", member.getId())
                .withClaim("username", member.getLoginId())
                .withClaim("role", member.getRole())
                .sign(Algorithm.HMAC512(secretKey));

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

    // Access 토큰 재생성
    public String recreateAccessToken(Member member) {

        String accessToken = JWT.create()
                .withSubject(member.getLoginId())
                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
                .withClaim("id", member.getId())
                .withClaim("username", member.getLoginId())
                .withClaim("role", member.getRole())
                .sign(Algorithm.HMAC512(secretKey));

        return accessToken;
    }

    public String getUsernameFromAccessToken(String token) {

        DecodedJWT jwt = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
        return jwt.getClaim("username").asString();
    }

    public String getUsernameFromRefreshToken(String token) {

        DecodedJWT jwt = JWT.require(Algorithm.HMAC512(refreshSecretKey)).build().verify(token);
        return jwt.getClaim("username").asString();
    }

    // access 토큰의 유효성 + 만료일자 확인 -> 유효하면 true 리턴
    public Boolean validateAccessToken(String token) {

        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (TokenExpiredException e) {
            throw new JwtException("TOKEN_EXPIRED");
        } catch (JWTVerificationException e) {
            throw new JwtException("TOKEN_INVALID");
        }
    }

    // refresh 토큰의 유효성 + 만료일자 확인 -> 유효하면 true 리턴
    public Boolean validateRefreshToken(String token) {

        try {
            JWT.require(Algorithm.HMAC512(refreshSecretKey)).build().verify(token);
            return true;
        } catch (TokenExpiredException e) {
            throw new JwtException("TOKEN_EXPIRED");
        } catch (JWTVerificationException e) {
            throw new JwtException("TOKEN_INVALID");
        }
    }
}
