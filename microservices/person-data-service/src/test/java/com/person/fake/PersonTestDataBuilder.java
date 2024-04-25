package com.person.fake;

import com.person.model.Person;

public class PersonTestDataBuilder {

    public static Person aPerson(Long personId) {
        return Person.builder()
                .id(personId)
                .name("a name")
                .country("a country")
                .passportNumber("a passport number")
                .build();
    }
}
