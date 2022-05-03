package objectProtocol;

import model.Donation;

public class AddDonationRequest implements Request{
    private Donation don;
    public AddDonationRequest(Donation don)
    {
        this.don=don;
    }
    public Donation getDonation(){return this.don;}
}
