package org.training.hibtest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.Session;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try{
        	Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        	Connection con = null;
        	try{
        		con = DriverManager.getConnection("jdbc:derby:projectdb");
        	}catch (SQLException e) {
        		con = DriverManager.getConnection("jdbc:derby:projectdb;create=true");
        		Statement st = con.createStatement();
        		
        		st.executeUpdate("CREATE TABLE users "
        				+ "(ID INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
        				+ "FIRST_NAME VARCHAR(150) NOT NULL, "
        				+ "LAST_NAME VARCHAR(150) NOT NULL, "
        				+ "EMAIL VARCHAR(320) UNIQUE NOT NULL, "
        				+ "ROLE VARCHAR(50) NOT NULL, "
        				+ "PASSWORD VARCHAR(100) NOT NULL, "
        				+ "PRIMARY KEY (ID))");
        		
        		st.executeUpdate("CREATE TABLE status "
        				+ "(ID INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
        				+ "NAME VARCHAR(50) NOT NULL, "
        				+ "PRIMARY KEY (ID))");
        		
        		st.executeUpdate("CREATE TABLE resolution "
        				+ "(ID INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
        				+ "NAME VARCHAR(50) NOT NULL, "
        				+ "PRIMARY KEY (ID))");
        		
        		st.executeUpdate("CREATE TABLE priority "
        				+ "(ID INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
        				+ "NAME VARCHAR(50) NOT NULL, "
        				+ "PRIMARY KEY (ID))");
        		
        		st.executeUpdate("CREATE TABLE type "
        				+ "(ID INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
        				+ "NAME VARCHAR(50) NOT NULL, "
        				+ "PRIMARY KEY (ID))");
        		
        		st.executeUpdate("CREATE TABLE project "
        				+ "(ID INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
        				+ "NAME VARCHAR(50) NOT NULL, "
        				+ "DESCRIPTION VARCHAR(2000) NOT NULL, "
        				+ "MANAGER_ID INT NOT NULL, "
        				+ "PRIMARY KEY (ID))");
        		
        		st.executeUpdate("ALTER TABLE project "
        				+ "ADD FOREIGN KEY (MANAGER_ID)"
        				+ "REFERENCES users (ID)");
        		
        		st.executeUpdate("CREATE TABLE build "
        				+ "(ID INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
        				+ "NAME VARCHAR(50) NOT NULL, "
        				+ "PROJECT_ID INT NOT NULL, "
        				+ "PRIMARY KEY (ID))");
        		
        		st.executeUpdate("ALTER TABLE build "
        				+ "ADD FOREIGN KEY (PROJECT_ID)"
        				+ "REFERENCES project (ID)");
        		
        		st.close();
        		
        		Session session = HibernateUtil.getSessionFactory().openSession();
        		session.beginTransaction();
        		session.save(new User("admin", "admin", "admin", Role.ADMINISTRATOR, "admin"));
        		session.save(new User("user", "user", "user", Role.USER, "user"));
        		session.save(new Status("New"));
        		session.save(new Status("Assigned"));
        		session.save(new Status("In Progress"));
        		session.save(new Status("Resolved"));
        		session.save(new Status("Closed"));
        		session.save(new Status("Reopened"));
        		session.save(new Resolution("Fixed"));
        		session.save(new Resolution("Invalid"));
        		session.save(new Resolution("Wontfix"));
        		session.save(new Resolution("Worksforme"));
        		session.save(new Priority("Critical"));
        		session.save(new Priority("Major"));
        		session.save(new Priority("Important"));
        		session.save(new Priority("Minor"));
        		session.save(new Type("Cosmetic"));
        		session.save(new Type("Bug"));
        		session.save(new Type("Feature"));
        		session.save(new Type("Perfomance"));
        		
        		session.getTransaction().commit();
        		session.beginTransaction();
        		
        		User manager = new User("admin", "admin", "admin", Role.ADMINISTRATOR, "admin");
        		manager.setId(1);
        		
        		Project project = new Project();
        		project.setName("beta1");
        		project.setDescription("about beta1");
        		project.setManager(manager);
        		session.save(project);
        		
        		Build build = new Build();
        		build.setName("1.0");
        		
        		build.setProject(project);
        		project.getBuilds().add(build);
        		
        		session.save(build);
        		
        		session.getTransaction().commit();
        		
        	}
        	ResultSet rs = con.createStatement().executeQuery("SELECT * FROM users");
        	while (rs.next()){
        		System.out.println(rs.getString(5));
        	}
        	
        	rs = con.createStatement().executeQuery("SELECT * FROM status");
        	while (rs.next()){
        		System.out.println(rs.getString(2));
        	}
        	
        	rs = con.createStatement().executeQuery("SELECT * FROM resolution");
        	while (rs.next()){
        		System.out.println(rs.getString(2));
        	}
        	
        	rs = con.createStatement().executeQuery("SELECT * FROM priority");
        	while (rs.next()){
        		System.out.println(rs.getString(2));
        	}
        	
        	rs = con.createStatement().executeQuery("SELECT * FROM type");
        	while (rs.next()){
        		System.out.println(rs.getString(2));
        	}
        	
        	rs = con.createStatement().executeQuery("SELECT * FROM project");
        	while (rs.next()){
        		System.out.println(rs.getString(2));
        	}
        	
        	rs = con.createStatement().executeQuery("SELECT * FROM build");
        	while (rs.next()){
        		System.out.println(rs.getString(2));
        	}
        	
        	rs.close();
        	if(con != null){
        		con.close();
        	}

        }catch (Exception e) {
        	e.printStackTrace();
        }finally {
        	HibernateUtil.close();
        }
    }
}
