package beachcombine.backend.service;

import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.common.jwt.dto.TokenDto;
import beachcombine.backend.common.jwt.provider.JwtTokenProvider;
import beachcombine.backend.domain.Member;
import beachcombine.backend.dto.request.AuthJoinRequest;
import beachcombine.backend.dto.request.AuthLoginRequest;
import beachcombine.backend.dto.response.AuthJoinResponse;
import beachcombine.backend.dto.response.AuthTokenResponse;
import beachcombine.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    // 일반 회원가입 (테스트용)
    public AuthJoinResponse saveMember(AuthJoinRequest requestDto) {

        Member member = Member.builder()
                .loginId(requestDto.getLoginId())
                .password(bCryptPasswordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .nickname(requestDto.getNickname())
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);

        AuthJoinResponse responseDto = AuthJoinResponse.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();

        return responseDto;
    }

    // 일반 로그인 (테스트용)
    public AuthTokenResponse login(AuthLoginRequest requestDto) {

        Member findMember = memberRepository.findByLoginId(requestDto.getLoginId());

        if(findMember == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ID);
        }
        if (!passwordEncoder.matches(requestDto.getPassword(), findMember.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        TokenDto tokenDto = jwtTokenProvider.createToken(findMember);
        refreshTokenService.saveRefreshToken(tokenDto);

        AuthTokenResponse responseDto = AuthTokenResponse.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .role(findMember.getRole())
                .build();

        return responseDto;
    }
}
