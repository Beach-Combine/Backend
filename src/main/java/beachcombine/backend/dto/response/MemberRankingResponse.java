package beachcombine.backend.dto.response;

import beachcombine.backend.domain.Member;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRankingResponse {

    private boolean nextPage;
    private List<MemberDto> memberDtoList;

    @Data
    @Builder
    public static class MemberDto {

        private Long id;
        private String nickname;
        private String image;
        private Integer point;

        public static MemberDto of(Member member, String imageUrl, String range) {

            return MemberDto.builder()
                    .id(member.getId())
                    .nickname(member.getNickname())
                    .image(imageUrl)
                    .point(range.equals("all") ? member.getTotalPoint() : member.getMonthPoint())
                    .build();
        }
    }


}