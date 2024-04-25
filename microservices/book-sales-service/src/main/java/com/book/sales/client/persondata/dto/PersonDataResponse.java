package com.book.sales.client.persondata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDataResponse {
    Long id;
    String name;
    String country;
    String passportNumber;
}
