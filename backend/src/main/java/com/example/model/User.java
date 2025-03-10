package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import jakarta.persistence.*;
import java.util.List;
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //generate a sequence of unused Long integer value
    @Column
    private Long id;
    @Column(unique = true) //validation on SQL side
    private String emailAddress;
    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //doesn't allow reading it
    private String password;
    @OneToOne(cascade = CascadeType.ALL) //when loading user, load profile as well
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    @OneToMany(mappedBy = "user", orphanRemoval = true) //if deleted, deletes associated records as well
    @LazyCollection(LazyCollectionOption.FALSE) //loads associated categories when loading the user
    private List<Category> categoryList;
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Habit> habitList;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<PracticeTracker> practiceList;

    public User() {
    }

    public User(Long id, String emailAddress, String password) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Habit> getHabitList() {
        return habitList;
    }

    public void setHabitList(List<Habit> habitList) {
        this.habitList = habitList;
    }

    public List<PracticeTracker> getPracticeList() {
        return practiceList;
    }

    public void setPracticeList(List<PracticeTracker> practiceList) {
        this.practiceList = practiceList;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", emailAddress='" + emailAddress + '\'' +
                ", profile=" + profile +
                '}';
    }
}
