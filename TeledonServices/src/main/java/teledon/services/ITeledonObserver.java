package teledon.services;

import model.CharityCase;
import model.Donation;

public interface ITeledonObserver {
    void donationReceived(Donation donation)throws TeledonException;
    void casesAmountUpdate(Iterable<CharityCase> case1)throws TeledonException;
    void donationsUpdate(Iterable<Donation> donations)throws TeledonException;

}
