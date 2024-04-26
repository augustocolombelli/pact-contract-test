package com.book.sales.fake;

import com.book.sales.client.persondata.dto.PersonDataResponse;

public class PersonDataResponseTestDataBuilder {

    public static PersonDataResponse aPersonDataResponse(Long personId) {
        return new PersonDataResponse(personId, "a name", "a country", "a passport number");
    }
}
