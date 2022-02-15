package day01;

import java.time.LocalDate;

public class Movie {

    private long id;
    private String title;
    private LocalDate releaseDate;

    public Movie(long id, String title, LocalDate localDate) {
        this.id = id;
        this.title = title;
        this.releaseDate = localDate;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getLocalDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                '}';
    }
}
