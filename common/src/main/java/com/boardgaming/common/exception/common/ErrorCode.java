package com.boardgaming.common.exception.common;

public enum ErrorCode {
    METHOD_NOT_ALLOWED(405, "C001", "지원하지 않는 METHOD 입니다."),
    HANDLE_AUTHENTICATION_ENTRYPOINT(401, "C002", "로그인 후 사용 가능합니다."),
    NOT_FOUND_USER(404, "U001", "존재하지 않는 유저입니다."),
    DUPLICATE_EMAIL(409, "UV001", "이미 가입된 이메일입니다."),
    NOT_SENT_EMAIL_VERIFICATION(400, "UV002", "발송되지 않은 인증번호입니다."),
    INVALID_EMAIL_VERIFICATION(400, "UV003", "유효하지 않은 인증번호입니다."),
    NOT_FOUND_EMAIL_VERIFICATION(400, "UV004", "인증되지 않은 이메일입니다."),
    INVALID_PASSWORD_CONFIRM(400, "UV005", "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    NOT_FOUND_GOMOKU_ROOM(404, "GR001", "존재하지 않은 오목 방입니다."),
    NOT_FOUND_GOMOKU_GAME_HISTORY(404, "GGH001", "존재하지 않은 오목 게임 기록입니다."),
    NOT_YOUR_TURN(400, "R001", "당신의 턴이 아닙니다."),
    INVALID_ROOM_STATE(400, "R002", "유효하지 않은 방 상태입니다."),
    INVALID_MOVE(400, "R003", "유효하지 않은 수입니다."),
    NOT_FOUND_FILE(404, "FI001", "존재하지 않은 파일 입니다."),
    NOT_FOUND_FILE_INFO(404, "FI002", "존재하지 않은 파일 정보입니다."),
    ALREADY_EXISTS_FILE(409, "FI003", "이미 생성된 파일 입니다."),
    INVALID_OAUTH2_TEMP_KEY(400, "OA001", "유효하지 않은 소셜 로그인 임시 키입니다.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(
        final int status,
        final String code,
        final String message
    ) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() { return status; }
    public String getCode() { return code; }
}
