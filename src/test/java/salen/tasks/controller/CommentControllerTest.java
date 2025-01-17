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
 * Test class for the {@link CommentController}
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    public String getTokenAdmin() throws Exception {
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

    public String getTokenUser() throws Exception {
        String username = "ame@mail.com";
        String password = "a@1234";
        String body = "{\"email\":\"" + username + "\", \"password\":\"" + password + "\"}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
                .contentType("application/json")
                .content(body)).andReturn();
        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"access_token\": \"", "");

        return response.replace("\"}", "");
    }

    @Test
    public void getCommentTaskOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks/1/comments/2")
                        .header("Authorization", "Bearer " + getTokenAdmin()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getAllCommentOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks/1/comments")
                        .header("Authorization", "Bearer " + getTokenAdmin()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void saveCommentTaskNotFoundTest() throws Exception {
        String dto = """
                {
                    "value": "test"
                }""";

        mockMvc.perform(post("/api/v1/tasks/100/comments")
                        .header("Authorization", "Bearer " + getTokenAdmin())
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void saveCommentOk() throws Exception {
        String dto = """
                {
                    "value": "test"
                }""";

        mockMvc.perform(post("/api/v1/tasks/1/comments")
                        .header("Authorization", "Bearer " + getTokenAdmin())
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void updateCommentAccessDenied() throws Exception {
        String dto = """
                {
                    "value": "test"
                }""";

        mockMvc.perform(put("/api/v1/tasks/1/comments")
                        .header("Authorization", "Bearer " + getTokenUser())
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    public void deleteNoContentTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tasks/1/comments/1")
                        .header("Authorization", "Bearer " + getTokenAdmin()))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void updateTaskNotFoundTest() throws Exception {
        String dto = """
                {
                    "id": 1,
                    "value": "test update"
                }""";

        mockMvc.perform(put("/api/v1/tasks/100/comments/1")
                        .header("Authorization", "Bearer " + getTokenAdmin())
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
