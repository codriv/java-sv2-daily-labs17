package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class RatingsRepository {

    private DataSource dataSource;

    public RatingsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertRating(long movieId, List<Integer> ratings) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            String sql = "insert into ratings (`movie_id`, `rating`) values (?, ?);" +
                    "UPDATE movies SET `average_rating` = (SELECT ROUND(AVG(rating), 1)" +
                    "FROM ratings GROUP BY movie_id HAVING movie_id = ?) WHERE id = ?";
            executeQueries(movieId, ratings, connection, sql);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot insert rating!", sqle);
        }
    }

    private void executeQueries(long movieId, List<Integer> ratings, Connection connection, String sql) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Integer actual : ratings) {
                if (actual < 1 || actual > 5) {
                    connection.rollback();
                    throw new IllegalArgumentException("Invalid rating!");
                }
                stmt.setLong(1, movieId);
                stmt.setLong(2, actual);
                stmt.setLong(3, movieId);
                stmt.setLong(4, movieId);
                stmt.executeUpdate();
            }
            connection.commit();
        }
    }

    public double selectAverageRating(long id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT ROUND(AVG(rating), 1) AS average FROM ratings GROUP BY movie_id HAVING movie_id = ?")) {
            ps.setLong(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("average");
                } else {
                    throw new IllegalStateException("Movie not found!");
                }
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot query rating!", sqle);
        }
    }
}
