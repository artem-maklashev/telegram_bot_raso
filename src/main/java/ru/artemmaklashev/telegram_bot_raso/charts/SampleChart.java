package ru.artemmaklashev.telegram_bot_raso.charts;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class SampleChart {

    // Метод для создания изображения таблицы на основе данных
    public static BufferedImage createProductionTable(Map<String, Integer> data) {
        // Создание таблицы и колонок
        TableView<Map.Entry<String, Integer>> table = new TableView<>();
        TableColumn<Map.Entry<String, Integer>, String> nameColumn = new TableColumn<>("Наименование");
        TableColumn<Map.Entry<String, Integer>, Integer> quantityColumn = new TableColumn<>("Кол-во");

        // Настройка колонок
        nameColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getKey()));
        quantityColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(param.getValue().getValue()));

        table.getColumns().add(nameColumn);
        table.getColumns().add(quantityColumn);

        // Заполнение таблицы данными
        table.getItems().addAll(data.entrySet());

        // Настройка ширины колонок
        nameColumn.setPrefWidth(200);
        quantityColumn.setPrefWidth(100);

        // Создание сцены для рендеринга
        Scene scene = new Scene(table, 500, 400);
        Stage stage = new Stage();
        stage.setScene(scene);

        // Отображаем сцену на JavaFX-платформе
        Platform.runLater(() -> {
            WritableImage image = table.snapshot(null, null);
            BufferedImage bufferedImage = javafx.embed.swing.SwingFXUtils.fromFXImage(image, null);
            saveBufferedImage(bufferedImage);
            stage.close(); // Закрываем окно после создания снимка
        });

        return null;  // Поскольку изображение сохраняется асинхронно, возвращаем null
    }

    // Метод для сохранения BufferedImage в файл
    private static void saveBufferedImage(BufferedImage image) {
        try {
            File file = new File("table_image.png");
            javax.imageio.ImageIO.write(image, "PNG", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
