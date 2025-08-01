package com.alaeldin.exception.data_already_exist;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@Data
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DataAlreadyExist extends RuntimeException
{
    private String fieldName;
    private String fieldValue;
    public DataAlreadyExist(String fieldName,String fieldValue)
    {
        super(String.format("%s with value '%s' already exists.", fieldName, fieldValue));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
