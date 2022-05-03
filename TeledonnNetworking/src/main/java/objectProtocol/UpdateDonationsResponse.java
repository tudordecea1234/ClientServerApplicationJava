package objectProtocol;

import model.Donation;

public class UpdateDonationsResponse implements UpdateResponse {private Iterable<Donation> donations;
    public UpdateDonationsResponse(Iterable<Donation> donations){
        this.donations=donations;
    }
    public Iterable<Donation> getDonations(){return donations;}
}
