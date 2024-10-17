package tech.zerofiltre.testing.calcul.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LoggingExtension.class)
public class CalculatorTesting {

    private Calculator calculatorTest;
    private Logger logger;

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @BeforeEach
    void initCalculator() {
        if (logger != null) {
            logger.info("Appel avant chaque test");
        }
        calculatorTest = new Calculator();
    }

    @AfterEach
    void undefCalculator() {
        if (logger != null) {
            logger.info("Appel après chaque test");
        }
        calculatorTest = null;
    }

    @BeforeAll
    static void initStartingTime() {
        System.out.println("Appel avant tous les tests");
        Instant.now();
    }

    @Test
    void testAdd() {
        // Arrange
        final int a = 2;
        final int b = 3;

        // Act
        final int somme = calculatorTest.add(a, b);

        // Assert
        assertThat(somme).isEqualTo(5);
        assertEquals(5, somme);
    }

    @Test
    void testMultiply() {
        // Arrange
        final int a = 5;
        final int b = 4;

        // Act
        final int product = calculatorTest.multiply(a, b);

        // Assert
        assertThat(product).isEqualTo(20);
        assertEquals(20, product);
    }

    @Test
    void testSubtract() {
        // Arrange
        final int a = 10;
        final int b = 3;

        // Act
        final int difference = calculatorTest.sub(a, b);

        // Assert
        assertThat(difference).isEqualTo(7);
        assertEquals(7, difference);
    }

    @Test
    void testDivide() {
        // Arrange
        final int a = 10;
        final int b = 2;

        // Act
        final int quotient = calculatorTest.divide(a, b);

        // Assert
        assertThat(quotient).isEqualTo(5);
        assertEquals(5, quotient);
    }
}
