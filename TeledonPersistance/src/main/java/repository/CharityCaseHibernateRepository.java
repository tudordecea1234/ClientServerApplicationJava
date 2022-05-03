package repository;

import model.CharityCase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class CharityCaseHibernateRepository {

    private static SessionFactory sessionFactory;

    public static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Exception " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public void add(CharityCase elem) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(elem);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la inserare "+ex);
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    public void delete(Long elem) {
       try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                CharityCase crit=session.load(CharityCase.class,elem);
                System.err.println("Stergem mesajul " + crit.getId());
                session.delete(crit);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la stergere "+ex);
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    public void update(CharityCase elem, Long id) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                CharityCase message = session.load( CharityCase.class, id );
                CharityCase nextMessage = new CharityCase(elem.getCaseName(),elem.getTotalAmount());
                nextMessage.setId(id);
                session.update(nextMessage);
                tx.commit();
            } catch(RuntimeException ex){
                System.err.println("Eroare la update "+ex);
                if (tx!=null)
                    tx.rollback();
            }
        }
    }

    public CharityCase findById(Long id) {
        CharityCase message=new CharityCase();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                message = session.load( CharityCase.class, id );
                System.out.println("Am gasit cazul: "+message);
                tx.commit();
            } catch(RuntimeException ex){
                System.err.println("Eroare la Find By Id "+ex);
                if (tx!=null)
                    tx.rollback();
            }
        }
        return message;
    }

    public Iterable<CharityCase> findAll() {
        List<CharityCase> cases = null;
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                 cases =
                        session.createQuery("from CharityCase ", CharityCase.class)
                // session.createQuery("select m from Message as m join fetch m.nextMessage order by m.text asc", Message.class). //initializarea obiectelor asociate
                                .list();
                System.out.println(cases.size() + " case(s) found:");
                for (CharityCase m : cases) {
                    System.out.println(m.getId()+' '+m.getCaseName() + ' ' + m.getTotalAmount());
                    //  System.out.println(m.getText() + ' ' + m.getId()+" textul urmatorului mesaj ["+m.getNextMessage().getText()+"]");
                }
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la select "+ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        return cases;
    }



    public Collection<CharityCase> getAll() {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<CharityCase> messages =
                        session.createQuery("from CharityCase", CharityCase.class).
                                //        session.createQuery("select m from Message as m join fetch m.nextMessage order by m.text asc", Message.class). //initializarea obiectelor asociate

                                        setFirstResult(1).setMaxResults(50).
                                list();
                System.out.println(messages.size() + " case(s) found:");
                for (CharityCase m : messages) {
                    System.out.println(m.getId()+' '+m.getCaseName() + ' ' + m.getTotalAmount());
                    //  System.out.println(m.getText() + ' ' + m.getId()+" textul urmatorului mesaj ["+m.getNextMessage().getText()+"]");
                }
                tx.commit();
                return messages;
            } catch (RuntimeException ex) {
                System.err.println("Eroare la select "+ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        return null;
    }
   /* public static void main(String[] args) {
        try {
            initialize();

            CharityCaseHibernateRepository test = new CharityCaseHibernateRepository();
            CharityCase case1=new CharityCase("aloalo",111);
            test.add(case1);
            test.getAll();
            test.delete(34L);
            test.findById(34L);
            test.findAll();
        }catch (Exception e){
            System.err.println("Exception "+e);
            e.printStackTrace();
        }finally {
            close();
        }
    }*/
}
