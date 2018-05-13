package io.petstore.service;

import com.jayway.restassured.response.ValidatableResponse;
import io.petstore.dto.Order;
import io.petstore.exception.ApiResponseException;

import java.util.Map;

public interface PetStoreService {
    Map<String, Long> getPetInventoriesByStatus();

    ValidatableResponse placePetOrder(Order order);

    Order findOrderById(long oderId) throws ApiResponseException;

    ValidatableResponse deleteOrderById(long orderId);
}
