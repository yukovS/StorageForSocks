package ru.skypro.storageforsocks.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.skypro.storageforsocks.exception.WrongOperationException;
import ru.skypro.storageforsocks.model.Operation;


@Slf4j
@Component
public class OperationConverter implements Converter<String, Operation> {
    /**
     * Check correctness name of operation and return it
     * @param source incoming name of operation
     * @return Enum operation
     */
    @Override
        public Operation convert(String source) {
        try {
            return Operation.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e ) {
            log.warn("Wrong operation");
            throw new WrongOperationException(source);
        }
    }
}
