package repository;


import model.Donation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class DonationDbRepository implements DonationRepository{
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public DonationDbRepository(Properties props) {
        logger.info("Initializing DonationDbRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }
    @Override
    public void add(Donation elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("insert into Donation (idCase,donorFirstName,donorLastName,address,phoneNumber,amountDonated) values(?,?,?,?,?,?)")) {
            preStm.setLong(1, elem.getIdCase());
            preStm.setString(2, elem.getDonorFirstName());
            preStm.setString(3, elem.getDonorLastName());
            preStm.setString(4, elem.getAddress());
            preStm.setString(5, elem.getPhoneNumber());
            preStm.setFloat(6, elem.getAmountDonated());
            int result = preStm.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Long elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("delete from Donation where id=?")) {
            preStm.setLong(1,elem);
            int result = preStm.executeUpdate();
            logger.trace("Deleted {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Donation elem, Long id) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("update Donation set idCase=?,donorFirstName=?,donorLastName=?,address=?,phoneNumber=?,amountDonated=? where id=?")) {
            preStm.setLong(1, elem.getIdCase());
            preStm.setString(2, elem.getDonorFirstName());
            preStm.setString(3, elem.getDonorLastName());
            preStm.setString(4,elem.getAddress());
            preStm.setString(5,elem.getPhoneNumber());
            preStm.setFloat(6, elem.getAmountDonated());
            preStm.setLong(7, id);
            int result = preStm.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB: " + ex);
        }
    }

    @Override
    public Donation findById(Long id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Donation> donations = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement("select * from Donation where id=?")) {
            ps.setLong(1, id);
            try (ResultSet r = ps.executeQuery()) {

                Donation donation = new Donation(r.getLong("idCase"),r.getString("donorFirstName"), r.getString("donorLastName"),
                        r.getString("address"),r.getString("phoneNumber"), r.getFloat("amountDonated"));
                donation.setId(r.getLong("id"));
                donations.add(donation);

            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit(donations);
        return donations.get(0);
    }

    @Override
    public Iterable<Donation> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Donation> donations = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement("select * from Donation")) {
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    Donation donation = new Donation(r.getLong("idCase"),r.getString("donorFirstName"), r.getString("donorLastName"),
                            r.getString("address"), r.getString("phoneNumber"),r.getFloat("amountDonated"));
                    donation.setId(r.getLong("id"));
                    donations.add(donation);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit(donations);
        return donations;
    }

    @Override
    public Collection<Donation> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Donation> donations = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement("select * from Donation")) {
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    Donation donation = new Donation(r.getLong("idCase"),r.getString("donorFirstName"), r.getString("donorLastName"),
                            r.getString("address"), r.getString("phoneNumber"),r.getFloat("amountDonated"));
                    donation.setId(r.getLong("id"));
                    donations.add(donation);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit(donations);
        return donations;
    }
}
