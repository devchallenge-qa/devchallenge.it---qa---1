package io.petstore.api;

import com.jayway.restassured.response.Response;

import java.util.Map;

public interface ApiClient {
    Response doGet(String path, Map<String, Object> params);
    Response doGet(String path);
    Response doPost(String path, Object body);
    Response doPut(String path, Object body);
    Response doDelete(String path);
}
