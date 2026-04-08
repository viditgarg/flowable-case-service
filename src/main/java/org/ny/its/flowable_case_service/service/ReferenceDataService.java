package org.ny.its.flowable_case_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ReferenceDataService {
    public List<String> getGenderOptions() {
        return List.of("Male", "Female", "Other", "Prefer not to say");
    }

    public List<String> getIncarcerationOptions() {
        return List.of("Incarcerated", "Not Incarcerated", "On Parole");
    }

    public List<String> getSsnValidationOptions() {
        return List.of("true", "false");
    }

}
