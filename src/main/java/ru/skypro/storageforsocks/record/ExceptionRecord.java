package ru.skypro.storageforsocks.record;

import lombok.Data;

@Data
public class ExceptionRecord {
    private Integer id;
    private String fieldName;
    private String inputValue;
    private String exceptionDescription;

    @Override
    public String toString() {
        return "\nException "+id+"{" +
                "fieldName='" + fieldName + '\'' +
                ", inputValue='" + inputValue + '\'' +
                ", exceptionDescription='" + exceptionDescription + '\'' +
                "}";
    }
}
