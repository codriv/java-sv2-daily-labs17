package day03;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/bookstore?useUnicode=true&allowMultiQueries=true");
            dataSource.setUserName("root");
            dataSource.setPassword("64o1qNpG8m");
        } catch (SQLException throwables) {
            throw new IllegalStateException("Cannot reach Database!");
        }

        Flyway flyway = Flyway.configure().dataSource(dataSource).locations("db/migration/bookstore").load();
        flyway.clean();
        flyway.migrate();

        BookRepository bookRepository = new BookRepository(dataSource);

        bookRepository.insertBook("Fekete István", "Vuk", 3400, 10);
        bookRepository.insertBook("Fekete István", "Téli Berek", 3600, 8);
        bookRepository.insertBook("Fekete Péter", "Kártyatrükkök", 1400, 2);

        System.out.println(bookRepository.findBookByWriter("fekete"));

        bookRepository.update(1L, 30);

    }

}
