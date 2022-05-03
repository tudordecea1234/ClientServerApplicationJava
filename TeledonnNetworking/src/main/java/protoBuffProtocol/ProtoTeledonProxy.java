package protoBuffProtocol;

import model.CharityCase;
import model.Donation;
import teledon.services.ITeledonObserver;
import teledon.services.ITeledonServices;
import teledon.services.TeledonException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoTeledonProxy implements ITeledonServices {
    private String host;
    private int port;

    private ITeledonObserver client;
    private volatile boolean finished;
    private InputStream input;
    private OutputStream output;
    private BlockingQueue<TeledonProtoBuffs.TeledonResponse> qresponses;
    private Socket connection;
    public ProtoTeledonProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<TeledonProtoBuffs.TeledonResponse>();
    }
    @Override
    public void loginUser(String email, String password, ITeledonObserver client) throws TeledonException {
        initializeConnection();
        System.out.println("Login request ...");
        sendRequest(ProtoUtils.createLoginRequest(email,password));
        TeledonProtoBuffs.TeledonResponse response=readResponse();
        if (response.getType()== TeledonProtoBuffs.TeledonResponse.Type.Ok){
            this.client=client;
            return;
        }
        if (response.getType()== TeledonProtoBuffs.TeledonResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            closeConnection();
            throw new TeledonException(errorText);
        }
    }

    @Override
    public Collection<Donation> getAllDonations(String searchName) throws TeledonException {
        sendRequest(ProtoUtils.getDonationsRequest(searchName));
        TeledonProtoBuffs.TeledonResponse response=readResponse();
        if (response.getType()== TeledonProtoBuffs.TeledonResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            throw new TeledonException(errorText);
        }
        Donation[] friends=ProtoUtils.getDonations(response);
        return List.of(friends);
    }

    @Override
    public void addDonation(Donation donation) throws TeledonException {
        sendRequest(ProtoUtils.addDonationRequest(donation));
        TeledonProtoBuffs.TeledonResponse response=readResponse();
        if (response.getType()== TeledonProtoBuffs.TeledonResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            throw new TeledonException(errorText);
        }
    }

    @Override
    public Collection<CharityCase> getAllCases() throws TeledonException {
        sendRequest(ProtoUtils.getCharityCasesRequest());
        TeledonProtoBuffs.TeledonResponse response=readResponse();
        if (response.getType()== TeledonProtoBuffs.TeledonResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            throw new TeledonException(errorText);
        }
        CharityCase[] friends=ProtoUtils.getCases(response);
        return List.of(friends);
    }

    @Override
    public void logout() throws TeledonException {
        sendRequest(ProtoUtils.createLogoutRequest());
        TeledonProtoBuffs.TeledonResponse response=readResponse();
        closeConnection();
        if (response.getType()== TeledonProtoBuffs.TeledonResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            throw new TeledonException(errorText);
        }
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(TeledonProtoBuffs.TeledonRequest request)throws TeledonException{
        try {
            System.out.println("Sending request ..."+request);
            //request.writeTo(output);
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Request sent.");
        } catch (IOException e) {
            throw new TeledonException("Error sending object "+e);
        }

    }

    private TeledonProtoBuffs.TeledonResponse readResponse() throws TeledonException{
        TeledonProtoBuffs.TeledonResponse response=null;
        try{
            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() throws TeledonException{
        try {
            connection=new Socket(host,port);
            output=connection.getOutputStream();
            //output.flush();
            input=connection.getInputStream();     //new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }
    private void handleUpdate(TeledonProtoBuffs.TeledonResponse updateResponse){
        switch (updateResponse.getType()){
            case UpdateCases :{
                CharityCase[] cases=ProtoUtils.getCases(updateResponse);
                System.out.println("UPDATING CASES... ");
                try {
                    client.casesAmountUpdate(List.of(cases));
                } catch (TeledonException e) {
                    e.printStackTrace();
                }
                break;
            }
            }

        }
    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    TeledonProtoBuffs.TeledonResponse response= TeledonProtoBuffs.TeledonResponse.parseDelimitedFrom(input);
                    System.out.println("response received "+response);

                    if (isUpdateResponse(response.getType())){
                        handleUpdate(response);
                    }else{
                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }

    private boolean isUpdateResponse(TeledonProtoBuffs.TeledonResponse.Type type){
        switch (type){
            case UpdateCases:  return true;
            //case NewDonation: return true;
        }
        return false;
    }

}
