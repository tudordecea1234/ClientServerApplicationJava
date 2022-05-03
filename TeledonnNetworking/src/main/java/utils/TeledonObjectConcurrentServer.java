package utils;

import objectProtocol.TeledonClientObjectWorker;
import teledon.services.ITeledonServices;

import java.net.Socket;

public class TeledonObjectConcurrentServer extends AbsConcurrentServer {
    private ITeledonServices teledonServer;
    public TeledonObjectConcurrentServer(int port,ITeledonServices serv){
        super(port);
        this.teledonServer=serv;
        System.out.println("Teledon- TeledonObjectConcurrentServer");
    }
    @Override
    protected Thread createWorker(Socket client) {
        TeledonClientObjectWorker worker=new TeledonClientObjectWorker(teledonServer, client);
        Thread tw=new Thread(worker);
        return tw;
    }
}
