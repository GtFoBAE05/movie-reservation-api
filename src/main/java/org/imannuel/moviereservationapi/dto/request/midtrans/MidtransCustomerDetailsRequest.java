package org.imannuel.moviereservationapi.dto.request.midtrans;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MidtransCustomerDetailsRequest {
    @JsonProperty(value = "email")
    private String email;
}
