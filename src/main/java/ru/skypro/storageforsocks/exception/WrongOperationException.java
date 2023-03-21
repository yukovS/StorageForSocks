package ru.skypro.storageforsocks.exception;

public class WrongOperationException extends RuntimeException{
    public WrongOperationException(String message) {
        super(message);
    }
}
