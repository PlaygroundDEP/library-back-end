package lk.ijse.dep8.library.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTransient;

import java.io.Serializable;
import java.util.Base64;

public class BookDTO implements Serializable {
    private String isbn;
    private String name;
    private String author;
    @JsonbTransient
    private byte[] preview;
    private boolean availability;

    public BookDTO() {
    }

    public BookDTO(String isbn, String name, String author) {
        this.setIsbn(isbn);
        this.setName(name);
        this.setAuthor(author);
    }

    public BookDTO(String isbn, String name, String author, byte[] preview) {
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.preview = preview;
    }

    public BookDTO(String isbn, String name, String author, byte[] preview, boolean availability) {
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.preview = preview;
        this.availability = availability;
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

    public byte[] getPreview() {
        return preview;
    }

    public void setPreview(byte[] preview) {
        this.preview = preview;
    }

    public boolean isAvailability() {
        return availability;
    }

    @JsonbTransient
    public void setAvailability(boolean availability) {
        this.availability = availability;
    }


    @JsonbProperty(value = "preview",  nillable = true)
    public String getPreviewAsDataURI() {
        return (preview == null ? null : "data:image/*;base64,"+
                Base64.getEncoder().encodeToString(preview));
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
