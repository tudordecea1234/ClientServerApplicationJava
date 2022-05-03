package objectProtocol;

import model.CharityCase;

public class UpdateCasesResponse implements UpdateResponse{
    private Iterable<CharityCase> cases;
    public UpdateCasesResponse(Iterable<CharityCase> cases){
        this.cases=cases;
    }
    public Iterable<CharityCase> getCases(){
        return this.cases;
    }
}
