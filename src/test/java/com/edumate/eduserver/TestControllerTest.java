package com.edumate.eduserver;

import static com.edumate.eduserver.docs.CustomRestDocsUtils.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.edumate.eduserver.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class TestControllerTest extends RestDocsSupport {

    @Override
    protected Object initController() {
        return new TestController();
    }

    @Test
    @DisplayName("GET / 테스트")
    void getTest() throws Exception {
        mockMvc.perform(get("/")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("test-get",
                responseFields(
                    fieldWithPath("testStr").description("테스트 문자열"),
                    fieldWithPath("testInt").description("테스트 정수")
                )
            ));
    }

    @Test
    @DisplayName("POST / 테스트")
    void postTest() throws Exception {
        String requestBody = """
            {
                "testStr": "hello",
                "testInt": 123
            }
            """;

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("test-post",
                requestFields(
                    fieldWithPath("testStr").description("테스트 문자열"),
                    fieldWithPath("testInt").description("테스트 정수")
                ),
                responseFields(
                    fieldWithPath("testStr").description("테스트 문자열"),
                    fieldWithPath("testInt").description("테스트 정수")
                )
            ));
    }
}
