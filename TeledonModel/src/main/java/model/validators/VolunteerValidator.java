package model.validators;

import model.Volunteer;

public class VolunteerValidator implements Validator<Volunteer> {
    @Override
    public void validate(Volunteer entity) throws ValidationException {
        String errors = "";
        String firstName = entity.getFirstName();
        if (firstName == null || firstName.isEmpty() || firstName.charAt(0) < 'A' || firstName.charAt(0) > 'Z')
            errors += "invalid firstName!\n";
        String lastName = entity.getLastName();
        if (lastName == null || lastName.isEmpty() || lastName.charAt(0) < 'A' || lastName.charAt(0) > 'Z')
            errors += "invalid lastName!\n";
        String email = entity.getEmail().trim();
        String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        boolean b = email.matches(EMAIL_REGEX);
        if (!b)
            errors += "invalid email!\n";
        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
