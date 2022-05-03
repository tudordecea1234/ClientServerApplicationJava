/*
package com.example.lab4_mpp.service;
import com.example.lab4_mpp.model.CharityCase;
import com.example.lab4_mpp.model.Donation;
import com.example.lab4_mpp.model.Volunteer;
import com.example.lab4_mpp.model.validators.CharityCaseValidator;
import com.example.lab4_mpp.model.validators.DonationValidator;
import com.example.lab4_mpp.model.validators.VolunteerValidator;
import com.example.lab4_mpp.repository.CharityCaseDbRepository;
import com.example.lab4_mpp.repository.DonationDbRepository;
import com.example.lab4_mpp.repository.VolunteerRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServiceTeledon {
    private final VolunteerRepository volunteerRepository;
    private final DonationDbRepository donationDbRepository;
    private final CharityCaseDbRepository caseDbRepository;
    private final VolunteerValidator volunteerValidator;
    private final DonationValidator donationValidator;
    private final CharityCaseValidator charityCaseValidator;


    public ServiceTeledon(VolunteerRepository volRepo, DonationDbRepository donRepo,
                          CharityCaseDbRepository caseRepo, VolunteerValidator val, DonationValidator don,
                          CharityCaseValidator caseValidator) {
        this.volunteerValidator = val;
        this.volunteerRepository = volRepo;
        this.donationDbRepository = donRepo;
        this.caseDbRepository = caseRepo;
        this.donationValidator = don;
        this.charityCaseValidator=caseValidator;


    }

    public Volunteer loginUser(String email, String password) {
        Volunteer user = volunteerRepository.findByEmail(email);
        if (user == null)
            return null;
        if (password.equals(user.getPassword()))
            return user;
        return null;
    }

    public Collection<CharityCase> getAllCases() {
        return caseDbRepository.getAll();
    }

    public Collection<Donation> getAllDonations() {
        return donationDbRepository.getAll();
    }

    public Collection<Donation> getAllDonations(String searchName) {
        String name = searchName.trim().replaceAll("[ ]+", " ").toLowerCase();
        List<Donation> donation = StreamSupport.stream(donationDbRepository.findAll().spliterator(), false)
                .filter(user -> {
                    String lastNameFirstName = (user.getDonorLastName() + " " + user.getDonorFirstName()).toLowerCase();
                    String firstNameLastName = (user.getDonorFirstName() + " " + user.getDonorLastName()).toLowerCase();
                    return (lastNameFirstName.startsWith(name)
                            || firstNameLastName.startsWith(name));
                }).collect(Collectors.toList());
        return donation;
    }

    public boolean addVolunteer(String email, String firstName, String lastName, String password) {
        Volunteer volunteer = new Volunteer(firstName, lastName, email, password);
        volunteerValidator.validate(volunteer);
        volunteerRepository.add(volunteer);
        return true;4
    }

    public void addDonation(Long idCase, String firstName, String lastName, String address, String phone, float amount) {
        Donation don = new Donation(idCase, firstName, lastName, address, phone, amount);
        donationValidator.validate(don);
        CharityCase case1 = caseDbRepository.findById(idCase);
        float totalAmount = case1.getTotalAmount();
        case1.setTotalAmount(totalAmount + amount);
        donationDbRepository.add(don);
        caseDbRepository.update(case1, case1.getId());
    }

    public void addCharityCase(String name,float totalAmount){
        CharityCase case1=new CharityCase(name,totalAmount);
        charityCaseValidator.validate(case1);
        caseDbRepository.add(case1);
    }
}
*/
