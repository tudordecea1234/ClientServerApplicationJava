module Lab4.MPP.TeledonnNetworking.main {
    requires Lab4.MPP.TeledonModel.main;
    requires Lab4.MPP.TeledonServices.main;
    requires java.rmi;
    requires com.google.protobuf;
    exports objectProtocol;
    exports utils;
    exports protoBuffProtocol;

}