package com.edumate.eduserver.studentrecord.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edumate.eduserver.studentrecord.controller.request.StudentRecordCreateRequest;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.facade.StudentRecordFacade;
import com.edumate.eduserver.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(StudentRecordController.class)
class StudentRecordControllerTest extends ControllerTest {

    @MockitoBean
    private StudentRecordFacade studentRecordFacade;

    @Test
    @DisplayName("학생 기록을 올바르게 업데이트한다")
    void updateStudentRecord_Success() throws Exception {
        // given
        StudentRecordType recordType = StudentRecordType.ABILITY_DETAIL;
        long recordId = 1L;
        StudentRecordCreateRequest request = new StudentRecordCreateRequest("2023-1", "학생의 능력 상세 기록", 1024);

        doNothing().when(studentRecordFacade)
                .updateStudentRecord(anyLong(), eq(recordType), eq(recordId), any(StudentRecordCreateRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                        post("/api/v1/student-records/{recordType}/{recordId}", recordType, recordId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(request)))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").exists());

        verify(studentRecordFacade).updateStudentRecord(anyLong(), eq(recordType), eq(recordId),
                any(StudentRecordCreateRequest.class));
    }

    @Test
    @DisplayName("잘못된 byteCount 값으로 학생 기록 업데이트에 실패한다")
    void updateStudentRecord_WithNegativeByteCount_BadRequest() throws Exception {
        // given
        final StudentRecordType recordType = StudentRecordType.ABILITY_DETAIL;
        final long recordId = 1L;
        final StudentRecordCreateRequest request = new StudentRecordCreateRequest("2023-1", "학생의 능력 상세 기록", -1);

        // when
        final ResultActions resultActions = mockMvc.perform(
                        post("/api/v1/student-records/{recordType}/{recordId}", recordType, recordId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(request)))
                .andDo(print());

        // then
        resultActions
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").exists());
    }
}
