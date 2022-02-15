package day01;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDate;

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

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.baseline();
        flyway.migrate();

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
//        actorsRepository.setNextId();
        actorsRepository.saveActor("Jim Doe");
        System.out.println(actorsRepository.findActorsWithPrefix("jo"));

        MoviesRepository moviesRepository = new MoviesRepository(dataSource);
        moviesRepository.saveMovie("Titanic", LocalDate.of(1999, 12, 10));
        moviesRepository.saveMovie("Film", LocalDate.of(1998, 10, 20));
        moviesRepository.saveMovie("Rajzfoilm", LocalDate.of(1995, 5, 8));
        System.out.println(moviesRepository.findAllMovies());
    }
}
