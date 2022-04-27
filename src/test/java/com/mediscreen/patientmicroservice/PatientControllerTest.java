package com.mediscreen.patientmicroservice;

import com.mediscreen.patientmicroservice.model.Gender;
import com.mediscreen.patientmicroservice.model.Patient;
import com.mediscreen.patientmicroservice.model.Patient.PatientBuilder;
import com.mediscreen.patientmicroservice.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class PatientControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PatientService service;

  Patient patient1= new PatientBuilder()
      .id(1)
      .firstName("John")
      .lastName("Doe")
      .dateOfBirth(LocalDate.of(2022,1,1))
      .gender(Gender.M)
      .address("Address of John")
      .phone("123-456")
      .build();

  Patient patient2= new PatientBuilder()
      .id(2)
      .firstName("Jane")
      .lastName("Doe")
      .dateOfBirth(LocalDate.of(2022,1,1))
      .gender(Gender.F)
      .build();

  @Test
  void displayHomePage() throws Exception {
    //Given
    //When
    mockMvc.perform(get("/patientAPI"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Welcome to MedicScreen Patient API")));
  }

  @Test
  void givenTwoPatientsWhenGetAllThenReturnListOfPatientsWithStatus200() throws Exception {
    //Given
    when(service.getAllPatient()).thenReturn(List.of(patient1,patient2));

    //When
    mockMvc.perform(get("/patientAPI/patients"))
        .andExpect(status().isOk())
        .andExpect(content().json(
            "[" +
                "{\"id\":1," +
                "\"firstName\":\"John\"," +
                "\"lastName\":\"Doe\"," +
                "\"dateOfBirth\":\"2022-01-01\"," +
                "\"gender\":\"M\"," +
                "\"address\":\"Address of John\"," +
                "\"phone\":\"123-456\"}," +

                "{\"id\":2," +
                "\"firstName\":\"Jane\"," +
                "\"lastName\":\"Doe\"," +
                "\"dateOfBirth\":\"2022-01-01\"," +
                "\"gender\":\"F\"," +
                "\"address\":null," +
                "\"phone\":null}]"
        ));
  }

  @Test
  void givenNoPatientWhenGetAllPatientsThenReturnAnEmptyListWithStatus204() throws Exception {
    //Given
    when(service.getAllPatient()).thenReturn(Collections.emptyList());

    //When
    mockMvc.perform(get("/patientAPI/patients"))
        .andExpect(status().isNoContent());
  }

  @Test
  void givenAPatientExistingWhenGetPatientByIdThenReturnPatientFoundWithStatus200() throws Exception {
    //Given
    when(service.getPatientById(anyInt())).thenReturn(Optional.ofNullable(patient1));
    //When
    mockMvc.perform(get("/patientAPI/patients/1"))
        .andExpect(status().isOk())
        .andExpect(content().json(
            "{\"id\":1," +
                "\"firstName\":\"John\"," +
                "\"lastName\":\"Doe\"," +
                "\"dateOfBirth\":\"2022-01-01\"," +
                "\"gender\":\"M\"," +
                "\"address\":\"Address of John\"," +
                "\"phone\":\"123-456\"}"
        ));
  }

  @Test
  void givenAPatientNotExistingWhenGetPatientByIdThenReturnStatus204() throws Exception {
    //Given
    when(service.getPatientById(anyInt())).thenReturn(Optional.empty());

    //When
    mockMvc.perform(get("/patientAPI/patients/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  void givenANewValidPatientWhenAddPatientThenPatientAddedWithStatus201() throws Exception {
    //Given
    when(service.addPatient(any())).thenReturn(patient1);

    //Then
    mockMvc.perform(post("/patientAPI/patients")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{"+
            "\"firstName\":\"John\"," +
            "\"lastName\":\"Doe\"," +
            "\"dateOfBirth\":\"2022-01-01\"," +
            "\"gender\":\"M\"," +
            "\"address\":\"Address of John\"," +
            "\"phone\":\"123-456\"}"))
        .andExpect(status().isCreated());
  }

  @Test
  void givenANewNotValidPatientWhenAddPatientThenReturnStatus422() throws Exception {
    //Given
    //When
    mockMvc.perform(post("/patientAPI/patients")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{"+
            "\"firstName\":null," +
            "\"lastName\":\"\"," +
            "\"dateOfBirth\":null," +
            "\"gender\":null," +
            "\"address\":null," +
            "\"phone\":null}"))
        .andDo(print()).andExpect(status().isUnprocessableEntity());
  }

  @Test
  void givenAPatientExistingWithAValidUpdateWhenUpdatePatientThenPatientUpdatedWithStatus201() throws Exception {
    //Given
    Patient patientToUpdate= new PatientBuilder()
        .id(1)
        .firstName("Johnny")
        .lastName("Clash")
        .dateOfBirth(LocalDate.of(2022,2,2))
        .gender(Gender.F)
        .address("Address of John")
        .phone("123-456")
        .build();

    when(service.updatePatient(anyInt(),any())).thenReturn(patientToUpdate);

    //When
    mockMvc.perform(put("/patientAPI/patients/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{"+
            "\"firstName\":\"Johnny\"," +
            "\"lastName\":\"Clash\"," +
            "\"dateOfBirth\":\"2022-02-02\"," +
            "\"gender\":\"F\"," +
            "\"address\":null," +
            "\"phone\":null}"))

        .andExpect(status().isCreated())
        .andExpect(content().json(
            "{\"id\":1," +
                "\"firstName\":\"Johnny\"," +
                "\"lastName\":\"Clash\"," +
                "\"dateOfBirth\":\"2022-02-02\"," +
                "\"gender\":\"F\"," +
                "\"address\":\"Address of John\"," +
                "\"phone\":\"123-456\"}"
        ));
  }

  @Test
  void givenAPatientExistingWithANotValidUpdateWhenUpdatePatientThenReturnStatus422() throws Exception {
    //Given
    //When
    mockMvc.perform(put("/patientAPI/patients/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{"+
                "\"firstName\":\"\"," +
                "\"lastName\":\"\"," +
                "\"dateOfBirth\":\"\"," +
                "\"gender\":null," +
                "\"address\":null," +
                "\"phone\":null}"))

        .andExpect(status().isUnprocessableEntity());
  }

  @Test
  void givenAPatientNotExitingWithAValidUpdateWhenUpdatePatientThenReturn204() throws Exception {
    //Given
    when(service.updatePatient(anyInt(),any())).thenReturn(null);

    //When
    mockMvc.perform(put("/patientAPI/patients/4")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{"+
            "\"firstName\":\"Johnny\"," +
            "\"lastName\":\"Clash\"," +
            "\"dateOfBirth\":\"2022-02-02\"," +
            "\"gender\":\"F\"," +
            "\"address\":null," +
            "\"phone\":null}"))
        .andExpect(status().isNoContent());
  }

  @Test
  void givenAPatientExistingWhenDeletePatientThenPatientDeletedWithStatus200() throws Exception {
    //Given
    when(service.deletePatient(anyInt())).thenReturn(true);

    //When
    mockMvc.perform(delete("/patientAPI/patients/1"))
        .andExpect(status().isOk());
  }

  @Test
  void givenAPatientNotExistingWhenDeletePatientThenReturnStatus204() throws Exception {
    //Given
    when(service.deletePatient(anyInt())).thenReturn(false);

    //When
    mockMvc.perform(delete("/patientAPI/patients/1"))
        .andExpect(status().isNoContent());
  }
}
