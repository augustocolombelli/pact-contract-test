package com.person.controller;

import com.person.model.Person;
import com.person.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<Person> getPersons() {
        log.info("Get persons API requested.");
        return personService.getPersons();
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Person getPersonById(@PathVariable Long id) {
        log.info("Get person by id {} API requested.", id);
        return personService.getPersonById(id);
    }

}
