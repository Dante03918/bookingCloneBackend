package securitybasicauth.demo.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class AccommodationsModel {


    @Id
    @GeneratedValue
    private int id;
    private int ownerId;
    private String ownerEmail;
    private String imageUrl;
    private String description;
    private long price;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "accommodationId")
    private List<DatesModel> reservations;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public List<DatesModel> getReservations() {
        return reservations;
    }

    public void setReservations(List<DatesModel> reservations) {
        this.reservations = reservations;
    }
}
