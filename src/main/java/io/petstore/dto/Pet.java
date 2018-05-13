package io.petstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Arrays;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class Pet {
    private long id;

    @JsonProperty("category")
    private Category category;

    private String name;

    @JsonProperty("tags")
    private Tag[] tags;

    @JsonProperty("photoUrls")
    private String[] photoUrls;

    @JsonProperty("status")
    private PetStatus status;

    public Pet() {
    }

    public enum PetStatus {
        @JsonProperty("available")
        AVAILABLE("available"),
        @JsonProperty("pending")
        PENDING("pending"),
        @JsonProperty("sold")
        SOLD("sold");

        private String value;
        public String getValue() {
            return value;
        }
        PetStatus(String value) {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", tags=" + Arrays.toString(tags) +
                ", photoUrls=" + Arrays.toString(photoUrls) +
                ", status=" + status +
                '}';
    }
}
