package beachcombine.backend.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberUpdateRequest {

    private Long id;
    private String nickname;
    private String image;

}
