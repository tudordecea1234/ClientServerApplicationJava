package protoBuffProtocol;

import model.CharityCase;
import model.Donation;
import model.Volunteer;

public class ProtoUtils {
    public static TeledonProtoBuffs.TeledonRequest createLoginRequest(String email,String pass){
        TeledonProtoBuffs.Volunteer userDTO=TeledonProtoBuffs.Volunteer.newBuilder().setEmail(email).setPassword(pass).build();
        TeledonProtoBuffs.TeledonRequest request= TeledonProtoBuffs.TeledonRequest.newBuilder().setType(TeledonProtoBuffs.TeledonRequest.Type.Login)
                .setUser(userDTO).build();
        return request;
    }
    public static TeledonProtoBuffs.TeledonRequest createLogoutRequest(){
        //TeledonProtoBuffs.Volunteer userDTO=TeledonProtoBuffs.Volunteer.newBuilder().setEmail(user.getEmail()).build();
        TeledonProtoBuffs.TeledonRequest request= TeledonProtoBuffs.TeledonRequest.newBuilder().setType(TeledonProtoBuffs.TeledonRequest.Type.Logout)
                .build();
        return request;
    }
    public static TeledonProtoBuffs.TeledonRequest addDonationRequest(Donation donation){
        TeledonProtoBuffs.Donation don=TeledonProtoBuffs.Donation.newBuilder().setAmountDonated(String.valueOf(donation.getAmountDonated())).
                setAddress(donation.getAddress()).setDonorFirstName(donation.getDonorFirstName()).setDonorLastName(donation.getDonorLastName()).
                setPhone(donation.getPhoneNumber()).setIdCase(String.valueOf(donation.getIdCase())).build();
        TeledonProtoBuffs.TeledonRequest request=TeledonProtoBuffs.TeledonRequest.newBuilder().setType(TeledonProtoBuffs.TeledonRequest.Type.AddDonation).setDonation(don).build();
        return request;
    }
    public static TeledonProtoBuffs.TeledonRequest getCharityCasesRequest(){
        TeledonProtoBuffs.TeledonRequest request=TeledonProtoBuffs.TeledonRequest.newBuilder().setType(TeledonProtoBuffs.TeledonRequest.Type.GetCharityCases).build();
        return request;
    }
    public static TeledonProtoBuffs.TeledonRequest getDonationsRequest(String name){
        TeledonProtoBuffs.TeledonRequest request= TeledonProtoBuffs.TeledonRequest.newBuilder()
                .setType(TeledonProtoBuffs.TeledonRequest.Type.GetDonations).setSearchName(name).build();
        return request;

    }
    public static TeledonProtoBuffs.TeledonResponse createOkResponse(){
        TeledonProtoBuffs.TeledonResponse response=TeledonProtoBuffs.TeledonResponse.newBuilder()
                .setType(TeledonProtoBuffs.TeledonResponse.Type.Ok).build();
        return response;
    }

    public static TeledonProtoBuffs.TeledonResponse createErrorResponse(String text){
        TeledonProtoBuffs.TeledonResponse response=TeledonProtoBuffs.TeledonResponse.newBuilder()
                .setType(TeledonProtoBuffs.TeledonResponse.Type.Error)
                .setError(text).build();
        return response;
    }
    public static TeledonProtoBuffs.TeledonResponse addDonationResponse(Donation donation){
        TeledonProtoBuffs.Donation don=TeledonProtoBuffs.Donation.newBuilder().setAmountDonated(String.valueOf(donation.getAmountDonated())).
                setAddress(donation.getAddress()).setDonorFirstName(donation.getDonorFirstName()).setDonorLastName(donation.getDonorLastName()).
                setPhone(donation.getPhoneNumber()).build();
        TeledonProtoBuffs.TeledonResponse response= TeledonProtoBuffs.TeledonResponse.newBuilder()
                .setType(TeledonProtoBuffs.TeledonResponse.Type.NewDonation).setDonation(don).build();
        return response;
    }
    public static TeledonProtoBuffs.TeledonResponse createGetDonationsResponse(Donation[] donations){
        TeledonProtoBuffs.TeledonResponse.Builder response=TeledonProtoBuffs.TeledonResponse.newBuilder()
                .setType(TeledonProtoBuffs.TeledonResponse.Type.GetDonations);
        for (Donation don: donations){
            TeledonProtoBuffs.Donation don2=TeledonProtoBuffs.Donation.newBuilder().setAmountDonated(String.valueOf(don.getAmountDonated()))
                    .setAddress(don.getAddress()).setDonorFirstName(don.getDonorFirstName()).setDonorLastName(don.getDonorLastName()).
                    setIdCase(String.valueOf(don.getIdCase())).
                    setPhone(don.getPhoneNumber()).build();
            response.addDonations(don2);
        }
        return response.build();
    }
    public static TeledonProtoBuffs.TeledonResponse createGetCasesResponse(CharityCase[] cases){
        TeledonProtoBuffs.TeledonResponse.Builder response=TeledonProtoBuffs.TeledonResponse.newBuilder()
                .setType(TeledonProtoBuffs.TeledonResponse.Type.GetCharityCases);
        for( CharityCase case1:cases){
            TeledonProtoBuffs.CharityCase case2=TeledonProtoBuffs.CharityCase.newBuilder().setCaseName(case1.getCaseName())
                    .setTotalAmount(case1.getTotalAmount()).build();
            response.addCases(case2);
        }
        return response.build();
    }
    public static TeledonProtoBuffs.TeledonResponse createUpdateCasesResponse(CharityCase[] cases){
        TeledonProtoBuffs.TeledonResponse.Builder response=TeledonProtoBuffs.TeledonResponse.newBuilder()
                .setType(TeledonProtoBuffs.TeledonResponse.Type.UpdateCases);
        for( CharityCase case1:cases){
            TeledonProtoBuffs.CharityCase case2=TeledonProtoBuffs.CharityCase.newBuilder().setCaseName(case1.getCaseName())
                    .setTotalAmount(case1.getTotalAmount()).build();
            response.addCases(case2);
        }
        return response.build();
    }
    public static String getError(TeledonProtoBuffs.TeledonResponse response){
        String errorMessage=response.getError();
        return errorMessage;
    }
    public static Donation getDonation1(TeledonProtoBuffs.TeledonResponse response){

        Donation don=new Donation(Long.parseLong(response.getDonation().getIdCase()),response.getDonation().getDonorFirstName(),
                response.getDonation().getDonorLastName(),response.getDonation().getAddress(),
                response.getDonation().getPhone(),Float.parseFloat(response.getDonation().getAmountDonated()));
        return don;
    }
    public static Donation[] getDonations(TeledonProtoBuffs.TeledonResponse response){
        Donation[] donations=new Donation[response.getDonationsCount()];
        for(int i=0;i<response.getDonationsCount();i++){
            TeledonProtoBuffs.Donation donDTO=response.getDonations(i);
            Donation don=new Donation(Long.parseLong(donDTO.getIdCase()),donDTO.getDonorFirstName(),donDTO.getDonorLastName(),donDTO.getAddress(),donDTO.getPhone(),Float.parseFloat(donDTO.getAmountDonated()));
            donations[i]=don;
        }
        return donations;
    }
    public static CharityCase[] getCases(TeledonProtoBuffs.TeledonResponse response){
        CharityCase[] cases=new CharityCase[response.getCasesCount()];
        for(int i=0;i<response.getCasesCount();i++){
            TeledonProtoBuffs.CharityCase caseDTO=response.getCases(i);
            CharityCase case1=new CharityCase(caseDTO.getCaseName(),caseDTO.getTotalAmount());
            case1.setId(caseDTO.getId());
            cases[i]=case1;
        }
        return cases;
    }
    /*public static Volunteer getVolunteer(TeledonProtoBuffs.TeledonResponse response){
        Volunteer user=new Volunteer();
        user.setId(response.get());
        return user;
    }*/
}
