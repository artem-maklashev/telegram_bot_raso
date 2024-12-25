package ru.artemmaklashev.telegram_bot_raso.charts;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class SampleCgart {

    public static BufferedImage ProductionTable(Map<String, Integer> data) {
        String[] columnNames = {"Наименование", "Кол-во"};
        Object[][] dataset = new Object[data.size()][];

        int index = 0; // Maintain a separate index
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            dataset[index++] = new Object[]{entry.getKey(), entry.getValue()};
        }

        JTable table = new JTable(dataset, columnNames);

        // Настройка размеров и столбцов
        table.getColumnModel().getColumn(0).setPreferredWidth(200); // Установить ширину первого столбца
        table.getColumnModel().getColumn(0).setMinWidth(150);       // Минимальная ширина столбца
        table.getColumnModel().getColumn(0).setMaxWidth(300);       // Максимальная ширина столбца
        table.setSize(table.getPreferredSize());

        return createImageFromTable(table);
    }

    // Метод для создания изображения из JTable (с заголовками колонок)
    public static BufferedImage createImageFromTable(JTable table) {
        table.doLayout();

        // Рендеринг заголовка таблицы
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setSize(table.getWidth(), tableHeader.getHeight());

        // Высота таблицы = заголовок + тело таблицы
        int totalHeight = tableHeader.getHeight() + table.getHeight();
        int originalWidth = table.getWidth();

        // Ширина, ограниченная 500px (масштабирование пропорционально)
        int targetWidth = Math.min(500, originalWidth);
        int targetHeight = (int) (totalHeight * ((double) targetWidth / originalWidth));

        // Создаём оригинальное изображение
        BufferedImage originalImage = new BufferedImage(
                originalWidth, totalHeight,
                BufferedImage.TYPE_INT_ARGB
        );

        // Рисуем заголовок и тело таблицы на оригинальном изображении
        Graphics2D g2d = originalImage.createGraphics();
        tableHeader.paint(g2d);
        g2d.translate(0, tableHeader.getHeight());
        table.paint(g2d);
        g2d.dispose();

        // Создаём масштабированное изображение с ограниченной шириной
        BufferedImage scaledImage = new BufferedImage(
                targetWidth, targetHeight,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2dScaled = scaledImage.createGraphics();
        g2dScaled.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2dScaled.dispose();

        return scaledImage;
    }


}
