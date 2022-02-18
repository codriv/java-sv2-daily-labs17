package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActorsRepository {

    private DataSource dataSource;

    public ActorsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long saveActor(String name) {
        setNextId();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("insert into actors (actor_name) values (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            return getGeneratedId(stmt);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot update: " + name, sqle);
        }
    }

    private long getGeneratedId(PreparedStatement stmt) throws SQLException {
        try(ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new IllegalStateException("No generated Id!");
            }
        }
    }

    public List<String> findActorsWithPrefix(String prefix) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("select * from actors where actor_name like ?")) {
            stmt.setString(1, prefix + "%");
           return getList(prefix, stmt);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot update: " + prefix, sqle);
        }
    }

    private List<String> getList(String prefix, PreparedStatement stmt) throws SQLException{
        List<String> result = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                String actorName = rs.getString("actor_name");
                result.add(actorName);
            }
        }
        return result;
    }

    public void setNextId() {
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("select max(id) from actors;")) {
            rs.next();
            int nextId = rs.getInt(1);
            stmt.executeQuery("alter table actors auto_increment = " + nextId + ";");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot query!", sqle);
        }
    }

    public Optional<Actor> findActorByName(String name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("select * from actors where actor_name = ?")) {
            stmt.setString(1, name);
            return getActor(stmt);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot query by name!", sqle);
        }
    }

    private Optional<Actor> getActor(PreparedStatement stmt) throws SQLException {
        try(ResultSet rs = stmt.executeQuery()) {
            String actor_name = null;
            if (rs.next()) {
                long id = rs.getLong("id");
                actor_name = rs.getString("actor_name");
                return Optional.of(new Actor(id, actor_name));
            } else {
                return Optional.empty();
            }
        }
    }
}
