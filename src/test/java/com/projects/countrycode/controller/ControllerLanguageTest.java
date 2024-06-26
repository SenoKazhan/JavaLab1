package com.projects.countrycode.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.projects.countrycode.domain.Country;
import com.projects.countrycode.domain.Language;
import com.projects.countrycode.repodao.CountryRepository;
import com.projects.countrycode.repodao.LanguageRepository;
import com.projects.countrycode.service.CounterService;
import com.projects.countrycode.service.LanguageService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ControllerLanguageTest {

  @Mock private LanguageService languageService;

  @Mock private LanguageRepository languageRepository;

  @Mock private CountryRepository countryRepository;

  @Mock private ControllerLanguage languageController;
  @Mock private CounterService counterService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    languageController =
        new ControllerLanguage(languageService, languageRepository, countryRepository);
  }


  @Test
  void findLanguageById_shouldReturnLanguage() {
    // Arrange
    Integer languageId = 1;
    Language expectedLanguage = new Language("English");
    expectedLanguage.setId(languageId);
    when(languageService.getLanguageById(languageId)).thenReturn(expectedLanguage);

    // Act
    Language result = languageController.findLanguageById(languageId);

    // Assert
    assertEquals(expectedLanguage, result);
  }

  @Test
  void findCouByLang_shouldReturnListOfCountries() {
    // Arrange
    Integer languageId = 1;
    List<Country> expectedCountries = new ArrayList<>();
    expectedCountries.add(new Country(1, "USA", "US", 1L));
    expectedCountries.add(new Country(2, "France", "FR", 33L));
    when(countryRepository.findCountriesByLanguageId(languageId)).thenReturn(expectedCountries);

    // Act
    List<Country> result = languageController.findCouByLang(languageId);

    // Assert
    assertEquals(expectedCountries, result);
  }

  @Test
  void createLanguage_shouldReturnSuccessMessage() {

    Language language = new Language("Spanish");

    // Act
    String result = languageController.createLanguage(language);

    // Assert
    verify(languageService).save(language);
    assertEquals("Language saved successfully", result);
  }

  @Test
  void addCountryToLanguage_shouldReturnSuccessMessage() {
    // Arrange
    Integer languageId = 1;
    Language language = new Language("English");
    language.setId(languageId);
    Country country = new Country(1, "USA", "US", 1L);

    // Mock behavior of languageRepository.findById
    when(languageRepository.findById(languageId)).thenReturn(Optional.of(language));

    // Mock behavior of countryRepository.findByName
    when(countryRepository.findByName(country.getName())).thenReturn(Optional.of(country));

    // Act
    ResponseEntity<String> result = languageController.addCountryToLanguage(languageId, country);

    // Assert
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals("Страна успешно добавлена к языку", result.getBody());
  }

  @Test
  void addCountryToLanguage_shouldReturnNotFoundWhenLanguageNotFound() {

    Integer languageId = 1;
    Country country = new Country(1, "USA", "US", 1L);

    when(languageRepository.findById(languageId)).thenReturn(Optional.empty());

    ResponseEntity<String> result = languageController.addCountryToLanguage(languageId, country);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
  }

  @Test
  void updateLanguage_shouldReturnSuccessMessage() {

    Integer languageId = 1;
    Language language = new Language("French");
    language.setId(languageId);

    // Act
    String result = languageController.updateLanguage(languageId, language);

    // Assert
    verify(languageService).update(language, languageId);
    assertEquals("Language updated successfully", result);
  }

  @Test
  void deleteLanguage_shouldDeleteLanguage() {

    Integer languageId = 1;
    Language language = new Language("German");
    language.setId(languageId);
    Optional<Language> languageOptional = Optional.of(language);
    when(languageRepository.findById(languageId)).thenReturn(languageOptional);

    // Act
    ResponseEntity<Language> result = languageController.deleteLanguage(languageId);

    // Assert
    verify(languageService).deleteLanguage(languageId);
    assertEquals(HttpStatus.OK, result.getStatusCode());
  }

  @Test
  void deleteLanguage_shouldReturnNotFoundWhenLanguageNotFound() {
    // Arrange
    Integer languageId = 1;
    // Return an empty optional to simulate that the language is not found
    when(languageRepository.findById(languageId)).thenReturn(Optional.empty());

    // Act
    ResponseEntity<Language> result = languageController.deleteLanguage(languageId);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
  }
}
