package model.validators;

import model.Donation;


public class DonationValidator implements Validator<Donation>{
    @Override
    public void validate(Donation entity) throws ValidationException {
        String errors = "";
        String firstName = entity.getDonorFirstName();
        if (firstName == null || firstName.isEmpty() || firstName.charAt(0) < 'A' || firstName.charAt(0) > 'Z')
            errors += "invalid firstName!\n";
        String lastName = entity.getDonorLastName();
        if (lastName == null || lastName.isEmpty() || lastName.charAt(0) < 'A' || lastName.charAt(0) > 'Z')
            errors += "invalid lastName!\n";
        String address = entity.getAddress();
        if (address == null || address.isEmpty())
            errors += "invalid address!\n";
        float amount=entity.getAmountDonated();
        if(amount<0)
            errors+="invalid amount\n";
        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
