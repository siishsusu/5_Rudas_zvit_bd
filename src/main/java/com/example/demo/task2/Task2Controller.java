package com.example.demo.task2;

import com.example.demo.DatabaseConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Task2Controller implements Initializable {
    @FXML
    private Label taskConditionLabel;

    @FXML
    private TableView<Object[]> table;

    @FXML
    private TableColumn<Object[], String> catNumCol, catNameCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        taskConditionLabel.setText("Всі категорії товарів, що не мають акційних товарів у магазині і не були жодного разу куплені (не було створено жодного чеку з ними).");

        catNumCol.setCellValueFactory(cellData -> cellData.getValue()[0] != null ? new SimpleStringProperty(cellData.getValue()[0].toString()) : null);
        catNameCol.setCellValueFactory(cellData -> cellData.getValue()[1] != null ? new SimpleStringProperty(cellData.getValue()[1].toString()) : null);

        try {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getConnection();
            Statement statement = connectDB.createStatement();
            ResultSet task2_q = statement.executeQuery("SELECT * " +
                    "FROM category " +
                    "WHERE category_number NOT IN ( " +
                    "SELECT category_number FROM product WHERE id_product " +
                    "IN ( SELECT id_product FROM store_product WHERE promotional_product = '1'))  " +
                    "AND category_number " +
                    "NOT IN (SELECT category_number FROM product p WHERE " +
                    "NOT EXISTS (SELECT * FROM sale WHERE UPC " +
                    "IN ( SELECT UPC FROM store_product sp WHERE sp.id_product = p.id_product)))");


            while (task2_q.next()) {
                String id = task2_q.getString("category_number");
                String name = task2_q.getString("category_name");
                Object[] row = {id, name};
                table.getItems().add(row);
            }

            task2_q.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
