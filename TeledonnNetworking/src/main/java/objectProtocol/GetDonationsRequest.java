package objectProtocol;

public class GetDonationsRequest implements Request{
    private String name;
    public GetDonationsRequest(String name){this.name=name;}
    public String getName(){return name;}
}
