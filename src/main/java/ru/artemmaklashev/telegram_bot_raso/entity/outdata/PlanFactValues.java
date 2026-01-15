package ru.artemmaklashev.telegram_bot_raso.entity.outdata;

import lombok.Data;

@Data
public class PlanFactValues {
    private float planValue;
    private float factValue;

    public PlanFactValues(float planValue, float factValue) {
        this.planValue = planValue;
        this.factValue = factValue;
    }
}
