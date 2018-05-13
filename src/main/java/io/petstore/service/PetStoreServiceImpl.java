package io.petstore.service;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;
import io.petstore.api.ApiClient;
import io.petstore.api.PetStoreApiClient;
import io.petstore.dto.Order;
import io.petstore.exception.ApiResponseException;

import static com.jayway.restassured.path.json.JsonPath.from;
import java.util.Map;

public class PetStoreServiceImpl implements PetStoreService {
    private ApiClient api;
    private String STORE_IVENTORY_URI = "store/inventory";
    private String PLACE_ORDER_URI = "store/order/";

    public PetStoreServiceImpl() {
        this.api = new PetStoreApiClient();
    }

    @Override
    public Map<String, Long> getPetInventoriesByStatus() {
        Response resp = api.doGet(STORE_IVENTORY_URI);
        return from(resp.asString()).getMap(".");
    }

    @Override
    public ValidatableResponse placePetOrder(Order order) {
        assert order != null;
        return api.doPost(PLACE_ORDER_URI, order).then();
    }

    @Override
    public Order findOrderById(long orderId) throws ApiResponseException {
        Order order = null;
        Response resp = api.doGet(PLACE_ORDER_URI + orderId);
        try{
            order = resp.as(Order.class);
        } catch(Exception e){
            throw new ApiResponseException(resp.getStatusCode(), resp.path("message"));
        }
        return order;
    }

    @Override
    public ValidatableResponse deleteOrderById(long orderId) {
        return api.doDelete(PLACE_ORDER_URI + orderId).then();
    }
}
