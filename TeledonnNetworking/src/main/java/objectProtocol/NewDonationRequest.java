package objectProtocol;

import model.Donation;

public class NewDonationRequest implements Request{
    private Donation don;
    public NewDonationRequest(Donation don){
        this.don=don;
    }
    public Donation getDonation(){
        return this.don;
    }
}
