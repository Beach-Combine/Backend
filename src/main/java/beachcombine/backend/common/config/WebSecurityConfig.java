package beachcombine.backend.common.config;

import beachcombine.backend.common.jwt.JwtAuthorizationFilter;
import beachcombine.backend.common.jwt.JwtUtils;
import beachcombine.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsConfig corsConfig;
    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // csrf 보안 토큰 disable처리.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 사용하지 x
                .and()
                .addFilter(corsConfig.corsFilter()) // @CrossOrigin(인증X), 시큐리티 필터에 등록(인증O)
                .formLogin().disable() // jwt 서버라서 아이디,비밀번호를 formLogin으로 안함
                .httpBasic().disable() // 매 요청마다 id, pwd 보내는 방식으로 인증하는 httpBasic 사용X
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), memberRepository, jwtUtils))
                .authorizeRequests() // 요청에 대한 사용권한 체크
                .antMatchers("/oauth2/*").permitAll()
                .anyRequest().permitAll();
    // .anyRequest().authenticated(); // 그외 나머지 요청은 인증 필요
    }
}