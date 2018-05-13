package io.petstore.service;

import com.jayway.restassured.response.ValidatableResponse;
import io.petstore.dto.Pet;
import io.petstore.exception.ApiResponseException;

import java.util.List;

public interface PetService {
    ValidatableResponse addPet(Pet petToAdd);

    List<Pet> findPetsByStatus(Pet.PetStatus... status) throws ApiResponseException;

    Pet findPetById(long petId) throws ApiResponseException;

    ValidatableResponse updatePet(Pet pet);

    ValidatableResponse deletePetById(long petId);

    ValidatableResponse uploadImageByPetId(long petId, String pathToImage, String additionalMetadata);

    ValidatableResponse updatePetNameAndStatusById(long petId, String petName, Pet.PetStatus status);
}
