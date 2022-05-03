package server;

import model.CharityCase;
import model.Donation;
import model.Volunteer;
import repository.CharityCaseHibernateRepository;
import repository.CharityCaseRepository;
import repository.DonationRepository;
import repository.VolunteerRepository;
import teledon.services.ITeledonObserver;
import teledon.services.ITeledonServices;
import teledon.services.TeledonException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TeledonServicesImplementation implements ITeledonServices {
    private VolunteerRepository volunteerRepository;
    private DonationRepository donationRepository;
    private CharityCaseRepository charityCaseRepository;
    private ITeledonObserver client;
    private Map<Long, ITeledonObserver> loggedClients;
    private CharityCaseHibernateRepository hibernateCaseRepo;

    public TeledonServicesImplementation(VolunteerRepository vol,DonationRepository don,CharityCaseRepository cases,CharityCaseHibernateRepository caseHibernate){
        this.volunteerRepository=vol;
        this.donationRepository=don;
        this.charityCaseRepository=cases;
        this.hibernateCaseRepo=caseHibernate;
        loggedClients=new ConcurrentHashMap<>();

    }
    private final int defaultThreadsNo=5;
    public synchronized  void loginUser(String email, String password, ITeledonObserver client)throws TeledonException{
        Volunteer vol=volunteerRepository.findByEmail(email);
        if(vol==null || !Objects.equals(vol.getPassword(), password))
        {
            throw new TeledonException("Authentification failed!");
        }
        if(loggedClients.get(vol.getId())!=null)
            throw new TeledonException("User already logged in.");
        loggedClients.put(vol.getId(), client);
        this.client=client;
    }
    public synchronized void addDonation(Donation donation)throws TeledonException{
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);

        executor.execute(()->{
            donationRepository.add(donation);
            CharityCase case1=charityCaseRepository.findById(donation.getIdCase());
            case1.setTotalAmount(donation.getAmountDonated()+case1.getTotalAmount());
            charityCaseRepository.update(case1,case1.getId());
            Iterable<Donation> donations=donationRepository.findAll();
            for(var client1: loggedClients.entrySet()) {
                try {
                    client1.getValue().casesAmountUpdate(charityCaseRepository.findAll());
                } catch (TeledonException e) {
                    e.printStackTrace();
                }
                try {
                    client1.getValue().donationsUpdate(donations);
                } catch (TeledonException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public synchronized Collection<Donation> getAllDonations(String searchName){
        System.out.println("Getting donations for name: "+searchName);
        String name = searchName.trim().replaceAll("[ ]+", " ").toLowerCase();
        List<Donation> donation = StreamSupport.stream(donationRepository.findAll().spliterator(), false)
                .filter(user -> {
                    String lastNameFirstName = (user.getDonorLastName() + " " + user.getDonorFirstName()).toLowerCase();
                    String firstNameLastName = (user.getDonorFirstName() + " " + user.getDonorLastName()).toLowerCase();
                    return (lastNameFirstName.startsWith(name)
                            || firstNameLastName.startsWith(name));
                }).collect(Collectors.toList());
        return donation;

    }
    public synchronized Collection<CharityCase>getAllCases(){
        hibernateCaseRepo.initialize();
        Collection<CharityCase> cases= hibernateCaseRepo.getAll();
        hibernateCaseRepo.close();
        return cases;
    }

    @Override
    public synchronized void logout() throws TeledonException {
        /*ITeledonObserver localClient=loggedClients.remove(vol.getId());
        if (localClient==null)
            throw new TeledonException("User "+vol.getId()+" is not logged in.");*/
    }


}

