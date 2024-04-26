package com.example.demo.task1;

import com.example.demo.DatabaseConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Task1Controller implements Initializable {
    @FXML
    private Label taskConditionLabel;

    @FXML
    private DatePicker startPick, endPick;

    @FXML
    private TableView<Object[]> table;

    @FXML
    private TableColumn<Object[], String> idCol, pibCol, categoryCol, sumCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Ініціалізація інтерфейсу та встановлення подій для елементів керування
        taskConditionLabel.setText("Загальна сума продажів за кожним касиром за обраний період часу в залежності від категорії товару.");

        idCol.setCellValueFactory(cellData -> cellData.getValue()[0] != null ? new SimpleStringProperty(cellData.getValue()[0].toString()) : null);
        pibCol.setCellValueFactory(cellData -> cellData.getValue()[1] != null ? new SimpleStringProperty(cellData.getValue()[1].toString()) : null);
        categoryCol.setCellValueFactory(cellData -> cellData.getValue()[2] != null ? new SimpleStringProperty(cellData.getValue()[2].toString()) : null);
        sumCol.setCellValueFactory(cellData -> cellData.getValue()[3] != null ? new SimpleStringProperty(cellData.getValue()[3].toString()) : null);

        startPick.setValue(LocalDate.now());
        endPick.setValue(LocalDate.now());

        startPick.setOnAction(event -> updateData());
        endPick.setOnAction(event -> updateData());

        updateData(); // Оновлення даних після завантаження інтерфейсу
    }

    private void updateData() {
        // Отримання дат початку і кінця періоду з відповідних елементів управління
        LocalDate startDate = startPick.getValue();
        LocalDate endDate = endPick.getValue();

        if (startDate != null && endDate != null) {
            try {
                // Встановлення з'єднання з базою даних
                DatabaseConnection connection = new DatabaseConnection();
                Connection connectDB = connection.getConnection();
                // Підготовка SQL-запиту з параметрами для вибору необхідних даних
                PreparedStatement statement = connectDB.prepareStatement("SELECT employee.id_employee, " +
                        "CONCAT(employee.empl_surname, ' ', employee.empl_name, ' ', employee.empl_patronymic) AS pib, " +
                        "category.category_name, " +
                        "SUM(`check`.sum_total) AS total_sales_amount " +
                        "FROM employee " +
                        "INNER JOIN `check` ON `check`.id_employee = employee.id_employee " +
                        "INNER JOIN sale ON sale.check_number = `check`.check_number " +
                        "INNER JOIN store_product ON sale.UPC = store_product.UPC " +
                        "INNER JOIN product ON product.id_product = store_product.id_product " +
                        "INNER JOIN category ON product.category_number = category.category_number " +
                        "WHERE `check`.print_date BETWEEN ? AND ? " +
                        "GROUP BY employee.id_employee, category.category_name");
                statement.setDate(1, Date.valueOf(startDate));
                statement.setDate(2, Date.valueOf(endDate));
                // Виконання запиту та отримання результатів
                ResultSet task1_q = statement.executeQuery();

                // Очищення таблиці перед додаванням нових даних
                table.getItems().clear();

                // Обробка результатів та додавання їх у таблицю
                while (task1_q.next()) {
                    String id = task1_q.getString("id_employee");
                    String pib = task1_q.getString("pib");
                    String category = task1_q.getString("category_name");
                    String sum = task1_q.getString("total_sales_amount");
                    Object[] row = {id, pib, category, sum};
                    table.getItems().add(row);
                }

                // Закриття з'єднання та інших ресурсів
                task1_q.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
