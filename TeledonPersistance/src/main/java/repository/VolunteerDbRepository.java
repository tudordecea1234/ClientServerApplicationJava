package repository;


import model.Volunteer;
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

public class VolunteerDbRepository implements VolunteerRepository {
    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public VolunteerDbRepository(Properties props) {
        logger.info("Initializing VolunteerDbRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(Volunteer elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("insert into Volunteers (firstName,lastName,email,password) values(?,?,?,?)")) {
            preStm.setString(1, elem.getFirstName());
            preStm.setString(2, elem.getLastName());
            preStm.setString(3, elem.getEmail());
            preStm.setString(4, elem.getPassword());
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
        try (PreparedStatement preStm = con.prepareStatement("delete from Volunteers where id=?")) {
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
    public void update(Volunteer elem, Long id) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("update Volunteers set firstName=?,lastName=?,email=?,password=? where id=?")) {
            preStm.setString(1, elem.getFirstName());
            preStm.setString(2, elem.getLastName());
            preStm.setString(3, elem.getEmail());
            preStm.setString(4,elem.getPassword());
            preStm.setLong(5, id);
            int result = preStm.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB: " + ex);
        }
    }

    @Override
    public Volunteer findById(Long id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Volunteer> volunteers = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement("select * from Volunteers where id=?")) {
            ps.setInt(1, Math.toIntExact(id));
            try (ResultSet r = ps.executeQuery()) {

                Volunteer volunteer = new Volunteer(r.getString("firstName"), r.getString("lastName"),
                        r.getString("email"), r.getString("password"));
                volunteer.setId((long) r.getInt("id"));
                volunteers.add(volunteer);

            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit(volunteers);
        return volunteers.get(0);
    }

    @Override
    public Iterable<Volunteer> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Volunteer> volunteers = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement("select * from Volunteers")) {
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    Volunteer car = new Volunteer(r.getString("firstName"), r.getString("lastName"),
                            r.getString("email"), r.getString("password"));
                    car.setId(r.getLong("id"));
                    volunteers.add(car);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit(volunteers);
        return volunteers;
    }

    @Override
    public Collection<Volunteer> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Volunteer> volunteers = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement("select * from Volunteers")) {
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    Volunteer vol = new Volunteer(r.getString("firstName"), r.getString("lastName"),
                            r.getString("email"), r.getString("password"));
                    vol.setId(r.getLong("id"));
                    volunteers.add(vol);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit(volunteers);
        return volunteers;
    }

    @Override
    public Volunteer findByEmail(String email) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Volunteer> volunteers = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement("select * from Volunteers where email=?")) {
            ps.setString(1, email);
            try (ResultSet r = ps.executeQuery()) {

                Volunteer volunteer = new Volunteer(r.getString("firstName"), r.getString("lastName"),
                        r.getString("email"), r.getString("password"));
                volunteer.setId((long) r.getInt("id"));
                volunteers.add(volunteer);

            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit(volunteers);
        return volunteers.get(0);
    }
}
