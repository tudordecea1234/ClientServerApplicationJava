package model.validators;

import model.CharityCase;

public class CharityCaseValidator implements Validator<CharityCase>{
    @Override
    public void validate(CharityCase entity) throws ValidationException {
        String errors = "";
        String firstName = entity.getCaseName();
        if (firstName == null || firstName.isEmpty() || firstName.charAt(0) < 'A' || firstName.charAt(0) > 'Z')
            errors += "invalid case name!\n";
        float amount=entity.getTotalAmount();
        if(amount<0)
            errors+="invalid amount\n";
        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
