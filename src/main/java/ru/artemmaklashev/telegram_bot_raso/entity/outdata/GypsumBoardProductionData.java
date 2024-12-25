package ru.artemmaklashev.telegram_bot_raso.entity.outdata;

public class GypsumBoardProductionData {
    private String boardTitle;
    private float planValue;
    private float factValue;
    private float defectiveValue;
    private float total;

    public GypsumBoardProductionData(String boardTitle, float total, float planValue, float factValue, float defectiveValue) {
        this.total = total;
        this.boardTitle = boardTitle;
        this.planValue = planValue;
        this.factValue = factValue;
        this.defectiveValue = defectiveValue;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    public float getPlanValue() {
        return planValue;
    }

    public void setPlanValue(float planValue) {
        this.planValue = planValue;
    }

    public float getFactValue() {
        return factValue;
    }

    public void setFactValue(float factValue) {
        this.factValue = factValue;
    }

    public float getDefectiveValue() {
        return defectiveValue;
    }

    public void setDefectiveValue(float defectiveValue) {
        this.defectiveValue = defectiveValue;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void addValues(float planValue, float total, float factValue, float defectiveValue){
        this.total+=total;
        this.planValue+=planValue;
        this.factValue+=factValue;
        this.defectiveValue+=defectiveValue;
    }
}
