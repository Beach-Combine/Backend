package beachcombine.backend.service;

import beachcombine.backend.common.auth.PrincipalDetails;
import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.common.jwt.dto.TokenDto;
import beachcombine.backend.common.jwt.JwtUtils;
import beachcombine.backend.common.oauth.provider.GoogleUser;
import beachcombine.backend.common.oauth.provider.OAuthUserInfo;
import beachcombine.backend.domain.Member;
import beachcombine.backend.domain.RefreshToken;
import beachcombine.backend.dto.request.AuthJoinRequest;
import beachcombine.backend.dto.request.AuthLoginRequest;
import beachcombine.backend.dto.response.AuthJoinResponse;
import beachcombine.backend.dto.response.AuthRecreateTokenResponse;
import beachcombine.backend.dto.response.AuthTokenResponse;
import beachcombine.backend.repository.MemberRepository;
import beachcombine.backend.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

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

        if (findMember == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ID);
        }
        if (!passwordEncoder.matches(requestDto.getPassword(), findMember.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        TokenDto tokenDto = jwtUtils.createToken(findMember);
        refreshTokenService.saveRefreshToken(tokenDto);

        AuthTokenResponse responseDto = AuthTokenResponse.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .role(findMember.getRole())
                .build();

        return responseDto;
    }

    // 구글 로그인
    public AuthTokenResponse googleLogin(Map<String, Object> data) {

        // 1. 클라에서 구글로그인 통한 토큰(앱 자체적인 토큰)을 받음
        // 2. 클라에서 `POST /auth/google` 로 api 요청함. 이때, 클라는 request body에 `Map<String, Object> data` 를 담아 보냄
        // 3. 서버는 request body로 들어온 data를 이용해 사용자 정보를 얻음. data는 map 형태로 되어 있고, key값이 profileObj인 value에 사용자 정보가 들어있음
        OAuthUserInfo googleUser = new GoogleUser((Map<String, Object>) data.get("profileObj"));
        /*
        Map<String, Object> data -> map.put("profileObj", object);

        data를 Map으로 받아오긴 하지만, Object 형태로 그려보자면 아래와 같음!
        data = {
            "profileObj" : {
                "googleId" : "구글아이디"
                "email" : "이메일"
                "name" : "이름"
            }
        }
         */

        // 4. DB에 data에서 받아온 정보를 가진 사용자가 있는지 조회
        Member findMember = memberRepository.findByLoginId(googleUser.getProvider() + "_" + googleUser.getProviderId());

        // 5. DB에 사용자가 없다면, 구글 로그인을 처음 한 사용자이니, DB에 사용자 정보를 저장(회원가입 시켜줌)
        if (findMember == null) {
            Member memberRequest = Member.builder()
                    .loginId(googleUser.getProvider() + "_" + googleUser.getProviderId())
                    .password(bCryptPasswordEncoder.encode("beachcombine"))
                    .email(googleUser.getEmail())
                    .provider(googleUser.getProvider())
                    .providerId(googleUser.getProviderId())
                    .nickname(googleUser.getName())
                    .image(googleUser.getImage())
                    .role("ROLE_USER")
                    .build();

            findMember = memberRepository.save(memberRequest);
        }

        // 6. 처음 온 사용자든, 기존 사용자든 구글 로그인을 시도했으니 로그아웃 전까진 앱을 마음껏 이용할 수 있게 앱의 자체적인 토큰(accessToken과 refreshToken)을 발급해줘야 함.
        TokenDto tokenDto = jwtUtils.createToken(findMember);
        refreshTokenService.saveRefreshToken(tokenDto); // 7. refreshToken은 DB에 저장. accessToken은 시큐리티 세션에 저장.

        // 8. 서버는 refreshToken과 accessToken, 그리고 사용자의 권한(관리자인지, 일반유저인지)을 클라에게 response body에 담아 줌
        AuthTokenResponse responseDto = AuthTokenResponse.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .role(findMember.getRole())
                .build();

        return responseDto;
    }

    // accessToken 재발급
    public AuthRecreateTokenResponse refresh(String request) {

        String refreshToken = request.replace("Bearer ", "");

        // refresh 토큰 유효한지 확인
        jwtUtils.validateRefreshToken(refreshToken);
        String loginId = jwtUtils.getUsernameFromRefreshToken(refreshToken);
        RefreshToken findRefreshToken = refreshTokenRepository.findByKeyLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.TOKEN_INVALID));
        if(!refreshToken.equals(findRefreshToken.getRefreshToken())) {
            throw new CustomException(ErrorCode.TOKEN_INVALID);
        }

        Member findMember = memberRepository.findByLoginId(loginId);

        String createdAccessToken = jwtUtils.recreateAccessToken(findMember);

        if (createdAccessToken == null) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }

        AuthRecreateTokenResponse authRecreateTokenResponse = AuthRecreateTokenResponse.builder()
                .accessToken(createdAccessToken)
                .role(findMember.getRole())
                .build();

        return authRecreateTokenResponse;
    }
}
