package com.person;

import com.person.controller.PersonController;
import com.person.repository.PersonRepository;
import com.person.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PersonDataServiceApplicationTests {

    @Autowired
    private PersonController personController;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Test
    void contextLoads() {
        assertThat(personController).isNotNull();
        assertThat(personService).isNotNull();
        assertThat(personRepository).isNotNull();
    }

}
