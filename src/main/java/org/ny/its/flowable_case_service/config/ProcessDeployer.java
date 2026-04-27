package org.ny.its.flowable_case_service.config;

import java.util.List;
import java.util.Map;

import org.flowable.engine.RepositoryService;
import org.ny.its.flowable_case_service.util.AppliationConstants;
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
        
        Map<String, String> tenantProcessMap = Map.of("CASE_APP", "processes/Case_Registration_V12.bpmn20.xml",
            "EECM_APP", "processes/Case_Registration_V12.bpmn20.xml",
            "FM_APP", "processes/Case_Registration_V12.bpmn20.xml"
        );
        
        
        tenantProcessMap.forEach((tenant, processFile) -> {
                 repositoryService.createDeployment()
                 .name("Deployment_"+tenant)
                 .key("Deployment_"+tenant)
                 .addClasspathResource("processes/Case_Registration_V12.bpmn20.xml")
                 .tenantId(tenant)
                 .enableDuplicateFiltering()
                 .deploy();
        });
        
        // List<String> TENANTS = List.of("CASE_APP", "EECM_APP", "FM_APP");

        // for (String tenant : TENANTS) {
        //     repositoryService.createDeployment()
        //             .name("Case_Registration_VN11_T" + "_tenant")
        //             .addClasspathResource("processes/Case_Registration_VN11_T.bpmn20.xml")
        //             .tenantId(tenant)
        //             .enableDuplicateFiltering()
        //             .deploy();
        // }

    }
}
