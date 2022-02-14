package day01;

import org.mariadb.jdbc.MariaDbDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorsRepository {

    private MariaDbDataSource dataSource;

    public ActorsRepository(MariaDbDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveActor(String name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("insert into actors (actor_name) values (?)")) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot update: " + name, sqle);
        }
    }

    public List<String> findActorsWithPrefix(String prefix) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("select * from actors where actor_name like ?")) {
            stmt.setString(1, prefix + "%");
            return getList(stmt);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot update: " + prefix, sqle);
        }
    }

    private List<String> getList(PreparedStatement stmt) throws SQLException {
        List<String> result = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String actorName = rs.getString("actor_name");
                result.add(actorName);
            }
        }
        return result;
    }
}
