package objectProtocol;

import model.Donation;

public class NewDonationResponse implements UpdateResponse{
    private Donation don;
    public NewDonationResponse(Donation don){
        this.don=don;
    }
    public Donation getDonation(){
        return this.don;
    }
}
