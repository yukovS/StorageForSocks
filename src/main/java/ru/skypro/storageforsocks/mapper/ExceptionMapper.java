package ru.skypro.storageforsocks.mapper;

import jakarta.validation.ConstraintViolation;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import ru.skypro.storageforsocks.record.ExceptionRecord;

import java.util.Objects;

@Mapper
public interface ExceptionMapper {
    @Mapping(target = "id", source = "i")
    @Mapping(target = "fieldName", expression = "java(getField(constraintViolation))")
    @Mapping(target = "inputValue", expression = "java(getInvalidValue(constraintViolation))")
    @Mapping(target = "exceptionDescription", source = "constraintViolation.messageTemplate")
    ExceptionRecord toExceptionRecordFromConstraintViolation(ConstraintViolation constraintViolation, Integer i);


    default String getField(ConstraintViolation constraintViolation) {
        return StringUtils.substringAfter(constraintViolation.getPropertyPath().toString(), ".");
    }

    default String getInvalidValue(ConstraintViolation constraintViolation) {
        return constraintViolation.getInvalidValue().toString();
    }
    @Mapping(target = "id", source = "i")
    @Mapping(target = "fieldName", expression = "java(getField(exception,i))")
    @Mapping(target = "inputValue",expression = "java(getRejectedValue(exception,i))")
    @Mapping(target = "exceptionDescription",expression = "java(getDefaultMessage(exception,i))")
    ExceptionRecord toExceptionRecordFromMethodArgumentNotValidException(MethodArgumentNotValidException exception, Integer i);

    default String getField(MethodArgumentNotValidException exception, Integer i) {
        return exception.getFieldErrors().get(i).getField();
    }

    default String getRejectedValue(MethodArgumentNotValidException exception, Integer i) {
        return Objects.requireNonNull(exception.getFieldErrors().get(i).getRejectedValue()).toString();
    }

    default String getDefaultMessage(MethodArgumentNotValidException exception, Integer i) {
        return exception.getFieldErrors().get(i).getDefaultMessage();
    }
    @Mapping(target = "id", constant = "0")
    @Mapping(target = "fieldName", source = "exception.parameterName")
    @Mapping(target = "inputValue",ignore = true)
    @Mapping(target = "exceptionDescription",source = "exception.body.detail")
    ExceptionRecord toExceptionRecordFromMissingServletRequestParameterException(MissingServletRequestParameterException exception);
}
