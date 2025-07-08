package ru.artemmaklashev.telegram_bot_raso.service.html;

import com.lowagie.text.pdf.BaseFont;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class HtmlToImageConverter {

    public byte[] convertHtmlToImage(String html) throws IOException {
        // 1. Подготовка HTML
        String xhtml = prepareXhtml(html);

        // 2. Генерация PDF
        byte[] pdfBytes = generatePdfFromHtml(xhtml);

        // 3. Конвертация PDF в PNG
        return convertPdfToImage(pdfBytes);
    }

    private String prepareXhtml(String html) {
        // Убедимся, что это валидный XHTML
        if (!html.contains("xmlns=")) {
            html = html.replaceFirst("<html>", "<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        }

        // Закрываем все незакрытые теги
        html = html.replaceAll("<tr>\\s*<tr>", "<tr></tr><tr>");

        return html;
    }

    private byte[] generatePdfFromHtml(String html) throws IOException {
        try (ByteArrayOutputStream pdfStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();

            // Настройки для корректного отображения
            renderer.getSharedContext().setReplacedElementFactory(
                    new CustomReplacedElementFactory(
                            renderer.getSharedContext().getReplacedElementFactory()
                    )
            );
            renderer.getSharedContext().setBaseURL("templates/fonts");
            renderer.getFontResolver().addFont("templates/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(pdfStream);
            return pdfStream.toByteArray();
        }
    }

    private byte[] convertPdfToImage(byte[] pdfBytes) throws IOException {
        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBuffer(pdfBytes))) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage image = pdfRenderer.renderImageWithDPI(0, 300);

            try (ByteArrayOutputStream imageStream = new ByteArrayOutputStream()) {
                ImageIO.write(image, "png", imageStream);
                return imageStream.toByteArray();
            }
        }
    }
}