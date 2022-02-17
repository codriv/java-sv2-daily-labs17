package day01;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ActorsMoviesRepository {

    private DataSource dataSource;

    public ActorsMoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertActorAndMovie(long actorId, long movieId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("insert into `movies-actors` (`actor_id`, `movie_id`) values (?, ?)")) {
            ps.setLong(1, actorId);
            ps.setLong(2, movieId);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot insert row to actors-movies", sqle);
        }
    }
}
