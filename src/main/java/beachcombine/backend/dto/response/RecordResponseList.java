package beachcombine.backend.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordResponseList {

    private String beachName;
    private List<RecordResponse> recordList;
}
