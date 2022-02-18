package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MoviesRepository {

    private DataSource dataSource;

    public MoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Long saveMovie(String title, LocalDate releaseDate) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("insert into movies (title, release_date) values (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, title);
            stmt.setDate(2, Date.valueOf(releaseDate));
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new IllegalStateException("Insert failed to movies!");
                }
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot save movie: " + title, sqle);
        }
    }

    public List<Movie> findAllMovies() {
        List<Movie> movies = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("select id, title, release_date from movies")) {
            while (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
                movies.add(new Movie(id, title, releaseDate));
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Movies not found!", sqle);
        }
        return movies;
    }

    public Optional<Movie> findMovieByTitle(String title) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("select * from movies where title = ?")) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Movie(rs.getLong("id"), rs.getString("title"), rs.getDate("release_date").toLocalDate()));
                } else {
                    throw new IllegalStateException("Movie not found!");
                }
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot query!", sqle);
        }
    }
}
