package beachcombine.backend.controller;

import beachcombine.backend.common.oauth.provider.GoogleUser;
import beachcombine.backend.common.oauth.provider.OAuthUserInfo;
import beachcombine.backend.domain.Member;
import beachcombine.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("oauth")
public class JwtController {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("jwt/google")
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

        // 구현 예정
        return "";
    }
}