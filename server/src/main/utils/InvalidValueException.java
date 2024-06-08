package main.utils;

public class InvalidValueException extends RuntimeException{
    public InvalidValueException(String fieldName, String requirements) {
        super(String.format("Error initializing field %s: %s", fieldName, requirements));
    }
}
