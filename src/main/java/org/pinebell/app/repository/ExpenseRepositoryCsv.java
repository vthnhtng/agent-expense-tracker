package org.pinebell.app.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import java.io.Writer;
import java.io.Reader;
import java.io.UncheckedIOException;

import org.apache.commons.csv.CSVParser;

import org.pinebell.app.entity.Expense;
import org.pinebell.app.entity.ExpenseCategory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

public class ExpenseRepositoryCsv implements ExpenseRepository {
    private final Path csvPath = Paths.get(System.getProperty("user.home"), "my_expenses.csv");
    private static final String[] HEADERS = {
            "id", "amount", "currency", "category", "description", "createdAt", "updatedAt"
    };
    private final Reader reader;
    private final CSVParser parser;
    private final Writer writer;
    private final CSVPrinter printer;

    public ExpenseRepositoryCsv() {
        if (!Files.exists(csvPath)) {
            try {
                createExpenseCSVFile();
            } catch (IOException e) {
                throw new RuntimeException(
                        "Expense CSV file is not found, failed to create expense CSV file: " + csvPath, e);
            }
        }

        try {
            reader = Files.newBufferedReader(csvPath);

            CSVFormat csvFormatForParser = CSVFormat.DEFAULT.builder()
                    .setHeader(HEADERS)
                    .setSkipHeaderRecord(true)
                    .build();

            parser = new CSVParser(reader, csvFormatForParser);
            writer = Files.newBufferedWriter(csvPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize expense CSV file: " + csvPath, e);
        }
    }

    private void createExpenseCSVFile() throws IOException {
        Files.createDirectories(csvPath.getParent());
        Files.createFile(csvPath);

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(HEADERS)
                .build();

        try (Writer writer = Files.newBufferedWriter(csvPath);
                CSVPrinter printer = new CSVPrinter(writer, csvFormat)) {
            printer.flush();
        }
    }

    public void create(Expense expense) throws RuntimeException {
        try {
            printer.printRecord(
                    expense.id(),
                    expense.amount(),
                    expense.currency(),
                    expense.category().name(),
                    expense.description(),
                    expense.createdAt(),
                    expense.updatedAt());
            printer.flush();
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to create expense: " + expense.toString(),
                    e);
        }
    }

    public List<Expense> findAll() throws RuntimeException {
        try {
            return parser.getRecords().stream()
                    .map(record -> new Expense(
                            UUID.fromString(record.get("id")),
                            new BigDecimal(record.get("amount")),
                            record.get("currency"),
                            ExpenseCategory.valueOf(record.get("category")),
                            record.get("description"),
                            LocalDateTime.parse(record.get("createdAt")),
                            LocalDateTime.parse(record.get("updatedAt"))))
                    .collect(Collectors.toList());
        } catch (UncheckedIOException e) {
            throw new RuntimeException("Failed to get expenses: " + e.getStackTrace(), e);
        }
    }
}
