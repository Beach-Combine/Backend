package beachcombine.backend.event;

import beachcombine.backend.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberEvent {

    private final Member member;
    private final NotificationCode notificationCode;
}
