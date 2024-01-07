package com.b6.mypaldotrip.domain.sample.service;

import com.b6.mypaldotrip.domain.sample.controller.dto.request.SampleReq;
import com.b6.mypaldotrip.domain.sample.controller.dto.response.SampleRes;
import com.b6.mypaldotrip.domain.sample.exception.SampleErrorCode;
import com.b6.mypaldotrip.domain.sample.store.entity.SampleEntity;
import com.b6.mypaldotrip.domain.sample.store.repository.SampleRepository;
import com.b6.mypaldotrip.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleRepository sampleRepository;

    public SampleRes saveSample(SampleReq req) {
        if (req.title().isEmpty()) {
            throw new GlobalException(SampleErrorCode.NO_TITLE_ERROR);
        }
        SampleEntity sample = SampleEntity.builder().title(req.title()).build();
        sampleRepository.save(sample);
        return SampleRes.builder().title(sample.getTitle()).build();
    }
}
