package beachcombine.backend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //공통 예외
    SHOULD_EXIST_IMAGE(HttpStatus.BAD_REQUEST, "이미지가 존재하지 않습니다."),
    NOT_VALID_URI(HttpStatus.BAD_REQUEST, "유효한 경로로 요청해주세요."),

    // Member 예외
    UNAUTHORIZED_ID(HttpStatus.UNAUTHORIZED, "아이디가 틀립니다."),
    UNAUTHORIZED_PASSWORD(HttpStatus.UNAUTHORIZED, "패스워드가 틀립니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    EXIST_USER_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    BAD_REQUEST_OPTION_VALUE(HttpStatus.BAD_REQUEST, "option 값이 잘못된 요청입니다."),

    // Beach 예외
    NOT_FOUND_BEACH(HttpStatus.NOT_FOUND, "해당 해변을 찾을 수 없습니다."),

    // Token 예외
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}