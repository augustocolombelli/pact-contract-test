package com.person;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import com.person.model.Person;
import com.person.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;

@WebMvcTest
@Provider("person-data-service")
@PactBroker
public class PersonDataProviderForBookSalesContractTest {

    private static final long A_PERSON_ID = 1001L;
    private static final String A_PERSON_NAME = "John";
    private static final String A_PERSON_PASSPORT_NUMBER = "FT858652";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @BeforeEach
    public void setUp(PactVerificationContext context){
        context.setTarget(new MockMvcTestTarget(mockMvc));
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("it has a person and status code is 200")
    public void itHasPersonWithIdAndStatusIs200() {
        when(personService.getPersonById(A_PERSON_ID))
                .thenReturn(Person.builder()
                        .id(A_PERSON_ID)
                        .name(A_PERSON_NAME)
                        .build());
    }

}
