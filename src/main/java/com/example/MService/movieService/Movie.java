package com.example.MService.movieService;

/**
 * Created by alokkulkarni on 14/10/17.
 */
public class Movie {

    private String movieName;
    private String year;

    public Movie() {
    }

    public Movie(String movieName, String year) {
        this.movieName = movieName;
        this.year = year;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;

        Movie movie = (Movie) o;

        if (getMovieName() != null ? !getMovieName().equals(movie.getMovieName()) : movie.getMovieName() != null)
            return false;
        return getYear() != null ? getYear().equals(movie.getYear()) : movie.getYear() == null;
    }

    @Override
    public int hashCode() {
        int result = getMovieName() != null ? getMovieName().hashCode() : 0;
        result = 31 * result + (getYear() != null ? getYear().hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Movie{" +
                "movieName='" + movieName + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
