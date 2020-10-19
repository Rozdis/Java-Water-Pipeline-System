package clasessForTable;

import javafx.beans.value.ObservableValue;

public class TableColumnData {
    private Integer startPoint;
    private Integer finishPoint;
    private Integer length;

    public TableColumnData(Integer startPoint, Integer finishPoint, Integer length) {
        this.startPoint = startPoint;
        this.finishPoint = finishPoint;
        this.length = length;
    }

    public Integer getStartPoint() {
        return startPoint;
    }

     public void changed(ObservableValue<? extends TableColumnData> observable, TableColumnData oldValue, TableColumnData newValue) {

    }

    public void setStartPoint(Integer startPoint) {
        this.startPoint = startPoint;
    }

    public Integer getFinishPoint() {
        return finishPoint;
    }

    public void setFinishPoint(Integer finishPoint) {
        this.finishPoint = finishPoint;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
