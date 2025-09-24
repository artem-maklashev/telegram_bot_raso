package ru.artemmaklashev.telegram_bot_raso.service.html;

import org.jetbrains.annotations.NotNull;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.render.BlockBox;
import org.w3c.dom.Element;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;


public class CustomReplacedElementFactory implements ReplacedElementFactory {
    private final ReplacedElementFactory superFactory;

    public CustomReplacedElementFactory(ReplacedElementFactory superFactory) {
        this.superFactory = superFactory;
    }

    @Override
    public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box,
                                                 UserAgentCallback uac, int cssWidth,
                                                 int cssHeight) {
        // Здесь можно добавить специальную обработку элементов
        return superFactory.createReplacedElement(c, box, uac, cssWidth, cssHeight);
    }

    @Override
    public void reset() {
        superFactory.reset();
    }

    @Override
    public void remove(Element e) {
        superFactory.remove(e);
    }

    @Override
    public void setFormSubmissionListener(@NotNull FormSubmissionListener formSubmissionListener) {

    }
}