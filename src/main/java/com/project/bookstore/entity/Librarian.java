package com.project.bookstore.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "librarian")
@Table(name = "librarian", schema = "public")
public class Librarian {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    @JoinColumn(name = "librarian_id",
            referencedColumnName = "id")
    private Library library;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
        library.setLibrarian(this);
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
}
