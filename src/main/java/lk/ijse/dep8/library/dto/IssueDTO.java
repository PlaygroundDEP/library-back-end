package lk.ijse.dep8.library.dto;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbTransient;

import java.sql.Date;

public class IssueDTO {
    private int id;
    private String nic;
    private String isbn;
    @JsonbDateFormat("yyyy-MM-dd")
    private Date date;
    private boolean availability;

    public IssueDTO() {
    }

    public IssueDTO(String nic, String isbn) {
        this.nic = nic;
        this.isbn = isbn;
    }

    public IssueDTO(int id, String nic, String isbn, Date date) {
        this.id = id;
        this.nic = nic;
        this.isbn = isbn;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isAvailability() {
        return availability;
    }

    @JsonbTransient
    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}
