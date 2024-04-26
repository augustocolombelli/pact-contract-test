package com.person.service;

import com.person.model.Person;
import com.person.repository.PersonRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.person.fake.PersonTestDataBuilder.aPerson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    private static final Long A_PERSON_ID = 12314L;

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @Nested
    class GetPersonById {

        @Test
        void whenGetPersonById_thenCallRepository() {
            personService.getPersonById(A_PERSON_ID);

            verify(personRepository).getPersonById(A_PERSON_ID);
        }

        @Test
        void whenGetPersonById_thenReturnPerson() {
            // given
            when(personRepository.getPersonById(A_PERSON_ID)).thenReturn(aPerson(A_PERSON_ID));

            // when
            Person actualPerson = personService.getPersonById(A_PERSON_ID);

            // then
            assertThat(actualPerson.getId()).isEqualTo(A_PERSON_ID);
        }

    }

    @Nested
    class GetPersons {

        @Test
        void whenGetPersons_thenCallRepository() {
            personService.getPersons();

            verify(personRepository).getPersons();
        }

        @Test
        void whenGetPersons_thenReturnPersons() {
            // given
            when(personRepository.getPersons()).thenReturn(List.of(aPerson(A_PERSON_ID)));

            // when
            List<Person> actualPersons = personService.getPersons();

            // then
            assertThat(actualPersons).extracting(Person::getId).contains(A_PERSON_ID);
        }

    }
    
}
