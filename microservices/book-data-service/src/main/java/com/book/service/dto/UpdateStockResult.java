package com.book.service.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateStockResult {
    UpdateStockStatus status;
    String message;
}
