package beachcombine.backend.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationCode {

    CLEANING_AND_TRASH_DISPOSAL("points",
            "You got 100 points",
            "Congratulations on the Beach cleaning."),
    CLEANING_WITHOUT_TRASH_DISPOSAL("points",
            "You got 30 points first",
            "We're checking trash can registration information"),
    TRASHCAN_CREDENTIAL("points",
            "You got 70 points",
            "The trash can registration was confirmed");

    private final String title;
    private final String message;
    private final String details;
}
