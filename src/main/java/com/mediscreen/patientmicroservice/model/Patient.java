package com.mediscreen.patientmicroservice.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Table(name = "patient")
@Entity
public class Patient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotEmpty(message = "FirstName is mandatory")
  private String firstName;

  @NotEmpty(message = "LastName is mandatory")
  private String lastName;

  @NotNull(message = "Date of birth is mandatory")
  private LocalDate dateOfBirth;

  @Column(name = "gender", columnDefinition = "varchar(1)")
  @NotNull(message = "Gender is mandatory")
  private String gender;

  private String address;
  private String phone;

  public Patient(){}

  private Patient(PatientBuilder builder){
    this.id=builder.id;
    this.firstName=builder.firstName;
    this.lastName=builder.lastName;
    this.dateOfBirth=builder.dateOfBirth;
    this.gender=builder.gender;
    this.address= builder.address;
    this.phone= builder.phone;
  }

  public Integer getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public String getGender() {
    return gender;
  }

  public String getAddress() {
    return address;
  }

  public String getPhone() {
    return phone;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public static class PatientBuilder{
    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String phone;

    public PatientBuilder id(Integer id){
      this.id=id;
      return this;
    }

    public PatientBuilder firstName(String firstName){
      this.firstName=firstName;
      return this;
    }

    public PatientBuilder lastName(String lastName){
      this.lastName=lastName;
      return this;
    }

    public PatientBuilder dateOfBirth(LocalDate DoB){
      this.dateOfBirth= DoB;
      return this;
    }

    public PatientBuilder gender(String gender){
      this.gender=gender;
      return this;
    }

    public PatientBuilder address(String address){
      this.address=address;
      return this;
    }

    public PatientBuilder phone(String phone){
      this.phone=phone;
      return this;
    }

    public Patient build(){
      return new Patient(this);
    }
  }
}
