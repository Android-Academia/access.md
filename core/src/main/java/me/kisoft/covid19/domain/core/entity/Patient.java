/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kisoft.covid19.domain.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import me.kisoft.covid19.domain.auth.entity.User;
import me.kisoft.covid19.domain.auth.enums.UserRole;
import me.kisoft.covid19.domain.entity.DomainEntity;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author tareq
 */
@Entity(name = "Patient")
@Table(name = "APP_USER")
@Getter
@Setter
@NamedQueries({
  @NamedQuery(name = "Patient.byDoctorAndId", query = "SELECT p  from Patient p WHERE p.id=(:patient_id) AND p.doctor.id=(:doctor_id)")
})
public class Patient extends DomainEntity {

  @JsonIgnore
  private String password;
  private String username;
  @Enumerated(EnumType.STRING)
  private UserRole userRole;
  private String telephoneNumber;
  private String firstName;
  private String lastName;
  @ManyToOne
  @Access(AccessType.PROPERTY)
  @JsonIgnore
  private Doctor doctor;

  @Access(AccessType.PROPERTY)
  @OneToOne(cascade = CascadeType.ALL)
  MedicalProfile profile = new MedicalProfile();

  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL)
  private List<Reccomendation> reccomendations = new ArrayList<>();

  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL)
  private List<Question> questions = new ArrayList<>();

  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL)
  private List<Symptom> symptoms = new ArrayList<>();

  public Patient(User user) {
    this.setUsername(user.getUsername());
    this.setPassword(user.getPassword());
    this.setUserRole(UserRole.ROLE_PATIENT);
    this.setTelephoneNumber(user.getTelephoneNumber());
    this.setFirstName(user.getFirstName());
    this.setLastName(user.getLastName());
  }

  public Patient() {

  }

  @JsonIgnore
  public String getPassword() {
    return this.password;
  }

  @JsonProperty
  public void setPassword(String password) {
    this.password = password;
  }

  public void addQuestion(Question question) {
    if (questions == null) {
      questions = new ArrayList<>();
    }
    question.setPatient(this);
    questions.add(question);
  }

  public void addReccomendation(Reccomendation reccomendation) {
    if (reccomendations == null) {
      reccomendations = new ArrayList<>();
    }
    reccomendation.setPatient(this);
    reccomendations.add(reccomendation);
  }

  public void answerQuestion(String answer, Long questionId) {
    Question foundQuestion = questions.stream()
            .filter(question -> Objects.equals(questionId, question.getId())).findFirst().orElse(null);
    if (foundQuestion != null) {
      Answer questionAnswer = new Answer();
      questionAnswer.setAnswer(answer);
      foundQuestion.answerQuestion(questionAnswer);
    }
  }

  public void markAnswerSeen(Long answerId) {
    this.getQuestions()
            .stream()
            .flatMap(question -> question.getAnswers().stream())
            .filter(answer -> Objects.equals(answer.getId(), answerId))
            .findFirst().orElse(new Answer()).setSeen(true);
  }

  public void markSymptomSeen(Long symptomId) {
    this.getSymptoms()
            .stream()
            .filter(symptom -> Objects.equals(symptom.getId(), symptomId))
            .findFirst().orElse(new Symptom()).setSeen(true);
  }

  public void addSymptom(Symptom symptom) {
    if (symptoms == null) {
      symptoms = new ArrayList<>();
    }
    symptom.setPatient(this);
    symptoms.add(symptom);

  }

  @Override
  public String getEntityName() {
    return "patient";
  }

  public List<Answer> getUnseenAnswers() {
    return this.getQuestions().stream()
            .flatMap(question -> question.getAnswers().stream())
            .filter(answer -> ! answer.isSeen()).collect(Collectors.toList());
  }

  public List<Symptom> getUnseenSymptoms() {
    return this.getSymptoms().stream()
            .filter(symptom -> !symptom.isSeen()).collect(Collectors.toList());
  }

}
