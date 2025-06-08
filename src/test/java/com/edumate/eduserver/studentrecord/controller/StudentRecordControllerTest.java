package com.edumate.eduserver.studentrecord.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edumate.eduserver.docs.CustomRestDocsUtils;
import com.edumate.eduserver.studentrecord.controller.request.StudentRecordCreateRequest;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.exception.InvalidSemesterFormatException;
import com.edumate.eduserver.studentrecord.exception.MemberStudentRecordNotFoundException;
import com.edumate.eduserver.studentrecord.exception.StudentRecordDetailNotFoundException;
import com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode;
import com.edumate.eduserver.studentrecord.facade.StudentRecordFacade;
import com.edumate.eduserver.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(StudentRecordController.class)
class StudentRecordControllerTest extends ControllerTest {

    @MockitoBean
    private StudentRecordFacade studentRecordFacade;

    private final String BASE_URL = "/api/v1/student-records";
    private final String BASE_DOMAIN_PACKAGE = "student-record/";
    private static final String RECORD_TYPE = StudentRecordType.BEHAVIOR_OPINION.getValue().toLowerCase();

    @Test
    @DisplayName("학생 기록 업데이트 성공 케이스")
    void updateStudentRecord_Success() throws Exception {
        // given
        StudentRecordCreateRequest request = new StudentRecordCreateRequest("2023-1", "학생의 행동 특성에 대한 기록", 100);
        long recordId = 1L;

        doNothing().when(studentRecordFacade).updateStudentRecord(anyLong(), any(StudentRecordType.class), anyLong(), any(StudentRecordCreateRequest.class));

        // when & then
        mockMvc.perform(post(BASE_URL + "/{recordType}/{recordId}", RECORD_TYPE, recordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-success",
                        pathParameters(
                                parameterWithName("recordType").description("기록 타입"),
                                parameterWithName("recordId").description("학생의 생기부 레코드 ID(ability, behavior, creative-career, creative-autonomy, creative-club)")
                        ),
                        requestFields(
                                fieldWithPath("semester").description("학기 정보 (예: '2023-1')"),
                                fieldWithPath("description").description("기록 내용"),
                                fieldWithPath("byteCount").description("기록 데이터 크기")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("학생 기록 업데이트 실패 - 유효하지 않은 학기 정보")
    void updateStudentRecord_InvalidSemester() throws Exception {
        // given
        String semesterInput = "invalid-semester";
        StudentRecordCreateRequest request = new StudentRecordCreateRequest(semesterInput, "학생의 행동 특성에 대한 기록", 100);
        long recordId = 1L;

        doThrow(new InvalidSemesterFormatException(StudentRecordErrorCode.INVALID_SEMESTER_FORMAT, semesterInput))
                .when(studentRecordFacade)
                .updateStudentRecord(anyLong(), any(StudentRecordType.class), anyLong(), any(StudentRecordCreateRequest.class));

        // when & then
        mockMvc.perform(post(BASE_URL + "/{recordType}/{recordId}", RECORD_TYPE, recordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("EDMT-4000202"))
                .andExpect(jsonPath("$.message").value(String.format("입력하신 %s는 유효하지 않은 학기 형식입니다. 올바른 형식은 'YYYY-1' 또는 'YYYY-2'입니다.", semesterInput)))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-fail/invalid-semester",
                        pathParameters(
                                parameterWithName("recordType").description("기록 타입"),
                                parameterWithName("recordId").description("학생의 생기부 레코드 ID")
                        ),
                        requestFields(
                                fieldWithPath("semester").description("학기 정보 (예: '2023-1')"),
                                fieldWithPath("description").description("기록 내용"),
                                fieldWithPath("byteCount").description("기록 데이터 크기")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("학생 기록 업데이트 실패 - 기록을 찾을 수 없음")
    void updateStudentRecord_RecordNotFound() throws Exception {
        // given
        StudentRecordCreateRequest request = new StudentRecordCreateRequest("2023-1", "학생의 행동 특성에 대한 기록", 100);
        long recordId = 999L;

        doThrow(new StudentRecordDetailNotFoundException(StudentRecordErrorCode.STUDENT_RECORD_DETAIL_NOT_FOUND))
                .when(studentRecordFacade)
                .updateStudentRecord(anyLong(), any(StudentRecordType.class), anyLong(), any(StudentRecordCreateRequest.class));

        // when & then
        mockMvc.perform(post(BASE_URL + "/{recordType}/{recordId}", RECORD_TYPE, recordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("EDMT-4040204"))
                .andExpect(jsonPath("$.message").value("해당 학생에 대한 생활기록부 기록이 존재하지 않습니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-fail/record-not-found",
                        pathParameters(
                                parameterWithName("recordType").description("기록 타입"),
                                parameterWithName("recordId").description("학생의 생기부 레코드 ID")
                        ),
                        requestFields(
                                fieldWithPath("semester").description("학기 정보 (예: '2023-1')"),
                                fieldWithPath("description").description("기록 내용"),
                                fieldWithPath("byteCount").description("기록 데이터 크기")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("학생 기록 업데이트 실패 - 회원의 해당 학기 생기부를 찾을 수 없음")
    void updateStudentRecord_MemberRecordNotFound() throws Exception {
        // given
        StudentRecordCreateRequest request = new StudentRecordCreateRequest("2023-1", "학생의 행동 특성에 대한 기록", 100);
        long recordId = 1L;

        doThrow(new MemberStudentRecordNotFoundException(StudentRecordErrorCode.MEMBER_STUDENT_RECORD_NOT_FOUND))
                .when(studentRecordFacade)
                .updateStudentRecord(anyLong(), any(StudentRecordType.class), anyLong(), any(StudentRecordCreateRequest.class));

        // when & then
        mockMvc.perform(post(BASE_URL + "/{recordType}/{recordId}", RECORD_TYPE, recordId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("EDMT-4040203"))
                .andExpect(jsonPath("$.message").value("해당 회원의 해당 학기 생활기록부가 존재하지 않습니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-fail/member-record-not-found",
                        pathParameters(
                                parameterWithName("recordType").description("기록 타입"),
                                parameterWithName("recordId").description("학생의 생기부 레코드 ID")
                        ),
                        requestFields(
                                fieldWithPath("semester").description("학기 정보 (예: '2023-1')"),
                                fieldWithPath("description").description("기록 내용"),
                                fieldWithPath("byteCount").description("기록 데이터 크기")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("학생 기록 업데이트 실패 - 유효하지 않은 RecordType")
    void updateStudentRecord_InvalidRecordType() throws Exception {
        // given
        StudentRecordCreateRequest request = new StudentRecordCreateRequest("2023-1", "학생의 행동 특성에 대한 기록", 100);
        String recordType = "invalid_type";
        long recordId = 1L;

        // when & then
        mockMvc.perform(post(BASE_URL + "/{recordType}/{recordId}", recordType, recordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("EDMT-4000201"))
                .andExpect(jsonPath("$.message").value(String.format("입력하신 %s는 유효하지 않는 생활기록부 항목입니다.", recordType)))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-fail/invalid-record-type",
                        pathParameters(
                                parameterWithName("recordType").description("기록 타입"),
                                parameterWithName("recordId").description("학생의 생기부 레코드 ID")
                        ),
                        requestFields(
                                fieldWithPath("semester").description("학기 정보 (예: '2023-1')"),
                                fieldWithPath("description").description("기록 내용"),
                                fieldWithPath("byteCount").description("기록 데이터 크기")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("학생 기록 업데이트 실패 - semester 필드 누락")
    void updateStudentRecord_MissingSemester() throws Exception {
        // given
        StudentRecordCreateRequest request = new StudentRecordCreateRequest(null, "학생의 행동 특성에 대한 기록", 100);
        long recordId = 1L;

        // when & then
        mockMvc.perform(post(BASE_URL + "/{recordType}/{recordId}", RECORD_TYPE, recordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("EDMT-40000"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-fail/missing-semester",
                        pathParameters(
                                parameterWithName("recordType").description("기록 타입"),
                                parameterWithName("recordId").description("학생의 생기부 레코드 ID")
                        ),
                        requestFields(
                                fieldWithPath("semester").description("학기 정보 (필수)"),
                                fieldWithPath("description").description("기록 내용"),
                                fieldWithPath("byteCount").description("기록 데이터 크기")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("학생 기록 업데이트 실패 - description 필드 누락")
    void updateStudentRecord_MissingDescription() throws Exception {
        // given
        StudentRecordCreateRequest request = new StudentRecordCreateRequest("2023-1", null, 100);
        long recordId = 1L;

        // when & then
        mockMvc.perform(post(BASE_URL + "/{recordType}/{recordId}", RECORD_TYPE, recordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("EDMT-40000"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-fail/missing-description",
                        pathParameters(
                                parameterWithName("recordType").description("기록 타입"),
                                parameterWithName("recordId").description("학생의 생기부 레코드 ID")
                        ),
                        requestFields(
                                fieldWithPath("semester").description("학기 정보 (예: '2023-1')"),
                                fieldWithPath("description").description("기록 내용 (필수)"),
                                fieldWithPath("byteCount").description("기록 데이터 크기")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("학생 기록 업데이트 실패 - 음수 byteCount")
    void updateStudentRecord_NegativeByteCount() throws Exception {
        // given
        StudentRecordCreateRequest request = new StudentRecordCreateRequest("2023-1", "학생의 행동 특성에 대한 기록", -1);
        long recordId = 1L;

        // when & then
        mockMvc.perform(post(BASE_URL + "/{recordType}/{recordId}", RECORD_TYPE, recordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("EDMT-40000"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-fail/negative-byte-count",
                        pathParameters(
                                parameterWithName("recordType").description("기록 타입"),
                                parameterWithName("recordId").description("학생의 생기부 레코드 ID")
                        ),
                        requestFields(
                                fieldWithPath("semester").description("학기 정보 (예: '2023-1')"),
                                fieldWithPath("description").description("기록 내용"),
                                fieldWithPath("byteCount").description("기록 데이터 크기 (0 이상이어야 함)")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }
}
