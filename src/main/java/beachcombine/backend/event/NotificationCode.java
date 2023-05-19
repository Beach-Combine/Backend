package beachcombine.backend.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationCode {

    CLEANING_AND_TRASH_DISPOSAL("청소 완료",
            "청소 완료로 100포인트가 지급되었습니다.",
            "청소 완료를 축하 드립니다."),
    CLEANING_WITHOUT_TRASH_DISPOSAL("청소 완료",
            "청소 완료로 30포인트가 지급되었습니다.",
            "청소 완료를 축하 드립니다. 쓰레기통이 인증되면 추가적인 70포인트가 지급됩니다.");

    private final String title;
    private final String message;
    private final String details;
}
