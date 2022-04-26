package com.mediscreen.patientmicroservice;

import com.mediscreen.patientmicroservice.model.Gender;
import com.mediscreen.patientmicroservice.model.Patient;
import com.mediscreen.patientmicroservice.model.Patient.PatientBuilder;
import com.mediscreen.patientmicroservice.repository.PatientRepository;
import com.mediscreen.patientmicroservice.service.PatientService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientServiceTest {

  private final PatientRepository repository= mock(PatientRepository.class);

  private final PatientService classUnderTest= new PatientService(repository);

  Patient patient1= new PatientBuilder()
      .id(1)
      .firstName("John")
      .lastName("Doe")
      .build();

  Patient patient2= new PatientBuilder()
      .id(2)
      .firstName("Jane")
      .lastName("Doe")
      .build();

  Patient validPatient=new PatientBuilder()
      .id(3)
      .firstName("Dave")
      .lastName("Smith")
      .dateOfBirth(LocalDate.of(2022, 1,1))
      .gender(Gender.M)
      .address("23, Grande Avenue")
      .phone("123-321")
      .build();

  @Test
  void givenTwoPatientExistingWhenGetAllPatientThenReturnListOfPatients() {
    //Given
    when(repository.findAll()).thenReturn(List.of(patient1,patient2));

    //When
    List<Patient> actual= classUnderTest.getAllPatient();

    //Then
    List<Patient>expected= List.of(patient1,patient2);
    assertThat(actual.size()).isEqualTo(2);

    verifyAssertPatientField(actual,expected);
    verify(repository, times(1)).findAll();
  }

  @Test
  void givenNoPatientExistingWhenGetAllThenReturnAnEmptyList() {
    //Given
    when(repository.findAll()).thenReturn(List.of());

    //When
    List<Patient> actual= classUnderTest.getAllPatient();

    //Then
    assertTrue(actual.isEmpty());
  }

  @Test
  void givenPatientExistingWhenGetByIdThenReturnPatient() {
    //Given
    when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(patient1));

    //When
    Optional<Patient> actual= classUnderTest.getPatientById(1);

    //Then
    verifyAssertPatientField(List.of(actual.get()),List.of(patient1));
    verify(repository,times(1)).findById(1);
  }

  @Test
  void givenNoPatientExistingWhenGetByIdThenReturnAnEmptyPatient() {
    //Given
    when(repository.findById(anyInt())).thenReturn(Optional.empty());

    //When
    Optional<Patient> actual= classUnderTest.getPatientById(1);

    //Then
    assertTrue(actual.isEmpty());
    verify(repository,times(1)).findById(1);
  }

  @Test
  void givenAValidPatientNoExistingWhenSavePatientThenReturnThePatientSaved() {
    //Given
    when(repository.save(any())).thenReturn(validPatient);

    //When
    Patient actual= classUnderTest.addPatient(validPatient);

    //Then
    verifyAssertPatientField(List.of(actual),List.of(validPatient));
  }

  @Test
  void givenANotValidPatientWhenAddPatientThenReturnNull() {
    //Given
    Patient notValidPatient= new PatientBuilder()
        .phone("789-987")
        .build();

    //When
    Patient actual= classUnderTest.addPatient(notValidPatient);

    //Then
    assertNull(actual);
    verify(repository,times(0)).save(notValidPatient);
  }

  @Test
  void givenAPatientExistingAndAValidUpdateWhenUpdatePatientThenReturnPatientUpdated() {
    //Given
    Patient patientUpdated= new PatientBuilder()
        .id(3)
        .firstName("John")
        .lastName("Doe")
        .dateOfBirth(LocalDate.of(2022,1,1))
        .gender(Gender.M)
        .address("23, Grande Avenue")
        .phone("123-321")
        .build();

    when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(validPatient));
    when(repository.save(any())).thenReturn(patientUpdated);

    //When
    Patient actual= classUnderTest.updatePatient(3,patientUpdated);

    //Then
    verifyAssertPatientField(List.of(actual),List.of(patientUpdated));
    verify(repository, times(1)).save(validPatient);
  }

  @Test
  void givenAPatientExistingAndANotValidUpdateWhenUpdatePatientThenReturnNull() {
    //Given
    Patient patientUpdated= new PatientBuilder()
        .id(3)
        .firstName("John")
        .lastName("Doe")
        .dateOfBirth(LocalDate.of(2022,1,1))
        .gender(Gender.M)
        .address("23, Grande Avenue")
        .phone("123-321")
        .build();

    when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(validPatient));

    //When
    Patient actual= classUnderTest.updatePatient(3,patientUpdated);

    //Then
    assertNull(actual);
    verify(repository, times(0)).save(patient1);
  }

  @Test
  void givenAPatientNotExistingWhenUpdatePatientThenReturnNull() {
    //Given
    Patient patientUpdated= new PatientBuilder()
        .id(3)
        .firstName("John")
        .lastName("Doe")
        .dateOfBirth(LocalDate.of(2022,1,1))
        .address("23, Grande Avenue")
        .phone("123-321")
        .build();

    when(repository.findById(anyInt())).thenReturn(Optional.empty());

    //When
    Patient actual= classUnderTest.updatePatient(3,patientUpdated);

    //Then
    assertNull(actual);
    verify(repository, times(0)).save(patientUpdated);
  }

  @Test
  void givenAPatientExistingWhenDeletePatientThenPatientDeletedAndReturnTrue() {
    //Given
    when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(validPatient));

    //When
    boolean actual=classUnderTest.deletePatient(3);

    //Then
    assertTrue(actual);
    verify(repository,times(1)).deleteById(3);
  }

  @Test
  void givenAPatientNotExistingWhenDeletePatientThenReturnFalse() {
    //Given
    when(repository.findById(anyInt())).thenReturn(Optional.empty());

    //When
    boolean actual=classUnderTest.deletePatient(3);

    //Then
    assertFalse(actual);
    verify(repository,times(0)).deleteById(3);
  }

  private void verifyAssertPatientField(List<Patient> actual, List<Patient> expected){
    for (int i = 0; i < actual.size(); i++) {
      assertThat(actual.get(i).getFirstName()).isEqualTo(expected.get(i).getFirstName());
      assertThat(actual.get(i).getLastName()).isEqualTo(expected.get(i).getLastName());
      assertThat(actual.get(i).getDateOfBirth()).isEqualTo(expected.get(i).getDateOfBirth());
      assertThat(actual.get(i).getGender()).isEqualTo(expected.get(i).getGender());
      assertThat(actual.get(i).getAddress()).isEqualTo(expected.get(i).getAddress());
      assertThat(actual.get(i).getPhone()).isEqualTo(expected.get(i).getPhone());
    }
  }
}
