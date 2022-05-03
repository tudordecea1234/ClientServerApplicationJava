package objectProtocol;
import model.CharityCase;
import model.Donation;
import model.Volunteer;
import teledon.services.ITeledonObserver;
import teledon.services.ITeledonServices;
import teledon.services.TeledonException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class TeledonServicesObjectProxy implements ITeledonServices {
    private String host;
    private int port;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private ITeledonObserver client;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    private List<Response> responses;

    public TeledonServicesObjectProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingDeque<Response>();
        responses=new ArrayList<Response>();
    }

    @Override
    public void loginUser(String email,String password, ITeledonObserver client) throws TeledonException {
        initializeConnection();
        sendRequest(new LoginRequest(email,password));
        Response response = readResponse();
        if (response instanceof OkResponse) {
            this.client = client;
            return;
        }
        if (response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) response;
            closeConnection();
            throw new TeledonException(err.getMessage());
        }
    }

    @Override
    public Collection<Donation> getAllDonations(String searchName) throws TeledonException {
        sendRequest(new GetDonationsRequest(searchName));
        Response response=readResponse();
        if(response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new TeledonException(err.getMessage());
        }
        GetDonationsResponse resp=(GetDonationsResponse) response;
        Iterable<Donation> donations=resp.getDonations();
        return (Collection<Donation>) donations;
    }

    @Override
    public void addDonation(Donation don) throws TeledonException {
    //Donation don=new Donation(idCase,firstName,lastName,address,phone,amount);
    sendRequest(new NewDonationRequest(don));
    Response response=readResponse();
        if (response instanceof ErrorResponse err){
            throw new TeledonException(err.getMessage());
        };

    }

    @Override
    public Collection<CharityCase> getAllCases() throws TeledonException {
        sendRequest(new GetCasesRequest());
        Response response=readResponse();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new TeledonException(err.getMessage());
        }
        GetCharityCasesResponse resp=(GetCharityCasesResponse) response;
        Iterable<CharityCase> cases=resp.getCases();
        return (Collection<CharityCase>) cases;

    }

    @Override
    public void logout() throws TeledonException {
        sendRequest(new LogoutRequest());
        Response response=readResponse();
        closeConnection();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new TeledonException(err.getMessage());
        }
    }

    private void sendRequest(Request request)throws TeledonException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new TeledonException("Error sending object "+e);
        }

    }
    private Response readResponse() throws TeledonException {
        Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
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
    private void initializeConnection() throws TeledonException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }
    private void handleUpdate(UpdateResponse update){
        if(update instanceof UpdateCasesResponse){
            UpdateCasesResponse charityUpd=(UpdateCasesResponse) update;
            Iterable<CharityCase> cases=charityUpd.getCases();
            try {
                client.casesAmountUpdate((cases));
            } catch (TeledonException e) {
                e.printStackTrace();
            }
        }
        if(update instanceof NewDonationResponse){
            GetDonationsResponse donationResponse=(GetDonationsResponse) update;
            Iterable<Donation> don=donationResponse.getDonations();
            System.out.println("New donation "+ Arrays.toString(new Iterable[]{don}));
            try {
                client.donationsUpdate((don));
            } catch (TeledonException e) {
                e.printStackTrace();
            }
        }
        if(update instanceof  UpdateDonationsResponse){
            UpdateDonationsResponse donationsResponse=(UpdateDonationsResponse) update;
            Iterable<Donation> don=donationsResponse.getDonations();
            System.out.println("donation "+ Arrays.toString(new Iterable[]{don}));
            try {
                client.donationsUpdate((don));
            } catch (TeledonException e) {
                e.printStackTrace();
            }
        }
    }
    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (response instanceof UpdateResponse) {
                        handleUpdate((UpdateResponse) response);
                    } else {
                        responses.add((Response)response);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        synchronized (responses){
                            responses.notify();
                        }
                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}

