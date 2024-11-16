package org.imannuel.moviereservationapi.dto.request.midtrans;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MidtransItemDetailRequest {
    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "price")
    private Long price;

    @JsonProperty(value = "quantity")
    private Integer quantity;

    @JsonProperty(value = "name")
    private String name;
}

