package org.ny.its.flowable_case_service.service;


import org.flowable.engine.RuntimeService;
import org.ny.its.flowable_case_service.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CaseWorkflowService {

    @Autowired
    private RuntimeService runtimeService;

    public String startCase(Person person) {

        Map<String, Object> vars = new HashMap<>();
        vars.put("firstName", person.getFirstName());
        vars.put("lastName", person.getLastName());
        vars.put("ssn", person.getSsn());
        vars.put("email", person.getEmail());
        vars.put("gender", person.getGender());
        vars.put("dateofbirth", person.getDateofbirth());

        return runtimeService
                .startProcessInstanceByKey("case_regVN10", vars)
                .getId();
    }
}
