package beachcombine.backend.service;

import beachcombine.backend.common.jwt.dto.TokenDto;
import beachcombine.backend.common.jwt.JwtUtils;
import beachcombine.backend.domain.RefreshToken;
import beachcombine.backend.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(TokenDto tokenDto) {

        RefreshToken refreshToken = RefreshToken.builder()
                .keyLoginId(tokenDto.getKey())
                .refreshToken(tokenDto.getRefreshToken())
                .build();

        String loginId = refreshToken.getKeyLoginId();
        if(refreshTokenRepository.existsByKeyLoginId(loginId)) {
            refreshTokenRepository.deleteByKeyLoginId(loginId);
        }

        refreshTokenRepository.save(refreshToken);
    }
}