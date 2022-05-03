module Lab4.MPP.TeledonClientFX.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires Lab4.MPP.TeledonServices.main;
    requires Lab4.MPP.TeledonnNetworking.main;
    requires Lab4.MPP.TeledonModel.main;
    exports clientFX.main to javafx.fxml;
    opens clientFX.main;
}