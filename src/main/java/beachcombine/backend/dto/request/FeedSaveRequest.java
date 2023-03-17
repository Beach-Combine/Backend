package beachcombine.backend.dto.request;

import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedSaveRequest {

    @Size(max=300)
    private String review;
}
