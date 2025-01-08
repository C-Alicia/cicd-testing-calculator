package tech.zerofiltre.testing.calcul.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import tech.zerofiltre.testing.calcul.domain.model.Calculation;
import tech.zerofiltre.testing.calcul.domain.model.CalculationModel;
import tech.zerofiltre.testing.calcul.domain.model.CalculationType;
import tech.zerofiltre.testing.calcul.repository.CalculationModelRepository;
import tech.zerofiltre.testing.calcul.service.CalculatorService;


@ExtendWith(SpringExtension.class)
public class CalculatorControllerTest {

  CalculatorController calculatorController;

  @MockBean
  CalculatorService calculatorService;

  @MockBean
  CalculationModelRepository calculationModelRepository;

  @BeforeEach
  void init(){
    calculatorController = new CalculatorController(calculatorService,calculationModelRepository);
  }

  @Test
  void calculate_mustUseCalculationModelRepository() {
      CalculationModel calculationModel = new CalculationModel(CalculationType.ADDITION, 12, 13);
      Calculation calculation = new Calculation();
      calculation.setCalculationType(CalculationType.ADDITION.name());
      calculation.setLeftArgument(12);
      calculation.setRightArgument(13);

      // GIVEN
      when(calculatorService.calculate(any())).thenReturn(calculationModel);
      when(calculationModelRepository.save(any())).thenReturn(calculationModel);

      // WHEN
      calculatorController.calculate(calculation, null, new ConcurrentModel());

      // THEN
      verify(calculationModelRepository, times(1)).save(calculationModel);
  }

  @Test
  void calculate_shouldNotSaveIfValidationFails() {
      Calculation calculation = new Calculation();
      calculation.setCalculationType(CalculationType.ADDITION.name());  // Valid type

      // Simulate a BindingResult with a validation error
      BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "calculation");
      bindingResult.rejectValue("calculationType", "error.calculationType", "Invalid calculation type");

      // WHEN
      calculatorController.calculate(calculation, bindingResult, new ConcurrentModel());

      // THEN
      verify(calculationModelRepository, times(0)).save(any());  // Should not call save due to validation error
  }

  @Test
  void calculate_shouldSaveWhenValidationPasses() {
      CalculationModel calculationModel = new CalculationModel(CalculationType.ADDITION, 12, 13);
      Calculation calculation = new Calculation();
      calculation.setCalculationType(CalculationType.ADDITION.name());
      calculation.setLeftArgument(12);
      calculation.setRightArgument(13);

      // GIVEN
      BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "calculation"); // No errors
      when(calculatorService.calculate(any())).thenReturn(calculationModel);
      when(calculationModelRepository.save(any())).thenReturn(calculationModel);

      // WHEN
      calculatorController.calculate(calculation, bindingResult, new ConcurrentModel());

      // THEN
      verify(calculationModelRepository, times(1)).save(calculationModel);  // Should call save since validation passed
  }
}
