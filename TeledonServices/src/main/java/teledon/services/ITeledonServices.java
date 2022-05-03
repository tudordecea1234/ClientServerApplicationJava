package teledon.services;

import model.CharityCase;
import model.Donation;
import model.Volunteer;

import java.util.Collection;

public interface ITeledonServices {
    void loginUser(String email,String password,ITeledonObserver client)throws TeledonException;
    Collection<Donation> getAllDonations(String searchName)throws TeledonException;
    void addDonation(Donation donation)throws TeledonException;

    Collection<CharityCase> getAllCases()throws TeledonException;
    void logout() throws TeledonException;


}
