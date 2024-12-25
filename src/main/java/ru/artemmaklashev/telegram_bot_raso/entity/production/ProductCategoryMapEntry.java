package ru.artemmaklashev.telegram_bot_raso.entity.production;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.artemmaklashev.telegram_bot_raso.entity.ProductCategories;

@Data
@AllArgsConstructor
public class ProductCategoryMapEntry<T extends ProductCategories> {
    private T category;
    private Float value;
}
