package securitybasicauth.demo.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "users")
public class RegisterUserModel {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;



    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ownerId")
    private List<AccommodationsModel> accommodations;


    public List<AccommodationsModel> getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(List<AccommodationsModel> accommodations) {
        this.accommodations = accommodations;
    }

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private String gender;

    @Column(name = "age")
    private int age;

    @Column(name = "password")
    private String password;

    public RegisterUserModel() {
    }

    public RegisterUserModel(List<AccommodationsModel> accommodations) {
        this.accommodations = accommodations;
    }

    public int getId() {
        return id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword(){return password; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
