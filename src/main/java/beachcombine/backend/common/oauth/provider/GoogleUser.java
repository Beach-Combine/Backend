package beachcombine.backend.common.oauth.provider;

import beachcombine.backend.dto.request.AuthGoogleLoginRequest;
import beachcombine.backend.repository.MemberRepository;

import java.util.Map;

public class GoogleUser implements OAuthUserInfo{

    private AuthGoogleLoginRequest attribute;

    public GoogleUser(AuthGoogleLoginRequest attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProviderId() {
        return attribute.getId();
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return attribute.getEmail();
    }

    @Override
    public String getNickname() {

        return attribute.getDisplayName();
    }

    @Override
    public String getImage() {
        return attribute.getPhotoUrl();
    }
}
