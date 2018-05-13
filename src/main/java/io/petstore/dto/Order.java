package io.petstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Order {
    private long id;
    private long petId;
    private long quantity;
    private Date shipDate;
    private OrderStatus status;
    private boolean complete;

    public Order() {
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", petId=" + petId +
                ", quantity=" + quantity +
                ", shipDate=" + shipDate +
                ", status=" + status +
                ", complete=" + complete +
                '}';
    }

    public enum OrderStatus {
        @JsonProperty("placed")
        PLACED("placed"),
        @JsonProperty("approved")
        APPROVED("approved"),
        @JsonProperty("delivered")
        DELIVERED("delivered");

        private String value;
        public String getValue() {
            return value;
        }
        OrderStatus(String value) {
            this.value = value;
        }
    }
}
