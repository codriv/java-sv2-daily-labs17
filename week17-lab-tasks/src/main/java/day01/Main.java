package day01;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

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
        flyway.clean();
//        flyway.baseline();
        flyway.migrate();

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
//        actorsRepository.saveActor("Jim Doe");
//        System.out.println(actorsRepository.findActorsWithPrefix("jo"));

//        System.out.println(actorsRepository.saveActor("Jani Doe"));
//        System.out.println(actorsRepository.findActorByName("Jim Doe").get());

        MoviesRepository moviesRepository = new MoviesRepository(dataSource);
//        moviesRepository.saveMovie("Titanic", LocalDate.of(1999, 12, 10));
//        moviesRepository.saveMovie("Film", LocalDate.of(1998, 10, 20));
//        moviesRepository.saveMovie("Rajzfoilm", LocalDate.of(1995, 5, 8));
//        System.out.println(moviesRepository.findAllMovies());

        ActorsMoviesRepository actorsMoviesRepository = new ActorsMoviesRepository(dataSource);

        ActorsMoviesService service = new ActorsMoviesService(actorsRepository, moviesRepository, actorsMoviesRepository);

        service.insertMovieWithActors("Titanic", LocalDate.of(1997, 11, 13), List.of("Leonardo", "Kate"));

    }
}