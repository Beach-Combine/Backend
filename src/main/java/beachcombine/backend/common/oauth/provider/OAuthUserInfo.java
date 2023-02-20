package beachcombine.backend.common.oauth.provider;

public interface OAuthUserInfo {

    String getProviderId();
    String getProvider();
    String getEmail();
    String getNickname();
    String getImage();
}
