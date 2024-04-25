package com.book.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStockApiRequest {

    @NotNull(message = "ID has to be present")
    Long id;

    @NotNull(message = "Quantity has to be present")
    Long quantity;

}
