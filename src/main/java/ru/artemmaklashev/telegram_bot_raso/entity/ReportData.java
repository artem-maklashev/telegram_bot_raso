package ru.artemmaklashev.telegram_bot_raso.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.artemmaklashev.telegram_bot_raso.entity.delays.Delays;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.BoardDefectsLog;
import ru.artemmaklashev.telegram_bot_raso.entity.production.Production;
import ru.artemmaklashev.telegram_bot_raso.entity.production.ProductionList;


import java.util.List;



@Data
@NoArgsConstructor
public class ReportData<T extends Product, U extends ProductCategories, V extends Production<U, T>, X extends Delays<T>> {
    private ProductionList productionList;
    private T product;
    private List<V> productions;
    private List<X> delays;
    private List<BoardDefectsLog> defectsLogs;

    public ReportData(ProductionList productionList, List<V> productions, T product, List<X> delays, List<BoardDefectsLog> defectsLogs) {
        this.productionList = productionList;
        this.productions = productions;
        this.product = product;
        this.delays = delays;
        this.defectsLogs = defectsLogs;
    }


}
