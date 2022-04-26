package com.mediscreen.patientmicroservice.service;

import com.mediscreen.patientmicroservice.model.Patient;
import com.mediscreen.patientmicroservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

  private final PatientRepository repository;

  public PatientService(PatientRepository repository) {
    this.repository = repository;
  }

  public List<Patient> getAllPatient() {
    return repository.findAll();
  }

  public Optional<Patient> getPatientById(int id) {
    return repository.findById(id);
  }

  public Patient addPatient(Patient patient) {
    boolean patientIsValid= isValid(patient);

    if (patientIsValid){
      return repository.save(patient);
    }
    return null;
  }

  public Patient updatePatient(int id, Patient patientToUpdate) {
    Optional<Patient> patientIsExist= repository.findById(id);
    boolean patientIsValid=isValid(patientToUpdate);

    if (patientIsValid & patientIsExist.isPresent()){
      Patient patient= updateInitialPatient(patientToUpdate, patientIsExist);
      return repository.save(patient);
    }
    return null;
  }

  public boolean deletePatient(int id) {
    Optional patient= repository.findById(id);

    if(patient.isPresent()){
      repository.deleteById(id);
      return true;
    }
    return false;
  }

  private boolean isValid(Patient patient) {
    return !(patient.getFirstName() == null |
        patient.getLastName() == null |
        patient.getDateOfBirth() == null |
        patient.getGender() == null);
  }

  private Patient updateInitialPatient(Patient patientToUpdate, Optional<Patient> patientIsExist) {
    patientIsExist.get().setFirstName(patientToUpdate.getFirstName());
    patientIsExist.get().setLastName(patientToUpdate.getLastName());
    patientIsExist.get().setDateOfBirth(patientToUpdate.getDateOfBirth());
    patientIsExist.get().setGender(patientToUpdate.getGender());

    if(patientToUpdate.getAddress()!=null){
      patientIsExist.get().setAddress(patientToUpdate.getAddress());
    }
    if (patientToUpdate.getPhone()!=null){
      patientIsExist.get().setPhone(patientToUpdate.getPhone());
    }
    return patientIsExist.get();
  }
}
