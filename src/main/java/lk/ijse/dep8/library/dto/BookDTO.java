package lk.ijse.dep8.library.dto;

public class BookDTO {
    private String isbn;
    private String name;
    private String author;

    public BookDTO() {
    }

    public BookDTO(String isbn, String name, String author) {
        this.setIsbn(isbn);
        this.setName(name);
        this.setAuthor(author);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "isbn='" + isbn + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
