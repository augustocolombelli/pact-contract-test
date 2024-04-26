package com.book.sales.controller;

import com.book.sales.service.BookSalesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.book.sales.fake.BookSalesResultTestDataBuilder.aOutOfStockBookSalesResult;
import static com.book.sales.fake.BookSalesResultTestDataBuilder.aSuccessBookSalesResult;
import static com.book.sales.fake.BookSalesResultTestDataBuilder.aUpdateStockFailureBookSalesResult;
import static com.book.sales.service.dto.BookSalesStatus.OUT_OF_STOCK;
import static com.book.sales.service.dto.BookSalesStatus.SUCCESS;
import static com.book.sales.service.dto.BookSalesStatus.UPDATE_STOCK_FAILURE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest
public class BookSalesControllerTest {

    private static final String PATH_SALES = "/book-sales";
    private static final String A_REQUEST_BODY = "{\"personId\": 1001,\"bookId\": 203}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookSalesService bookSalesService;

    @Test
    void whenMakeRequest_thenResultContentTypeIsJSON() throws Exception {
        when(bookSalesService.processBookSales(any(), any())).thenReturn(aSuccessBookSalesResult());

        this.mockMvc.perform(
                        post(PATH_SALES)
                                .contentType(APPLICATION_JSON)
                                .content(A_REQUEST_BODY))
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    void whenMakeRequest_thenHttpStatusCodeIsOK() throws Exception {
        when(bookSalesService.processBookSales(any(), any())).thenReturn(aSuccessBookSalesResult());

        this.mockMvc.perform(
                        post(PATH_SALES)
                                .contentType(APPLICATION_JSON)
                                .content(A_REQUEST_BODY))
                .andExpect(status().isOk());
    }

    @Test
    void whenMakeRequest_henReturnsResponseStatus() throws Exception {
        when(bookSalesService.processBookSales(any(), any())).thenReturn(aSuccessBookSalesResult());

        this.mockMvc
                .perform(post(PATH_SALES).contentType(APPLICATION_JSON).content(A_REQUEST_BODY))
                .andExpect(jsonPath("$.status").value(SUCCESS.toString()));

    }

    @Test
    void whenBookDoesNotHaveStock_thenReturnsHttpStatus422() throws Exception {
        when(bookSalesService.processBookSales(any(), any())).thenReturn(aOutOfStockBookSalesResult());

        this.mockMvc
                .perform(post(PATH_SALES).contentType(APPLICATION_JSON).content(A_REQUEST_BODY))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(OUT_OF_STOCK.toString()));

    }

    @Test
    void whenThereIsProblemDuringUpdateStock_thenReturnsHttpStatus422() throws Exception {
        when(bookSalesService.processBookSales(any(), any())).thenReturn(aUpdateStockFailureBookSalesResult());

        this.mockMvc
                .perform(post(PATH_SALES).contentType(APPLICATION_JSON).content(A_REQUEST_BODY))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(UPDATE_STOCK_FAILURE.toString()));

    }

}
