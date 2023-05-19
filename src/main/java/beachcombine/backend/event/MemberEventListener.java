package beachcombine.backend.event;

import beachcombine.backend.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Async
@Transactional(readOnly = true)
@Component
public class MemberEventListener {

    @EventListener
    public void handleMemberEvent(MemberEvent memberEvent){

        Member member = memberEvent.getMember();
        NotificationCode notificationCode = memberEvent.getNotificationCode();
        // DB에 Notification 정보 저장
    }
}
