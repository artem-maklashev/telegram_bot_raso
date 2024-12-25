package ru.artemmaklashev.telegram_bot_raso.entity.production;

import lombok.Getter;
import ru.artemmaklashev.telegram_bot_raso.entity.Product;
import ru.artemmaklashev.telegram_bot_raso.entity.ProductCategories;


@Getter
public abstract class Production<U extends ProductCategories, T extends Product> {
    private U category;
    private T product;
    private Float value;
    private ProductionList productionList;
    private int id;
}
