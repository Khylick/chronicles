package fr.khylick.chronicles.world.infrastructure;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class WorldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGenerateWorldWithRequestedDimensions() throws Exception {
        mockMvc.perform(
            get("/api/world")
                .param("width", "3")
                .param("height", "2")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.width").value(3))
        .andExpect(jsonPath("$.height").value(2))
        .andExpect(jsonPath("$.tiles", hasSize(6)))
        .andExpect(jsonPath("$.tiles[0].position.x").value(0))
        .andExpect(jsonPath("$.tiles[0].position.y").value(0))
        .andExpect(jsonPath("$.tiles[0].terrainType").isString());
    }

    @Test
    void shouldUseDefaultDimensions() throws Exception {
        mockMvc.perform(get("/api/world"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.width").value(50))
        .andExpect(jsonPath("$.height").value(30))
        .andExpect(jsonPath("$.tiles", hasSize(1500)));
    }

    @Test
    void shouldRejectWidthAboveMaximum() throws Exception {
        mockMvc.perform(
            get("/api/world")
                .param("width", "201")
                .param("height", "30")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.message")
        .value("La largeur maximale autorisée est de 200"));
    }

    @Test
    void shouldRejectNegativeWidth() throws Exception {
        mockMvc.perform(
            get("/api/world")
                .param("width", "-1")
                .param("height", "30")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(
            "La largeur du monde doit être strictement positive"
        ));
    }
}