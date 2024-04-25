package com.person.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Person {
    Long id;
    String name;
    String country;
}
