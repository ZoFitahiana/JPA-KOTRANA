package com.fresh.coding.attachapijavains3.services.impl;

import com.fresh.coding.attachapijavains3.entities.Category;
import com.fresh.coding.attachapijavains3.services.CategoryService;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final String CATEGORIES_CSV_PATH = "src/main/resources/s3_database/categories.csv";
    private static final String TEMP_CSV_PATH = "src/main/resources/s3_database/temp_categories.csv";

    @Override
    public List<Category> findAllCategories() {
        ensureFilesExist();
        return readCategoriesFromFile();
    }

    @SneakyThrows
    @Override
    public List<Category> saveAllCategories(List<Category> categories) {
        ensureFilesExist();
        Path csvPath = Paths.get(CATEGORIES_CSV_PATH);
        Path tempCsvPath = Paths.get(TEMP_CSV_PATH);
        var existingCategoriesMap = new HashMap<Long, Category>();

        if (Files.exists(csvPath)) {
            @Cleanup BufferedReader reader = Files.newBufferedReader(csvPath);
            @Cleanup CsvBeanReader csvReader = new CsvBeanReader(reader, CsvPreference.STANDARD_PREFERENCE);
            String[] header = csvReader.getHeader(true);
            Category category;
            while ((category = csvReader.read(Category.class, header)) != null) {
                existingCategoriesMap.put(Long.valueOf(category.getId()), category);
            }
        }

        for (var category : categories) {
            existingCategoriesMap.put(Long.valueOf(category.getId()), category);
        }

        @Cleanup BufferedWriter writer = Files.newBufferedWriter(tempCsvPath);
        @Cleanup CsvBeanWriter csvWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
        String[] header = {"id", "name", "description"};
        csvWriter.writeHeader(header);
        for (var category : existingCategoriesMap.values()) {
            csvWriter.write(category, header);
        }

        Files.move(tempCsvPath, csvPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        return categories;
    }

    @SneakyThrows
    @Override
    public Category findById(Long id) {
        ensureFilesExist();
        Path csvPath = Paths.get(CATEGORIES_CSV_PATH);

        @Cleanup BufferedReader reader = Files.newBufferedReader(csvPath);
        @Cleanup CsvBeanReader csvReader = new CsvBeanReader(reader, CsvPreference.STANDARD_PREFERENCE);
        String[] header = csvReader.getHeader(true);
        Category category;
        while ((category = csvReader.read(Category.class, header)) != null) {
            if (Long.valueOf(category.getId()).equals(id)) {
                return category;
            }
        }

        throw new IllegalArgumentException("Category not found");
    }

    @SneakyThrows
    @Override
    public void deleteById(Long id) {
        ensureFilesExist();
        Path csvPath = Paths.get(CATEGORIES_CSV_PATH);
        Path tempCsvPath = Paths.get(TEMP_CSV_PATH);
        boolean removed = false;

        @Cleanup BufferedReader reader = Files.newBufferedReader(csvPath);
        @Cleanup CsvBeanReader csvReader = new CsvBeanReader(reader, CsvPreference.STANDARD_PREFERENCE);
        @Cleanup BufferedWriter writer = Files.newBufferedWriter(tempCsvPath);
        @Cleanup CsvBeanWriter csvWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
        String[] header = {"id", "name", "description"};
        csvWriter.writeHeader(header);
        Category category;
        while ((category = csvReader.read(Category.class, header)) != null) {
            if (Long.valueOf(category.getId()).equals(id)) {
                removed = true;
            } else {
                csvWriter.write(category, header);
            }
        }

        if (removed) {
            Files.move(tempCsvPath, csvPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.deleteIfExists(tempCsvPath);
        }
    }

    @SneakyThrows
    private List<Category> readCategoriesFromFile() {
        Path path = Paths.get(CATEGORIES_CSV_PATH);
        if (!Files.exists(path)) {
            return List.of();
        }
        @Cleanup BufferedReader reader = Files.newBufferedReader(path);
        @Cleanup CsvBeanReader csvReader = new CsvBeanReader(reader, CsvPreference.STANDARD_PREFERENCE);
        String[] header = csvReader.getHeader(true);
        List<Category> categories = new ArrayList<>();
        Category category;
        while ((category = csvReader.read(Category.class, header)) != null) {
            categories.add(category);
        }
        return categories;
    }

    @SneakyThrows
    private void ensureFilesExist() {
        Path categoriesPath = Paths.get(CATEGORIES_CSV_PATH);
        if (!Files.exists(categoriesPath)) {
            @Cleanup BufferedWriter writer = Files.newBufferedWriter(categoriesPath);
            @Cleanup CsvBeanWriter csvWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
            csvWriter.writeHeader("id", "name", "description");
        }
    }
}
