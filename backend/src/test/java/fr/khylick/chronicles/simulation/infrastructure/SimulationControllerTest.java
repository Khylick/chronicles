package fr.khylick.chronicles.simulation.infrastructure;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SimulationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateSimulationAtTurnZero() throws Exception {
        mockMvc.perform(
                        post("/api/simulation")
                                .param("width", "80")
                                .param("height", "48")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.turn").value(0))
                .andExpect(jsonPath("$.world.width").value(80))
                .andExpect(jsonPath("$.world.height").value(48))
                .andExpect(jsonPath("$.civilizationStates").isArray());
    }

    @Test
    void shouldAdvanceSimulationToNextTurn() throws Exception {
        mockMvc.perform(
                        post("/api/simulation")
                                .param("width", "80")
                                .param("height", "48")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.turn").value(0));

        mockMvc.perform(
                        post("/api/simulation/next-turn")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.turn").value(1));
    }

    @Test
    void shouldAdvanceSimulationAcrossMultipleTurns() throws Exception {
        mockMvc.perform(
                        post("/api/simulation")
                                .param("width", "80")
                                .param("height", "48")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.turn").value(0));

        mockMvc.perform(
                        post("/api/simulation/next-turn")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.turn").value(1));

        mockMvc.perform(
                        post("/api/simulation/next-turn")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.turn").value(2));

        mockMvc.perform(
                        post("/api/simulation/next-turn")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.turn").value(3));
    }

    @Test
    void shouldReturnCivilizationStatesAfterAdvancingTurn() throws Exception {
        mockMvc.perform(
                        post("/api/simulation")
                                .param("width", "80")
                                .param("height", "48")
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        post("/api/simulation/next-turn")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.turn").value(1))
                .andExpect(jsonPath("$.civilizationStates").isArray())
                .andExpect(
                        jsonPath("$.civilizationStates[0].civilization")
                                .exists()
                )
                .andExpect(
                        jsonPath("$.civilizationStates[0].stock")
                                .exists()
                );
    }
}