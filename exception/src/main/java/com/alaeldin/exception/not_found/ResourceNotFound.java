package com.alaeldin.exception.not_found;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFound  extends RuntimeException
{
      public  String resourceName;
      public  String fieldName;
      public  Integer fieldValue;
      public  ResourceNotFound(String resourceName, String fieldName, Integer fieldValue) {
          super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
          this.resourceName = resourceName;
          this.fieldName = fieldName;
          this.fieldValue = fieldValue;
      }
}
