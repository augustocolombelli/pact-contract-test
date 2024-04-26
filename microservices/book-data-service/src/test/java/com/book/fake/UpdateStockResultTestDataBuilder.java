package com.book.fake;

import com.book.service.dto.UpdateStockResult;
import com.book.service.dto.UpdateStockStatus;

import static com.book.service.dto.UpdateStockStatus.FAILURE;
import static com.book.service.dto.UpdateStockStatus.SUCCESS;

public class UpdateStockResultTestDataBuilder {

    public static UpdateStockResult aSuccessUpdateStockResult() {
        return UpdateStockResult.builder()
                .status(SUCCESS)
                .message("a message")
                .build();
    }

    public static UpdateStockResult aFailureUpdateStockResult() {
        return UpdateStockResult.builder()
                .status(FAILURE)
                .message("a message")
                .build();
    }

}
