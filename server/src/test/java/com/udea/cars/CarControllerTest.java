package com.udea.cars;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CarControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void coolCarsExcludesUncoolOnes() throws Exception {
        mvc.perform(get("/cool-cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", hasItem("Ferrari")))
                .andExpect(jsonPath("$[*].name", not(hasItem("Yugo GV"))));
    }

    @Test
    void createAndUpdateCar() throws Exception {
        String body = """
                {"name":"Tesla","model":"Model 3","color":"Negro","year":2024,"price":45000}
                """;
        String location = mvc.perform(post("/cars").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.model").value("Model 3"))
                .andReturn().getResponse().getContentAsString();
        long id = Long.parseLong(location.replaceAll(".*\"id\":(\\d+).*", "$1"));

        String update = """
                {"name":"Tesla","model":"Model Y","color":"Blanco","year":2025,"price":50000}
                """;
        mvc.perform(put("/cars/" + id).contentType(MediaType.APPLICATION_JSON).content(update))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.color").value("Blanco"));
    }

    @Test
    void updateMissingCarReturns404() throws Exception {
        mvc.perform(put("/cars/9999").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"X\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void invalidCarIsRejected() throws Exception {
        mvc.perform(post("/cars").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}
