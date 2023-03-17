package beachcombine.backend.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateRequest {

    @NotEmpty
    @Pattern(regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$", message="닉네임은 2자 이상 16자 이하, 영어 또는 숫자 또는 한글로 구성되어야 합니다") // 2자 이상 16자 이하, 영어 또는 숫자 또는 한글로 구성
    private String nickname;

    @NotNull
    private MultipartFile image;

    @NotNull
    private Boolean isChanged; // image 수정 여부 체크. true면 이미지 수정된 것.
}