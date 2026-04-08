package org.ny.its.flowable_case_service.service;

import lombok.RequiredArgsConstructor;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("incarcerationValidationService")
@RequiredArgsConstructor
public class IncarcerationValidationService implements JavaDelegate {

    private final RuntimeService runtimeService;

    private final Logger log = LoggerFactory.getLogger(IncarcerationValidationService.class);

    @Override
    public void execute(DelegateExecution execution) {
        log.info("validating incarceration status for ::" + execution.getVariable("firstName") + " "
                + execution.getVariable("lastName"));

        // boolean incarcerated = false;

        // To - Do
        // external services to be called
        // call doccs and rikers services
        execution.setVariable("incarcerationStatus", "Incarcerated");
        log.info("variable set: " + execution.getVariable("incarcerationStatus"));
    }
}
