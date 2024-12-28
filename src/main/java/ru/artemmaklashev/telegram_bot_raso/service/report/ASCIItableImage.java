package ru.artemmaklashev.telegram_bot_raso.service.report;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class ASCIItableImage {
    private Map<String, Integer> tableData;
    private List<String> tableHeaders;

    public ASCIItableImage(Map<String, Integer> tableData, List<String> tableHeaders) {
        this.tableData = tableData;
        this.tableHeaders = tableHeaders;
    }

    public BufferedImage drawTable() {
        // Определяем размеры изображения
        int width = 350; // Ширина изображения
        int height = 200; // Высота изображения

        // Создаем пустое изображение
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        // Настройки графики
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Используем моноширинный шрифт
        g.setColor(Color.BLACK);

        // Рисуем таблицу
        int x = 20; // Начальная позиция по X
        int y = 20; // Начальная позиция по Y
        int rowHeight = 20;

        // Рисуем заголовки
        g.drawString(tableHeaders.get(0), x, y);
        g.drawString(tableHeaders.get(1), x + 250, y);  // Отступ для второго столбца
        y += rowHeight;

        // Рисуем данные таблицы
        for (Map.Entry<String, Integer> entry : tableData.entrySet()) {
            g.drawString(entry.getKey(), x, y);
            g.drawString(entry.getValue().toString(), x + 250, y);
            y += rowHeight;
        }
        String total = "Итого: " + tableData.values().stream().mapToInt(Integer::intValue).sum();
        g.drawString(total, x, y+ + rowHeight);
        // Завершаем рисование
        g.dispose();

        // Возвращаем изображение
        return image;
    }

    // Метод для сохранения изображения в файл
//    public void saveImage() {
//        try {
//            BufferedImage image = drawTable();
//            ImageIO.write(image, "png", new File("table_image.png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}

