package com.project.bookstore.entity;

import com.project.bookstore.entity.types.Gender;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "user")
@Table(name = "app_user", schema = "public")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "phoneNumber")
    private String phoneNumber;
    @Column(name = "password")
    private String password;
    @Column(name = "country")
    private String country;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "yearOfBirth")
    private int yearOfBirth;
    @Column(name = "verifiedAccount")
    private boolean verifiedAccount;
    @Column(name = "verificationCode")
    private String verificationCode;
    @Column(name = "verificationCodeTime")
    private LocalDateTime verificationCodeTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            fetch = FetchType.LAZY,
            mappedBy = "reservedUser")
    private List<Reservation> reservations = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @JoinTable(name = "user_libraries", schema = "public",
            joinColumns = @JoinColumn(name = "app_user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "library_id", nullable = false))
    private List<Library> favouriteLibraries = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            fetch = FetchType.LAZY,
            mappedBy = "reviewerUser")
    private List<Reviews> userReviews = new ArrayList<>();

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public boolean isVerifiedAccount() {
        return verifiedAccount;
    }

    public void setVerifiedAccount(boolean verifiedAccount) {
        this.verifiedAccount = verifiedAccount;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public LocalDateTime getVerificationCodeTime() {
        return verificationCodeTime;
    }

    public void setVerificationCodeTime(LocalDateTime verificationCodeTime) {
        this.verificationCodeTime = verificationCodeTime;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Library> getFavouriteLibraries() {
        return favouriteLibraries;
    }

    public void setFavouriteLibraries(List<Library> favouriteLibraries) {
        this.favouriteLibraries = favouriteLibraries;
    }

    public void addLibraryToFavourites(Library library) {
        this.favouriteLibraries.add(library);
    }

    public void removeLibraryFromFavourites(Library library) {
        favouriteLibraries.remove(library);
    }

    public List<Reviews> getUserReviews() {
        return userReviews;
    }

    public void setUserReviews(List<Reviews> userReviews) {
        this.userReviews = userReviews;
    }

    public void addReview(Reviews reviews) {
        this.userReviews.add(reviews);
        reviews.setReviewerUser(this);
    }
}
