package objectProtocol;

import model.CharityCase;

public class GetCharityCasesResponse implements Response{
    private Iterable<CharityCase> cases;
    public GetCharityCasesResponse(Iterable<CharityCase> cases){
        this.cases=cases;
    }
    public Iterable<CharityCase> getCases(){
        return this.cases;
    }
}
