module com.sikora.minessweepercik {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.sikora.minessweepercik to javafx.fxml;
    exports com.sikora.minessweepercik;
}