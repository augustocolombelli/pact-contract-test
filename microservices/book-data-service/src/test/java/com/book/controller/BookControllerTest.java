package com.book.controller;

import com.book.exception.BookNotFoundException;
import com.book.model.Book;
import com.book.service.BookService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.book.fake.BookTestDataBuilder.aBook;
import static com.book.fake.UpdateStockResultTestDataBuilder.aFailureUpdateStockResult;
import static com.book.fake.UpdateStockResultTestDataBuilder.aSuccessUpdateStockResult;
import static com.book.service.dto.UpdateStockStatus.FAILURE;
import static com.book.service.dto.UpdateStockStatus.SUCCESS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest
public class BookControllerTest {

    private static final String PATH_BOOKS = "/books";
    private static final String UPDATE_STOCK_PATH = "/books/updateStock";
    private static final Long A_BOOK_ID = 12314L;
    private static final Long ANOTHER_BOOK_ID = 44414L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Nested
    class GetBooks {

        @Test
        void whenMakeRequest_thenResultContentTypeIsJSON() throws Exception {
            mockMvc
                    .perform(get(PATH_BOOKS))
                    .andExpect(content().contentType(APPLICATION_JSON));
        }

        @Test
        void whenMakeRequest_thenHttpStatusCodeIsOK() throws Exception {
            mockMvc
                    .perform(get(PATH_BOOKS))
                    .andExpect(status().isOk());
        }

        @Test
        void whenMakeRequest_thenReturnBooks() throws Exception {
            when(bookService.getBooks()).thenReturn(List.of(aBook(A_BOOK_ID), aBook(ANOTHER_BOOK_ID)));

            mockMvc
                    .perform(get(PATH_BOOKS))
                    .andExpect(jsonPath("$.[0].id").value(A_BOOK_ID))
                    .andExpect(jsonPath("$.[1].id").value(ANOTHER_BOOK_ID));

        }
    }

    @Nested
    class GetBookById {

        @Test
        void whenMakeRequest_thenHttpStatusCodeIsOK() throws Exception {
            mockMvc
                    .perform(get(PATH_BOOKS + "/" + A_BOOK_ID))
                    .andExpect(status().isOk());
        }

        @Test
        void whenMakeRequest_thenReturnBook() throws Exception {
            Book aBook = aBook(A_BOOK_ID);

            when(bookService.getBookById(A_BOOK_ID)).thenReturn(aBook);

            mockMvc
                    .perform(get(PATH_BOOKS + "/" + A_BOOK_ID))
                    .andExpect(jsonPath("$.id").value(A_BOOK_ID))
                    .andExpect(jsonPath("$.name").value(aBook.getName()));

        }

        @Test
        void whenResourceNotExistent_thenReturn404() throws Exception {
            when(bookService.getBookById(A_BOOK_ID)).thenThrow(BookNotFoundException.class);

            mockMvc
                    .perform(get(PATH_BOOKS + "/" + A_BOOK_ID))
                    .andExpect(jsonPath("$.statusCode").value(404));
        }
    }

    @Nested
    class UpdateStock {

        private static final String A_REQUEST_BODY = "{\"id\": 202,\"quantity\": 1}";

        @Test
        void whenMakeRequest_thenResultContentTypeIsJSON() throws Exception {
            when(bookService.updateStock(any(), any())).thenReturn(aSuccessUpdateStockResult());

            mockMvc
                    .perform(post(UPDATE_STOCK_PATH).contentType(APPLICATION_JSON).content(A_REQUEST_BODY))
                    .andExpect(content().contentType(APPLICATION_JSON));
        }

        @Test
        void whenMakeRequest_thenHttpStatusCodeIsOK() throws Exception {
            when(bookService.updateStock(any(), any())).thenReturn(aSuccessUpdateStockResult());

            mockMvc
                    .perform(post(UPDATE_STOCK_PATH).contentType(APPLICATION_JSON).content(A_REQUEST_BODY))
                    .andExpect(status().isOk());
        }

        @Test
        void whenMakeRequest_thenReturnsResponseStatus() throws Exception {
            when(bookService.updateStock(any(), any())).thenReturn(aSuccessUpdateStockResult());

            mockMvc
                    .perform(post(UPDATE_STOCK_PATH).contentType(APPLICATION_JSON).content(A_REQUEST_BODY))
                    .andExpect(jsonPath("$.status").value(SUCCESS.toString()));

        }

        @Test
        void whenBookDoesNotHaveStock_thenHttpStatusIs422() throws Exception {
            when(bookService.updateStock(any(), any())).thenReturn(aFailureUpdateStockResult());

            mockMvc
                    .perform(post(UPDATE_STOCK_PATH).contentType(APPLICATION_JSON).content(A_REQUEST_BODY))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.status").value(FAILURE.toString()));

        }

    }

}
