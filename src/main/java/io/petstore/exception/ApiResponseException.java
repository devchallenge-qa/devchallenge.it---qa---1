package io.petstore.exception;

public class ApiResponseException extends Exception {
    public ApiResponseException(int code, String type, String message) {
        super( "{code:" + code + ", type:" + type + ", message:" + message + "}");
    }

    public ApiResponseException(int responseStatusCodee, String responseMessage){
        super( responseStatusCodee + " - " + responseMessage);
    }

    public ApiResponseException(String message) {
        super(message);
    }
}
