package com.book;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import com.book.model.Book;
import com.book.service.BookService;
import com.book.service.dto.UpdateStockResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.book.service.dto.UpdateStockStatus.SUCCESS;
import static org.mockito.Mockito.when;

@WebMvcTest
@Provider("book-data-service")
@PactBroker
public class BookDataProviderForBookSalesContractTest {

    private static final long A_BOOK_ID = 1001L;
    private static final long A_BOOK_STOCK = 15L;
    private static final long A_BOOK_QUANTITY_UPDATE_STOCK = 1L;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @BeforeEach
    public void setUp(PactVerificationContext context){
        context.setTarget(new MockMvcTestTarget(mockMvc));
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("it has a book and status code is 200")
    public void itHasBookWithIdAndStatusIs200() {
        when(bookService.getBookById(A_BOOK_ID))
                .thenReturn(Book.builder()
                        .id(A_BOOK_ID)
                        .currentStock(A_BOOK_STOCK)
                        .build());
    }

    @State("it has a book and stock can be updated")
    public void aRequestToUpdateTheStockOfBook() {
        when(bookService.updateStock(A_BOOK_ID, A_BOOK_QUANTITY_UPDATE_STOCK))
                .thenReturn(UpdateStockResult.builder()
                        .message("The stock of the book " + A_BOOK_ID + " was update to 16")
                        .status(SUCCESS)
                        .build());
    }

}
