package com.tommy.board.controller;

import com.tommy.board.domain.dto.ApiResult;
import com.tommy.board.domain.dto.PreferenceCreateRequest;
import com.tommy.board.domain.dto.PreferenceCreateResponse;
import com.tommy.board.service.impl.PreferenceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PreferenceController {
    private PreferenceServiceImpl preferenceService;

    @Autowired
    public PreferenceController(PreferenceServiceImpl preferenceService) {
        this.preferenceService = preferenceService;
    }

    public ApiResult<PreferenceCreateResponse> createPreference(PreferenceCreateRequest preferenceCreateRequest) {
        return new ApiResult<PreferenceCreateResponse>(this.preferenceService.createPreference(preferenceCreateRequest));
    }
}
