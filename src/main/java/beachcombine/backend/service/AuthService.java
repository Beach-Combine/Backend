package beachcombine.backend.service;

import beachcombine.backend.domain.Member;
import beachcombine.backend.dto.request.AuthJoinRequest;
import beachcombine.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 일반 회원가입 (테스트용)
    @Transactional
    public Member saveMember(AuthJoinRequest dto) {

        Member member = Member.builder()
                .loginId(dto.getLoginId())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);

        return member;
    }
}
