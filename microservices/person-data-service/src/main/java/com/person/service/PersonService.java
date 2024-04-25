package com.person.service;

import com.person.model.Person;
import com.person.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Person getPersonById(Long id) {
        return personRepository.getPersonById(id);
    }

    public List<Person> getPersons() {
        return personRepository.getPersons();
    }

}
