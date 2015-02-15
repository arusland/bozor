package com.arusland.bozor.service.impl;

import com.arusland.bozor.service.EncodingSupportProvider;
import com.arusland.bozor.util.EncodingUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by ruslan on 16.02.2015.
 */
@Service
public class EncodingSupportProviderImpl implements EncodingSupportProvider {
    @Value("${bozor.encoding.tomcat7hack}")
    private Boolean useEncodingConvert;

    @Override
    public String normalize(String value) {
        if (value != null) {
            return useEncodingConvert != null && useEncodingConvert ? EncodingUtils.normalizeValue(value) : value;
        }

        return null;
    }
}
