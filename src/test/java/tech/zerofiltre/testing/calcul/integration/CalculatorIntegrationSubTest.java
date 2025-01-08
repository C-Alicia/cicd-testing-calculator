package tech.zerofiltre.testing.calcul.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CalculatorIntegrationSubTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSubtractionEndpoint() throws Exception {
        // Arrange
        String leftField = "6";   // Valeur de gauche
        String rightField = "3";  // Valeur de droite
        String typeDropDown = "SUBTRACTION"; // Utilisation de la valeur du type SOUSTRACTION

        // Act
        MvcResult result = mockMvc.perform(
                post("/calculator") // Requête POST pour le formulaire
                .param("leftArgument", leftField)  // Paramètre pour le premier champ
                .param("rightArgument", rightField) // Paramètre pour le second champ
                .param("calculationType", typeDropDown)) // Paramètre pour le type de calcul
            .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 OK
            .andReturn();

        // Assert
        String content = result.getResponse().getContentAsString();

        // Extraction du résultat depuis le HTML pour vérifier la solution
        assertThat(content).contains("<span id=\"solution\">3</span>"); // Vérifie que la solution est "3"
    }
}
