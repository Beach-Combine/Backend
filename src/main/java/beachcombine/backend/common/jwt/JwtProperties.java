package beachcombine.backend.common.jwt;

import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;

public interface JwtProperties {

    String SECRET = "beach4321"; // 우리 서버만 알고 있는 비밀값
    Long ACCESS_TOKEN_EXPIRATION_TIME = Duration.ofMinutes(30).toMillis(); // 30분
    Long REFRESH_TOKEN_EXPIRATION_TIME = Duration.ofDays(14).toMillis();  // 2주
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
