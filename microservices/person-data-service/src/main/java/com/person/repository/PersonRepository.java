package com.person.repository;

import com.person.exception.PersonNotFoundException;
import com.person.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonRepository {

    private static final List<Person> persons = buildFakePersons();

    public Person getPersonById(Long id) {
        return persons.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(PersonNotFoundException::new);
    }

    public List<Person> getPersons() {
        return persons;
    }

    private static List<Person> buildFakePersons() {
        Person aPerson = Person.builder()
                .id(1001L)
                .name("John")
                .country("Brazil")
                .build();

        Person anotherPerson = Person.builder()
                .id(1002L)
                .name("Maria")
                .country("Brazil")
                .build();

        Person aThirdPerson = Person.builder()
                .id(1003L)
                .name("Robert")
                .country("Brazil")
                .build();

        return List.of(aPerson, anotherPerson, aThirdPerson);
    }

}
