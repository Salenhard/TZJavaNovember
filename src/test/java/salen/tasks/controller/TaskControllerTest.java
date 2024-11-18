package salen.tasks.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link TaskController}
 */
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    public String getToken() throws Exception {
        String username = "email@mail.com";
        String password = "e@1234";
        String body = "{\"email\":\"" + username + "\", \"password\":\"" + password + "\"}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
                .contentType("application/json")
                .content(body)).andReturn();
        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"access_token\": \"", "");

        return response.replace("\"}", "");
    }

    @Test
    public void getTaskIsOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks/1")
                        .header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getAllTasksIsOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks")
                        .header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void saveTaskBadRequestValidationTest() throws Exception {
        String dto = """
                {
                    "title": "",
                    "description": "",
                    "status": "",
                    "priority": "",
                    "authorEmail": "",
                    "executorEmail": "",
                    "executorId": 0
                }""";

        mockMvc.perform(post("/api/v1/tasks")
                        .header("Authorization", "Bearer " + getToken())
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void deleteNoContentTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tasks/{0}", "0")
                        .header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void updateTaskIsNotFoundTest() throws Exception {
        String dto = """
                {
                    "title": "test",
                    "description": "tsettset",
                    "status": "waiting",
                    "priority": "high",
                    "executorId": 1
                }""";

        mockMvc.perform(put("/api/v1/tasks/100")
                        .header("Authorization", "Bearer " + getToken())
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
