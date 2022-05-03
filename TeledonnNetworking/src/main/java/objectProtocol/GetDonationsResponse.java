package objectProtocol;

import model.Donation;

public class GetDonationsResponse implements  Response{
    private Iterable<Donation> donations;
    public GetDonationsResponse(Iterable<Donation> donations){
        this.donations=donations;
    }
    public Iterable<Donation> getDonations(){return donations;}
}
