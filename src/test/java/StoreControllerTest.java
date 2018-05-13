import com.jayway.restassured.http.ContentType;
import io.petstore.dto.Order;
import io.petstore.dto.Pet;
import io.petstore.exception.ApiResponseException;
import io.petstore.service.PetService;
import io.petstore.service.PetServiceImpl;
import io.petstore.service.PetStoreService;
import io.petstore.service.PetStoreServiceImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class StoreControllerTest {
    private PetStoreService petStoreService;
    private PetService petService;

    @BeforeClass
    public void setUp() {
        this.petStoreService = new PetStoreServiceImpl();
        this.petService = new PetServiceImpl();
    }

    @Test
    public void orderForPetShouldBePlaced() throws ParseException {
        long petId = 2L;
        Pet petToAdd = Pet.builder()
                .name("Richard")
                .status(Pet.PetStatus.AVAILABLE)
                .id(petId)
                .build();
        petService.addPet(petToAdd)
                .statusCode(200).contentType(ContentType.JSON);


        Order orderToAdd = Order.builder()
                .petId(petToAdd.getId())
                .quantity(1)
                .id(1L).status(Order.OrderStatus.PLACED)
                .build();
        petStoreService.placePetOrder(orderToAdd)
                .statusCode(200).contentType(ContentType.JSON)
                .body("quantity", equalTo(1))
                .body("status", equalTo(Order.OrderStatus.PLACED.getValue()));
    }

    @Test(expectedExceptions = ApiResponseException.class,
            expectedExceptionsMessageRegExp = "404 - Order not found")
    public void shouldNotFindNonExistingOrder() throws ApiResponseException {
        petStoreService.deleteOrderById(1L);
        petStoreService.findOrderById(1L);
    }

    @Test
    public void shouldDeletePlacedOrder(){
        Pet petToAdd = Pet.builder()
                .name("newPet")
                .status(Pet.PetStatus.AVAILABLE)
                .id(3L)
                .build();
        petService.addPet(petToAdd)
                .statusCode(200).contentType(ContentType.JSON);


        Order orderToAdd = Order.builder()
                .petId(petToAdd.getId())
                .quantity(1)
                .id(3L).status(Order.OrderStatus.PLACED)
                .build();
        petStoreService.placePetOrder(orderToAdd)
                .statusCode(200).contentType(ContentType.JSON)
                .body("quantity", equalTo(1))
                .body("status", equalTo(Order.OrderStatus.PLACED.getValue()));

        petStoreService.deleteOrderById(orderToAdd.getId()).statusCode(200);
    }
}
