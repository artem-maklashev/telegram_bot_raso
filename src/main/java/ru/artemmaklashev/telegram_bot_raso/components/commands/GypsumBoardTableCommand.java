package ru.artemmaklashev.telegram_bot_raso.components.commands;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.thymeleaf.spring6.SpringTemplateEngine;
import ru.artemmaklashev.telegram_bot_raso.components.MessageService;
import ru.artemmaklashev.telegram_bot_raso.entity.outdata.GypsumBoardProductionData;
import ru.artemmaklashev.telegram_bot_raso.service.html.HtmlProductionTable;
import ru.artemmaklashev.telegram_bot_raso.service.html.HtmlToImageConverter;
import ru.artemmaklashev.telegram_bot_raso.service.reportServices.gypsumBoard.GypsumBoardReportService;
import ru.artemmaklashev.telegram_bot_raso.service.telegram.RequestLoggingService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component("gypsumBoardTable")
public class GypsumBoardTableCommand implements Command{


    private final GypsumBoardReportService service;
    private final SpringTemplateEngine templateEngine;
    private final HtmlToImageConverter htmlToImageConverter = new HtmlToImageConverter();
    private final RequestLoggingService loggingService;
    private final MessageService messageService;

    public GypsumBoardTableCommand(GypsumBoardReportService service, @Qualifier("templateEngine") SpringTemplateEngine templateEngine, RequestLoggingService loggingService, MessageService messageService) {
        this.service = service;
        this.templateEngine = templateEngine;
        this.loggingService = loggingService;
        this.messageService = messageService;
    }

    @Override
    public Object execute(Update update) {
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        String userName = update.getCallbackQuery().getFrom().getFirstName() + " " + update.getCallbackQuery().getFrom().getLastName();
        logRequest(update.getCallbackQuery().getFrom().getId(), Long.parseLong(chatId),userName, "gypsumBoardTable");
        sendAdminMessage("Пользователь " + userName + " запросил отчет по план-факт за месяц");
        List<GypsumBoardProductionData> productionData = service.getProductionsByInterval();
        if (!productionData.isEmpty()) {
            HtmlProductionTable htmlProductionTable = new HtmlProductionTable(templateEngine);
            String html = htmlProductionTable.render(productionData);
            byte[] bytes ;
            try {
                bytes = htmlToImageConverter.convertHtmlToImage(html);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//            return SendMessage.builder()
//                    .chatId(chatId)
//                    .text("отчет сгенерирован")
//                    .build();
            if (bytes != null) {
                return SendPhoto.builder()
                        .chatId(chatId)
                        .photo(new InputFile(new ByteArrayInputStream(bytes), "report.png"))
                        .caption("Отчет сгенерирован")
                        .build();
            } else {
                return EditMessageText.builder()
                        .chatId(chatId)
                        .messageId(messageId)
                        .text("Произошла ошибка при формировании отчета.")
                        .build();
            }

        } else {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Нет данных")
                    .build();
        }
    }

    @Override
    public void sendAdminMessage(String text) {
        messageService.sendAdminMessage(text);
    }

    @Override
    public void logRequest(Long userId, Long chatId, String text, String command) {
        CompletableFuture.runAsync(() -> loggingService.logRequest(userId, chatId, text, command));
    }
}
