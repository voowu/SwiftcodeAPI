package com.excercises.swiftAPI.initializers;

import com.excercises.swiftAPI.services.CsvDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    private final CsvDataService csvDataService;

    @Autowired
    public DatabaseInitializer(CsvDataService csvDataService) {
        this.csvDataService = csvDataService;
        LoadBanks();
    }

    private void LoadBanks() {
        csvDataService.uploadDatabaseFromLocalFile();
    }
}

