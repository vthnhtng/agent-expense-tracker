package org.pinebell.app.repository;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import org.pinebell.app.domain.Expense;
import org.pinebell.app.domain.ExpenseCategory;

public class CsvExpenseRepository implements ExpenseRepository {

    private static final String[] HEADERS = {
            "id", "amount", "currency", "category", "description", "createdAt", "updatedAt"
    };

    private static final CSVFormat CSV_READ_FORMAT = CSVFormat.DEFAULT.builder()
            .setHeader(HEADERS)
            .setSkipHeaderRecord(true)
            .build();

    private static final CSVFormat CSV_WRITE_FORMAT = CSVFormat.DEFAULT;

    private final Path csvPath;

    public CsvExpenseRepository() {
        this.csvPath = Paths.get(System.getProperty("user.home"), "my_expenses.csv");
        initCsvFile();
    }

    public CsvExpenseRepository(Path csvPath) {
        this.csvPath = csvPath;
        initCsvFile();
    }

    private void initCsvFile() {
        if (!Files.exists(csvPath)) {
            try {
                Files.createDirectories(csvPath.getParent());
                Files.createFile(csvPath);
                try (Writer writer = Files.newBufferedWriter(csvPath);
                        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.builder()
                                .setHeader(HEADERS)
                                .build())) {
                    printer.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to create expense CSV file: " + csvPath, e);
            }
        }
    }

    @Override
    public void save(Expense expense) {
        try (Writer writer = Files.newBufferedWriter(csvPath, StandardOpenOption.APPEND);
                CSVPrinter printer = new CSVPrinter(writer, CSV_WRITE_FORMAT)) {
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
            throw new RuntimeException("Failed to save expense: " + expense, e);
        }
    }

    @Override
    public List<Expense> findAll() {
        try (Reader reader = Files.newBufferedReader(csvPath);
                CSVParser parser = new CSVParser(reader, CSV_READ_FORMAT)) {
            return parser.getRecords().stream()
                    .map(this::mapToExpense)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read expenses from: " + csvPath, e);
        }
    }

    @Override
    public List<Expense> findByDate(LocalDate date) {
        return findAll().stream()
                .filter(expense -> expense.createdAt().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    private Expense mapToExpense(org.apache.commons.csv.CSVRecord record) {
        return new Expense(
                UUID.fromString(record.get("id")),
                new BigDecimal(record.get("amount")),
                record.get("currency"),
                ExpenseCategory.valueOf(record.get("category")),
                record.get("description"),
                LocalDateTime.parse(record.get("createdAt")),
                LocalDateTime.parse(record.get("updatedAt")));
    }
}
