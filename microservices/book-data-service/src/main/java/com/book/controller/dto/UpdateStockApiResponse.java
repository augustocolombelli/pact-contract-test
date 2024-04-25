package com.book.controller.dto;

import com.book.service.dto.UpdateStockStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateStockApiResponse {
    UpdateStockStatus status;
    String message;
}

