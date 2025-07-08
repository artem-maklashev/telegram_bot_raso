package ru.artemmaklashev.telegram_bot_raso.service.html;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import ru.artemmaklashev.telegram_bot_raso.entity.outdata.GypsumBoardProductionData;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Service
public class HtmlProductionTable {
    private final SpringTemplateEngine templateEngine;


    public HtmlProductionTable(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;

    }
    public String render(List<GypsumBoardProductionData> data) {

        Context context = new Context();
        context.setVariable("title", "План-факт за период: " + LocalDate.now().withDayOfMonth(1) + " - " + LocalDate.now().minusDays(1));
        context.setVariable("headers", List.of("Наименование", "План", "Факт", "Отклонение", "% брака"));
        context.setVariable("items", data);
        String html = templateEngine.process("table-template", context);

        try {
            Files.writeString(Paths.get("output.html"), html);
            System.out.println("HTML сохранен в output.html");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return html;
    }
}
