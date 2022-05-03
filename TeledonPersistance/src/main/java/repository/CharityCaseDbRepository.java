package repository;

import model.CharityCase;
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

public class CharityCaseDbRepository implements CharityCaseRepository{
    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public CharityCaseDbRepository(Properties props) {
        logger.info("Initializing CharityCaseDbRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }
    @Override
    public void add(CharityCase elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("insert into CharityCases (caseName,totalAmount) values(?,?)")) {
            preStm.setString(1, elem.getCaseName());
            preStm.setFloat(2, elem.getTotalAmount());
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
        try (PreparedStatement preStm = con.prepareStatement("delete from CharityCases where id=?")) {
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
    public void update(CharityCase elem, Long id) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("update CharityCases set caseName=?,totalAmount=? where id=?")) {
            preStm.setString(1, elem.getCaseName());
            preStm.setFloat(2, elem.getTotalAmount());
            preStm.setLong(3, id);
            int result = preStm.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB: " + ex);
        }
    }

    @Override
    public CharityCase findById(Long id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<CharityCase> cases = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement("select * from CharityCases where id=?")) {
            ps.setLong(1, id);
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    CharityCase case1 = new CharityCase(r.getString("caseName"),r.getFloat("totalAmount"));
                    case1.setId(r.getLong("id"));
                    cases.add(case1);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit(cases);
        return cases.get(0);
    }


    @Override
    public Iterable<CharityCase> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<CharityCase> cases = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement("select * from CharityCases")) {
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    CharityCase case1 = new CharityCase(r.getString("caseName"),r.getFloat("totalAmount"));
                    case1.setId(r.getLong("id"));
                    cases.add(case1);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit(cases);
        return cases;
    }


    @Override
    public Collection<CharityCase> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<CharityCase> cases = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement("select * from CharityCases")) {
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    CharityCase case1 = new CharityCase(r.getString("caseName"),r.getFloat("totalAmount"));
                    case1.setId(r.getLong("id"));
                    cases.add(case1);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit(cases);
        return cases;
    }
}
