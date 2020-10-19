module Java.test {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.base;
    requires java.sql;
    requires opencsv;



    opens sample;
    opens clasessForTable;
    opens graph;
}