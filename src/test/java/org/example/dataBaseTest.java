package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.*;

public class dataBaseTest {

    private static final String URL = "jdbc:h2:tcp://localhost:9092/mem:testdb";
    private static final String USER = "user";
    private static final String PASSWORD = "pass";
    @Test
    public void testAddNewProductToDatabase() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Шаг 1: Добавление нового товара
            Statement statement = connection.createStatement();
            int updateCount = statement.executeUpdate("INSERT INTO FOOD VALUES (5, 'Авокадо', 'FRUIT', 1)");
            assertEquals(1, updateCount, "Ожидается сообщение: Update count: 1");

            // Шаг 2: Проверка добавления товара
            ResultSet resultSet = statement.executeQuery("SELECT * FROM FOOD WHERE FOOD_ID = 5");
            if (resultSet.next()) {
                int foodId = resultSet.getInt("FOOD_ID");
                String foodName = resultSet.getString("FOOD_NAME");
                String foodType = resultSet.getString("FOOD_TYPE");
                int foodExotic = resultSet.getInt("FOOD_EXOTIC");
                assertEquals(5, foodId);
                assertEquals("Авокадо", foodName);
                assertEquals("FRUIT", foodType);
                assertEquals(1, foodExotic);
            }

            // Шаг 3: Проверка вывода всей таблицы
            resultSet = statement.executeQuery("SELECT * FROM FOOD");
            boolean avocadoFound = false;
            while (resultSet.next()) {
                if (resultSet.getString("FOOD_NAME").equals("Авокадо")) {
                    avocadoFound = true;
                    break;
                }
            }
            assertEquals(true, avocadoFound, "Ожидается наличие товара 'Авокадо' в таблице");

            // Удаление созданного товара
            statement.executeUpdate("DELETE FROM FOOD WHERE FOOD_ID = 5");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
