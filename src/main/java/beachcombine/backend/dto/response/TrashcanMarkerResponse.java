package beachcombine.backend.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrashcanMarkerResponse {

    private String lat;
    private String lng;
    private String address;
    private Long id;
    private String nickname;
    private Boolean is_certified;
    private LocalDate date;
    private String image;
}
