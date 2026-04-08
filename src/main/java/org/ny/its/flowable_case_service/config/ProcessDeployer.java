package org.ny.its.flowable_case_service.config;

import org.flowable.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProcessDeployer {

    @Autowired
    private RepositoryService repositoryService;

    @PostConstruct
    public void deploy() {

        repositoryService.createDeployment()
                .addClasspathResource("processes/Case_Registration_VN11_T.bpmn20.xml")
                .tenantId("CASE_APP")
                .deploy();
    }
}
