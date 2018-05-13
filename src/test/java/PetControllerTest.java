import com.jayway.restassured.http.ContentType;
import io.petstore.dto.Category;
import io.petstore.dto.Pet;
import io.petstore.dto.Tag;
import io.petstore.exception.ApiResponseException;
import io.petstore.service.PetService;
import io.petstore.service.PetServiceImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.testng.Assert.assertNotNull;

public class PetControllerTest {
    private PetService petService;
    private String PATH_TO_IMAGE =  "src/main/resources/petImage.jpg";

    @BeforeClass
    public void setUp() {
        this.petService = new PetServiceImpl();
    }

    @Test
    public void newPetShouldBeAdded() throws ApiResponseException{
        Category category = new Category(0, "home_pet");
        Tag[] tag = {new Tag(0, "tag1")};
        long petId = 1L;
        String petName = "Richard";
        Pet petToAdd = Pet.builder()
                .name(petName)
                .status(Pet.PetStatus.SOLD)
                .id(petId)
                .tags(tag)
                .category(category)
                .photoUrls(new String[]{"test"})
                .build();

        petService.addPet(petToAdd)
                .statusCode(200).contentType(ContentType.JSON)
                .body("name", equalTo(petName));

        Pet addedPet = petService.findPetById(petId);

        assertThat(petToAdd, equalTo(addedPet));
    }

    @Test
    public void newPetWithMultiplyTagsShouldBeAdded(){
        Tag[] tag = {new Tag(1, "dog"), new Tag(2, "rottweiler") };
        long petId = 2L;
        Pet petToAdd = Pet.builder()
                .name("Richard")
                .status(Pet.PetStatus.SOLD)
                .id(petId)
                .tags(tag)
                .photoUrls(new String[]{"test"})
                .build();

        petService.addPet(petToAdd)
                .statusCode(200).contentType(ContentType.JSON)
                .body("tags[0].name", equalTo("dog"))
                .body("tags[1].name", equalTo("rottweiler"));
    }

    @Test
    public void shouldUpdatePetNameAndStatusById() throws ApiResponseException {
        Pet petToAdd = Pet.builder()
                .id(1L)
                .name("Max")
                .status(Pet.PetStatus.AVAILABLE)
                .build();

        petService.addPet(petToAdd)
                .statusCode(200).contentType(ContentType.JSON)
                .body("name", equalTo("Max"));

        petService.updatePetNameAndStatusById(1L, "Maxik", Pet.PetStatus.SOLD)
                       .statusCode(200).contentType(ContentType.JSON);

        Pet updatedPet = petService.findPetById(1L);

        assertThat(updatedPet.getName(), equalTo("Maxik"));
        assertThat(updatedPet.getStatus(), equalTo(Pet.PetStatus.SOLD));
    }

    @Test
    public void shouldNotUpdateNonExistingPet(){
        petService.deletePetById(101L);
        petService.updatePetNameAndStatusById(101L, "Cat", Pet.PetStatus.SOLD)
                     .statusCode(404)
                     .contentType(ContentType.JSON);
    }

    /**
     * Replaces all existing pet data with new pet data
     */
    @Test
    public void shouldUpdatePetWithReplacingExistingPetData(){
        Pet petToAdd = Pet.builder()
                .id(3L)
                .status(Pet.PetStatus.AVAILABLE)
                .name("PetDog")
                .build();
        petService.addPet(petToAdd)
                .statusCode(200).contentType(ContentType.JSON)
                .body("name", equalTo("PetDog"))
                .body("status", equalTo(Pet.PetStatus.AVAILABLE.getValue()));

        Pet petToUpdate = Pet.builder()
                .id(3L)
                .status(Pet.PetStatus.PENDING)
                .name("PetCat").build();
        petService.updatePet(petToUpdate)
                .statusCode(200).contentType(ContentType.JSON)
                .body("name", equalTo("PetCat"))
                .body("status", equalTo(Pet.PetStatus.PENDING.getValue()));
    }

    @Test(expectedExceptions = ApiResponseException.class,
            expectedExceptionsMessageRegExp = "404 - Pet not found")
    public void shouldNotFindNonExistingPetById() throws ApiResponseException {
        long petId = 1001L;
        petService.deletePetById(petId);
        petService.findPetById(petId);
    }

    @Test(expectedExceptions = ApiResponseException.class,
            expectedExceptionsMessageRegExp = "404 - Pet not found")
    public void petShouldBeDeleted() throws ApiResponseException{
        long petId = 1L;
        Pet petToBeDeleted = Pet.builder()
                .id(petId)
                .status(Pet.PetStatus.AVAILABLE)
                .name("Masik")
                .build();
        petService.addPet(petToBeDeleted)
                .statusCode(200).contentType(ContentType.JSON)
                .body("name", equalTo("Masik"));

        petService.deletePetById(petId)
                .statusCode(200).contentType(ContentType.JSON);

        petService.findPetById(petId);
    }

    @Test
    public void shouldReturnSoldPetsByStatus() throws ApiResponseException {
        List<Pet> pets = petService.findPetsByStatus(Pet.PetStatus.SOLD);
        for(Pet pet : pets){
            assertThat(pet.getStatus(), equalTo(Pet.PetStatus.SOLD));
        }
    }

    @Test
    public void shouldReturnSoldAndPendingPetsByStatus() throws ApiResponseException {
       List<Pet> pets = petService.findPetsByStatus(Pet.PetStatus.SOLD, Pet.PetStatus.PENDING);
        for(Pet pet : pets){
            assertThat(pet.getStatus(),
                    anyOf(equalTo(Pet.PetStatus.SOLD), equalTo(Pet.PetStatus.PENDING)));
        }
    }

    @Test
    public void shouldUploadPetsImageByPetId(){
       String additionalData = "FavouritePicture";
       petService.uploadImageByPetId(1L, PATH_TO_IMAGE, additionalData )
               .statusCode(200).contentType(ContentType.JSON)
               .body("message", containsString(additionalData));
    }

    @Test
    public void shouldNotUploadNotImageFile(){
        String path_to_pdf =  "src/main/resources/pdf-sample.pdf";
        petService.uploadImageByPetId(1L, path_to_pdf, "" )
                .statusCode(400).contentType(ContentType.JSON);

    }

    /**
     * Expect that we can't load image for non existing pet
     * May be 404 Not Found with message Pet Not Found
     */
    @Test
    public void shouldNotUploadImageForNonExistingPet(){
        petService.deletePetById(1L);
        petService.uploadImageByPetId(1L, PATH_TO_IMAGE, "")
                .statusCode(404).contentType(ContentType.JSON);
    }

    /**
     * 1. Upload image for pet by pet id
     * 2. Get pet by id
     * 3. Сheck if pet has photoUrl
     */
    @Test
    public void petShouldContainsImageUrlAfterImageUploading() throws ApiResponseException {
        Pet petToAdd = Pet.builder()
                .id(101L)
                .name("PetWithPhoto")
                .build();
        petService.addPet(petToAdd)
                .statusCode(200).contentType(ContentType.JSON)
                .body("name", equalTo("PetWithPhoto"))
                .body("photoUrls", equalTo(null));
        petService.uploadImageByPetId(101L, PATH_TO_IMAGE, "")
                .statusCode(200).contentType(ContentType.JSON);

        Pet pet = petService.findPetById(101L);
        assertNotNull(pet.getPhotoUrls(), "Pet photoUrls");
    }
}

