package dasturlash.uz;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        firstLevelCacheExample();

    }


    public static void firstLevelCacheExample() throws InterruptedException {
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();

        SessionFactory factory = meta.getSessionFactoryBuilder().build();
        Session session = factory.openSession();

        StudentEntity student = new StudentEntity();
        student.setName("Eshmat");
        student.setSurname("Eshmatov");
        student.setCreatedDate(LocalDateTime.now());

        session.save(student); // save student. object in session cache.

        //waiting 3 to change
        Thread.sleep(3000);

        //get same student again, no query in logs.
        //Student will be taken from session cache
        StudentEntity student2 = session.get(StudentEntity.class, student.getId());

        //create new session
        Session newSession = factory.openSession();
        //get same student. Hibernate will get it from database
        StudentEntity student3 = newSession.get(StudentEntity.class, student.getId());

        session.close();
        factory.close();
    }
}