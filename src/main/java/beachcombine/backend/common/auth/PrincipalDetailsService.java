package beachcombine.backend.common.auth;

import beachcombine.backend.domain.Member;
import beachcombine.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("PrincipalDetailsService : 진입");
        Member member = memberRepository.findByLoginId(username);

        return new PrincipalDetails(member);
    }
}
