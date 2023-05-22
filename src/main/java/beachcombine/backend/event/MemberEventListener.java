package beachcombine.backend.event;

import beachcombine.backend.domain.Member;
import beachcombine.backend.domain.Notification;
import beachcombine.backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Async
@Transactional
@Component
@RequiredArgsConstructor
public class MemberEventListener {

    private final NotificationRepository notificationRepository;
    @EventListener
    public void handleMemberEvent(MemberEvent memberEvent){

        Member member = memberEvent.getMember();
        NotificationCode notificationCode = memberEvent.getNotificationCode();

        // DB에 Notification 정보 저장
        saveNotification(member, notificationCode);
    }

    public void saveNotification(Member member, NotificationCode notificationCode){

        Notification notification = Notification.builder()
                .member(member)
                .title(notificationCode.getTitle())
                .message(notificationCode.getMessage())
                .details(notificationCode.getDetails())
                .build();

        notificationRepository.save(notification);
    }
}
