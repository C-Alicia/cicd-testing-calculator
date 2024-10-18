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
class CalculatorIntegrationMultTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testMultiplyEndpoint() throws Exception {
        // Arrange
        String leftField = "6";
        String rightField = "4";
        String typeDropDown = "MULTIPLICATION"; // Utilisation de la valeur du type Multiply

        // Act
        MvcResult result = mockMvc.perform(
                post("/calculator") // Requête POST car le formulaire utilise method="post"
                .param("leftArgument", leftField)  // Paramètres correspondant aux champs du formulaire
                .param("rightArgument", rightField)
                .param("calculationType", typeDropDown))
            .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 OK
            .andReturn();

        // Assert
        String content = result.getResponse().getContentAsString();
        
        // Extraction du résultat depuis le HTML pour vérifier la solution
        assertThat(content).contains("<span id=\"solution\">24</span>");  // Vérifie que la solution est "8"
    }
}
