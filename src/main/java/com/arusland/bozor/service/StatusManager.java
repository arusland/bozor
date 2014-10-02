package com.arusland.bozor.service;

import com.arusland.bozor.dto.StatusResult;

/**
 * Created by ruslan on 02.10.2014.
 */
public interface StatusManager {

    StatusResult hasUpdates(String token);

    void modifyItems();

    void modifyProducts();
}
