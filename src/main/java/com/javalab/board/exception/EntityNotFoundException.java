package com.javalab.board.exception;

/**
 * 커스텀 예외 클래스: 엔티티를 찾을 수 없을 때 발생
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
