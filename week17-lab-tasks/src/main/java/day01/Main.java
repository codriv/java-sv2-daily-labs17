package day01;

import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {

        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");
            dataSource.setUserName("root");
            dataSource.setPassword("64o1qNpG8m");
        } catch (SQLException throwables) {
            throw new IllegalStateException("Cannot reach Database!");
        }

//        try (Connection connection = dataSource.getConnection()){
//            Statement stmt = connection.createStatement();
//            stmt.executeUpdate("insert into actors (actor_name) values ('John Doe')");
//        } catch (SQLException sql) {
//            throw new IllegalStateException("Cannot connect");
//        }

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        actorsRepository.saveActor("Jack Doe");
        System.out.println(actorsRepository.findActorsWithPrefix("jo"));
    }
}
