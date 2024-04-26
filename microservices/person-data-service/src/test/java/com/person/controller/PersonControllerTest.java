package com.person.controller;

import com.person.exception.PersonNotFoundException;
import com.person.model.Person;
import com.person.service.PersonService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.person.fake.PersonTestDataBuilder.aPerson;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest
public class PersonControllerTest {

    private static final String PATH_PERSONS = "/persons";
    private static final Long A_PERSON_ID = 12314L;
    private static final Long ANOTHER_PERSON_ID = 44414L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Nested
    class GetPersons {

        @Test
        void whenMakeRequest_thenResultContentTypeIsJSON() throws Exception {
            mockMvc
                    .perform(get(PATH_PERSONS))
                    .andExpect(content().contentType(APPLICATION_JSON));
        }

        @Test
        void whenMakeRequest_thenHttpStatusCodeIsOK() throws Exception {
            mockMvc
                    .perform(get(PATH_PERSONS))
                    .andExpect(status().isOk());
        }

        @Test
        void whenMakeRequest_thenReturnPersons() throws Exception {
            when(personService.getPersons()).thenReturn(List.of(aPerson(A_PERSON_ID), aPerson(ANOTHER_PERSON_ID)));

            mockMvc
                    .perform(get(PATH_PERSONS))
                    .andExpect(jsonPath("$.[0].id").value(A_PERSON_ID))
                    .andExpect(jsonPath("$.[1].id").value(ANOTHER_PERSON_ID));

        }

    }

    @Nested
    class GetPersonById {

        @Test
        void whenMakeRequest_thenHttpStatusCodeIsOK() throws Exception {
            mockMvc
                    .perform(get(PATH_PERSONS + "/" + A_PERSON_ID))
                    .andExpect(status().isOk());
        }

        @Test
        void whenMakeRequest_thenReturnPerson() throws Exception {
            Person aPerson = aPerson(A_PERSON_ID);

            when(personService.getPersonById(A_PERSON_ID)).thenReturn(aPerson);

            mockMvc
                    .perform(get(PATH_PERSONS + "/" + A_PERSON_ID))
                    .andExpect(jsonPath("$.id").value(A_PERSON_ID))
                    .andExpect(jsonPath("$.name").value(aPerson.getName()));

        }

        @Test
        void whenResourceNotExistent_thenReturn404() throws Exception {
            when(personService.getPersonById(A_PERSON_ID)).thenThrow(PersonNotFoundException.class);

            mockMvc
                    .perform(get(PATH_PERSONS + "/" + A_PERSON_ID))
                    .andExpect(jsonPath("$.statusCode").value(404));
        }

    }

}
