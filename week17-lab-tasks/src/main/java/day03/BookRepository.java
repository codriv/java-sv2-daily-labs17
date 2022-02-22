package day03;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class BookRepository {

    private JdbcTemplate jdbcTemplate;

    public BookRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insertBook(String writer, String title, int price, int pieces) {
        jdbcTemplate.update("insert into books (writer, title, price, pieces) values (?, ?, ?, ?)", writer, title, price, pieces);
    }

    public List<Book> findBookByWriter(String prefix) {
        return jdbcTemplate.query("select * from books where writer like ?", (rs, rowNum) -> new Book(rs.getLong("id"), rs.getString("writer"), rs.getString("title"), rs.getInt("price"), rs.getInt("pieces")), prefix + "%");
    }

    public void update(Long id, int pieces) {
        jdbcTemplate.update("update books set pieces = pieces + ? where id = ?", pieces, id);
    }


}
