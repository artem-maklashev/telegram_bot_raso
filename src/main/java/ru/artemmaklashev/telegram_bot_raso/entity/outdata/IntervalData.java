package ru.artemmaklashev.telegram_bot_raso.entity.outdata;

import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.Plan;
import ru.artemmaklashev.telegram_bot_raso.entity.production.BoardProduction;

import java.util.List;
import java.util.Objects;

public class IntervalData {
    private final List<BoardProduction> productions;
    private final List<Plan> plans;

    public IntervalData(List<BoardProduction> productions, List<Plan> plans) {
        this.productions = Objects.requireNonNull(productions);
        this.plans = Objects.requireNonNull(plans);
    }

    public List<BoardProduction> getProductions() {
        return productions;
    }

    public List<Plan> getPlans() {
        return plans;
    }
}
