package objectProtocol;

import model.CharityCase;
import model.Donation;
import teledon.services.ITeledonObserver;
import teledon.services.ITeledonServices;
import teledon.services.TeledonException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class TeledonClientObjectWorker implements Runnable, ITeledonObserver {
    private ITeledonServices server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public TeledonClientObjectWorker(ITeledonServices server,Socket connection){
        this.server=server;
        this.connection=connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run(){
        while(connected){
            try {
                Object request=input.readObject();
                Object response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse((Response) response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }
    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    @Override
    public void donationReceived(Donation donation) throws TeledonException {
        System.out.println("Donation added "+donation);
        try{
            sendResponse(new NewDonationResponse(donation));
        }catch(IOException e){
            throw new TeledonException("Sending error: "+e);
        }
    }

    @Override
    public void casesAmountUpdate(Iterable<CharityCase> case1) throws TeledonException {
        System.out.println("Getting cases...");
        try{
            sendResponse(new UpdateCasesResponse(case1));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void donationsUpdate(Iterable<Donation> donations) throws TeledonException {
        System.out.println("Getting donations...");
        try{
            sendResponse(new UpdateDonationsResponse(donations));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private Response handleRequest(Request request){
        Response response=null;
        if (request instanceof LoginRequest){
            System.out.println("Login request ...");
            LoginRequest logReq=(LoginRequest)request;
            String email=logReq.getEmail();
            String password=logReq.getPassword();

            try {
                server.loginUser(email,password, this);
                return new OkResponse();
            } catch (TeledonException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof LogoutRequest){
            System.out.println("Logout request...");
            LogoutRequest logReq=(LogoutRequest)request;

            try {
                server.logout();
                connected=false;
                return new OkResponse();

            } catch (TeledonException e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        if(request instanceof NewDonationRequest){
            System.out.println("NewDonationRequest...");
            NewDonationRequest req=(NewDonationRequest) request;
            Donation donation=req.getDonation();
            try{
                server.addDonation(donation);
                return new OkResponse();
            }catch(TeledonException e){
                return new ErrorResponse(e.getMessage());
            }
        }
        if(request instanceof GetDonationsRequest){
            System.out.println("GetDonationsRequest...");
            GetDonationsRequest req=(GetDonationsRequest) request;
            String name=req.getName();
            try{
                Donation[] donations= server.getAllDonations(name).toArray(new Donation[0]);
                return new GetDonationsResponse(List.of(donations));
            }catch(TeledonException e){
                return new ErrorResponse(e.getMessage());
            }
        }
        if(request instanceof  GetCasesRequest){
            System.out.println("GetCharityCasesRequest...");
            GetCasesRequest req=(GetCasesRequest) request;
            try{
                CharityCase[] cases= server.getAllCases().toArray(new CharityCase[0]);
                return new GetCharityCasesResponse(List.of(cases));
            }catch(TeledonException e){
                return new ErrorResponse(e.getMessage());
            }
        }
        return response;
    }
}
