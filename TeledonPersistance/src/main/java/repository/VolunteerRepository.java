package repository;


import model.Volunteer;

public interface VolunteerRepository extends Repository<Volunteer,Long> {
    public Volunteer findByEmail(String email);
}
