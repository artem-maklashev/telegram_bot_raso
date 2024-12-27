package ru.artemmaklashev.telegram_bot_raso.service.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ASCIItable {
    private Map<String, Integer> tableData = new HashMap<>();
    private List<String> tableHeaders = new ArrayList<>();

    public ASCIItable(Map<String, Integer> tableData, List<String> tableHeaders) {
        this.tableData = tableData;
        this.tableHeaders = tableHeaders;
    }


    public String drawTable() {
        StringBuilder sb = new StringBuilder();

        sb.append("<pre>").append("\n"); // Открываем HTML блок для моноширинного текста

        // Создаем горизонтальную линию таблицы
        String horizontalLine = "+" + "-".repeat(getFirstColumnWidth()) + "+" + "-".repeat(getSecondColumnWidth()) + "+\n";
        sb.append(horizontalLine);

        // Заголовки таблицы
        sb.append("|").append("<b>"+tableHeaders.get(0)+"</b>")
                .append(" ".repeat(getFirstColumnWidth() - tableHeaders.get(0).length()))
                .append("|").append(tableHeaders.get(1))
                .append(" ".repeat(getSecondColumnWidth() - tableHeaders.get(1).length()))
                .append("|\n");
        sb.append(horizontalLine);

        // Данные таблицы
        for (Map.Entry<String, Integer> entry : tableData.entrySet()) {
            sb.append("|").append(entry.getKey())
                    .append(" ".repeat(getFirstColumnWidth() - entry.getKey().length()))
                    .append("|").append(entry.getValue())
                    .append(" ".repeat(getSecondColumnWidth() - entry.getValue().toString().length()))
                    .append("|\n");
            sb.append(horizontalLine);
        }

        sb.append("</pre>").append("\n"); // Закрываем HTML блок

        return sb.toString();
    }

    private int getFirstColumnWidth() {
        int maxWidth = 0;
        if (tableHeaders.get(0).length() > maxWidth) {
            maxWidth = tableHeaders.get(0).length();
        }
        for (Map.Entry<String, Integer> entry : tableData.entrySet()) {
            if (entry.getKey().length() > maxWidth) {
                maxWidth = entry.getKey().length();
            }
        }
        return maxWidth;
    }

    private int getSecondColumnWidth() {
        int maxWidth = 0;
        if (tableHeaders.get(1).length() > maxWidth) {
            maxWidth = tableHeaders.get(1).length();
        }
        for (Integer value : tableData.values()) {
            if (value.toString().length() > maxWidth) {
                maxWidth = value.toString().length();
            }
        }
        return maxWidth;
    }
}
