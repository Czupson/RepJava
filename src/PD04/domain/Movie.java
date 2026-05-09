package PD04.domain;

public record Movie(
        int id,
        String title,
        String director,
        int year,
        Category category
){
    public Movie {
        if (title == null || title.isBlank()){
            throw new IllegalArgumentException("title cannot be blank");
        }
        if (director == null || director.isBlank()){
            throw new IllegalArgumentException("director cannot be blank");
        }
        if (category == null){
            throw new IllegalArgumentException("category cannot be null");
        }
        if (year < 1888) {
            throw new IllegalArgumentException("year must be >= 1888");
        }
    }
}
