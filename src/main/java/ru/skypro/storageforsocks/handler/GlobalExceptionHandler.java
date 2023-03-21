package ru.skypro.storageforsocks.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.skypro.storageforsocks.exception.NeedDeleteMoreSocksThanExistException;
import ru.skypro.storageforsocks.exception.SocksNotFoundException;
import ru.skypro.storageforsocks.exception.WrongOperationException;
import ru.skypro.storageforsocks.mapper.ExceptionMapper;
import ru.skypro.storageforsocks.record.ExceptionRecord;


import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ExceptionMapper exceptionMapper;

    @ExceptionHandler(NeedDeleteMoreSocksThanExistException.class)
    public ResponseEntity<String> needDeleteMoreSocksThanExistExceptionHandler(NeedDeleteMoreSocksThanExistException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Need to delete more socks than exist");
    }

    @ExceptionHandler(SocksNotFoundException.class)
    public ResponseEntity<String> socksNotFoundExceptionHandler(SocksNotFoundException e) {
        System.out.println("hi");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Socks not found");


    }

    @ExceptionHandler(WrongOperationException.class)
    public ResponseEntity<String> wrongOperationExceptionHandler(WrongOperationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Sent request with wrong operation '"+e.getMessage()+"'");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> constraintViolationExceptionHandler(ConstraintViolationException e) {
        List<ExceptionRecord> exceptionRecordList = new ArrayList<>();
        List<ConstraintViolation<?>> exceptionList = e.getConstraintViolations().stream().toList();
        for (int i = 0; i < exceptionList.size(); i++) {
            exceptionRecordList.add(exceptionMapper.toExceptionRecordFromConstraintViolation(exceptionList.get(i), i));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exceptionRecordList.toString());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<ExceptionRecord> exceptionRecordList = new ArrayList<>();
        for (int i = 0; i < e.getBindingResult().getErrorCount(); i++) {
            exceptionRecordList.add(exceptionMapper.toExceptionRecordFromMethodArgumentNotValidException(e, i));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exceptionRecordList.toString());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        ExceptionRecord exceptionRecord = exceptionMapper.toExceptionRecordFromMissingServletRequestParameterException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exceptionRecord.toString());
    }

}
