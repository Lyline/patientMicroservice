package com.mediscreen.patientmicroservice.controller;

import com.mediscreen.patientmicroservice.model.Patient;
import com.mediscreen.patientmicroservice.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/patientAPI")
public class PatientController {

  private final PatientService service;

  public PatientController(PatientService service) {
    this.service = service;
  }

  @GetMapping("")
  public ResponseEntity<String> getWelcome(){
    return new ResponseEntity<>("Welcome to MedicScreen Patient API", HttpStatus.OK);
  }

  @GetMapping("/patients")
  public ResponseEntity getAllPatients(){
    List<Patient> patients= service.getAllPatient();

    if (!patients.isEmpty()){
      return new ResponseEntity<>(patients,HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/patients/{id}")
  public ResponseEntity getPatientById(@PathVariable int id){
    Optional<Patient> patient=service.getPatientById(id);

    if (patient.isPresent()){
      return new ResponseEntity<>(patient.get(),HttpStatus.OK);
    }return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping(value = "/patients")
  public ResponseEntity<Patient> addPatient(@RequestBody Patient patient){
    Patient patientSaved=service.addPatient(patient);
    return new ResponseEntity<>(patientSaved,HttpStatus.CREATED);
  }

  @PutMapping("/patients/{id}")
  public ResponseEntity<Patient> updatePatient(@PathVariable int id, @RequestBody Patient patientToUpdate){
    Patient patientUpdated= service.updatePatient(id, patientToUpdate);

    if (Objects.isNull(patientUpdated)) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(patientUpdated,HttpStatus.CREATED);
  }

  @DeleteMapping("/patients/{id}")
  public ResponseEntity<Boolean> getDeletePatient(@PathVariable int id){
    boolean patientDeleted= service.deletePatient(id);

    if (patientDeleted){
      return new ResponseEntity<>(true, HttpStatus.OK);
    }
    return new ResponseEntity<>(false, HttpStatus.OK);
  }
}
