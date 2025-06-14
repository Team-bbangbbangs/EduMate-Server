package com.edumate.eduserver.studentrecord.controller;

import static com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode.INVALID_SEMESTER_FORMAT;
import static com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode.MEMBER_STUDENT_RECORD_NOT_FOUND;
import static com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode.STUDENT_RECORD_DETAIL_NOT_FOUND;
import static com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode.UPDATE_PERMISSION_DENIED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edumate.eduserver.docs.CustomRestDocsUtils;
import com.edumate.eduserver.studentrecord.controller.request.StudentRecordCreateRequest;
import com.edumate.eduserver.studentrecord.controller.request.StudentRecordUpdateRequest;
import com.edumate.eduserver.studentrecord.controller.request.StudentRecordsCreateRequest;
import com.edumate.eduserver.studentrecord.controller.request.vo.StudentRecordCreateInfo;
import com.edumate.eduserver.studentrecord.controller.request.vo.StudentRecordInfo;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.exception.InvalidSemesterFormatException;
import com.edumate.eduserver.studentrecord.exception.MemberStudentRecordNotFoundException;
import com.edumate.eduserver.studentrecord.exception.StudentRecordDetailNotFoundException;
import com.edumate.eduserver.studentrecord.exception.UpdatePermissionDeniedException;
import com.edumate.eduserver.studentrecord.facade.StudentRecordFacade;
import com.edumate.eduserver.studentrecord.facade.response.StudentNamesResponse;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordDetailResponse;
import com.edumate.eduserver.studentrecord.facade.response.vo.StudentDetail;
import com.edumate.eduserver.util.ControllerTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@DisplayName("학생 생활기록부 컨트롤러 테스트")
@WebMvcTest(StudentRecordController.class)
class StudentRecordControllerTest extends ControllerTest {

    @MockitoBean
    private StudentRecordFacade studentRecordFacade;

    private final String BASE_URL = "/api/v1/student-records";
    private final String BASE_DOMAIN_PACKAGE = "student-record/";
    private static final String ACCESS_TOKEN = "access-token";
    private static final String RECORD_TYPE = StudentRecordType.BEHAVIOR_OPINION.getValue().toLowerCase();

    @Test
    @DisplayName("학생 생활기록부 내용을 성공적으로 업데이트 한다.")
    void updateStudentRecord_Success() throws Exception {
        // given
        StudentRecordUpdateRequest request = new StudentRecordUpdateRequest("학생의 행동 특성에 대한 기록", 100);
        long recordId = 1L;

        doNothing().when(studentRecordFacade)
                .updateStudentRecord(anyString(), anyLong(), anyString(), anyInt());

        // when & then
        mockMvc.perform(post(BASE_URL + "/detail/{recordId}", recordId)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-success",
                        pathParameters(
                                parameterWithName("recordId").description("학생의 생기부 레코드 ID")
                        ),
                        requestFields(
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
    @DisplayName("학생 생활기록부 내용을 성공적으로 불러온다.")
    void getStudentRecord_Success() throws Exception {
        // given
        long recordId = 1L;

        StudentRecordDetailResponse dummyRecordResponse = new StudentRecordDetailResponse("이 학생은 바르고 성실한 학생입니다.", 15);
        when(studentRecordFacade.getStudentRecord(anyString(), anyLong()))
                .thenReturn(dummyRecordResponse);

        // when & then
        mockMvc.perform(get(BASE_URL + "/detail/{recordId}", recordId)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "get-success",
                        pathParameters(
                                parameterWithName("recordId").description("학생 레코드 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.description").description("학생 기록 내용"),
                                fieldWithPath("data.byteCount").description("학생 기록 데이터 바이트 수")
                        )
                ));
    }

    @Test
    @DisplayName("특정 학기 특정 생활기록부 항목에 작성된 학생 정�� 목록을 성공적으로 불러온다.")
    void getStudentName() throws Exception {
        // given
        StudentDetail detail1 = new StudentDetail(1L, "김가연");
        StudentDetail detail2 = new StudentDetail(2L, "이승섭");
        StudentNamesResponse dummyResponse = new StudentNamesResponse(List.of(detail1, detail2));

        when(studentRecordFacade.getStudentDetails(anyString(), any(StudentRecordType.class), any(String.class)))
                .thenReturn(dummyResponse);
        String recordType = StudentRecordType.ABILITY_DETAIL.getValue().toLowerCase();

        // when & then
        mockMvc.perform(get(BASE_URL + "/{recordType}/students", recordType)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .param("semester", "2025-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "get-names-success",
                        pathParameters(
                                parameterWithName("recordType").description("생활기록부 항목 타입")
                        ),
                        queryParameters(
                                parameterWithName("semester").description("학기 (YYYY-1 또는 YYYY-2 형식)")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.studentDetails[].recordId").description("레코드 ID"),
                                fieldWithPath("data.studentDetails[].studentName").description("학생 이름")
                        )
                ));
    }

    @Test
    @DisplayName("여러개의 생기부 목록을 성공적으로 저장한다.")
    void insertStudentRecords() throws Exception {
        // given
        StudentRecordsCreateRequest request = new StudentRecordsCreateRequest("2025-1",
                List.of(new StudentRecordInfo("2020123", "김가연"), new StudentRecordInfo("2931232", "김지안"))
        );
        doNothing().when(studentRecordFacade)
                .createStudentRecords(anyString(), any(StudentRecordType.class), anyString(), anyList());

        // when & then
        mockMvc.perform(post(BASE_URL + "/{recordType}/students/batch", RECORD_TYPE)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.code").value("EDMT-201"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "create-success",
                        pathParameters(
                                parameterWithName("recordType").description("생활기록부 항목 타입")
                        ),
                        requestFields(
                                fieldWithPath("semester").description("학기 정보"),
                                fieldWithPath("studentRecords[].studentNumber").description("학번"),
                                fieldWithPath("studentRecords[].studentName").description("학생 이름")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }


    @Test
    @DisplayName("존재하지 않은 학생 레코드 ID로 요청을 보내면 실패한다.")
    void updateStudentRecord_RecordNotFound() throws Exception {
        // given
        StudentRecordUpdateRequest request = new StudentRecordUpdateRequest("2023-1", 100);
        long recordId = 999L;

        doThrow(new StudentRecordDetailNotFoundException(STUDENT_RECORD_DETAIL_NOT_FOUND))
                .when(studentRecordFacade)
                .updateStudentRecord(anyString(), anyLong(), anyString(), anyInt());

        // when & then
        mockMvc.perform(post(BASE_URL + "/detail/{recordId}", recordId)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value(STUDENT_RECORD_DETAIL_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(
                        STUDENT_RECORD_DETAIL_NOT_FOUND.getMessage()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "fail/record-not-found",
                        pathParameters(
                                parameterWithName("recordId").description("학생의 생기부 레코드 ID")
                        ),
                        requestFields(
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
    @DisplayName("존재하지 않은 학생 생활기록부 관련 요청을 보내면 실패한다.")
    void updateStudentRecord_MemberRecordNotFound() throws Exception {
        // given
        StudentRecordUpdateRequest request = new StudentRecordUpdateRequest("학생의 행동 특성에 대한 기록", 100);
        long recordId = 1L;

        doThrow(new MemberStudentRecordNotFoundException(MEMBER_STUDENT_RECORD_NOT_FOUND))
                .when(studentRecordFacade)
                .updateStudentRecord(anyString(), anyLong(), anyString(), anyInt());

        // when & then
        mockMvc.perform(post(BASE_URL + "/detail/{recordId}", recordId)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value(MEMBER_STUDENT_RECORD_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(
                        MEMBER_STUDENT_RECORD_NOT_FOUND.getMessage()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "fail/member-record-not-found",
                        pathParameters(
                                parameterWithName("recordId").description("학생의 생기부 레코드 ID")
                        ),
                        requestFields(
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
    @DisplayName("description 필드 누락하여 요청을 보내면 실패한다.")
    void updateStudentRecord_MissingDescription() throws Exception {
        // given
        StudentRecordUpdateRequest request = new StudentRecordUpdateRequest(null, 100);
        long recordId = 1L;

        // when & then
        mockMvc.perform(post(BASE_URL + "/detail/{recordId}", recordId)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("EDMT-4002"))
                .andExpect(jsonPath("$.message").value("null 값은 허용되지 않습니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-fail/missing-description",
                        pathParameters(
                                parameterWithName("recordId").description("학생의 생기부 레코드 ID")
                        ),
                        requestFields(
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
    @DisplayName("음수 byteCount를 보내면 요청에 실패한다.")
    void updateStudentRecord_NegativeByteCount() throws Exception {
        // given
        StudentRecordUpdateRequest request = new StudentRecordUpdateRequest("학생의 행동 특성에 대한 기록", -1);
        long recordId = 1L;

        // when & then
        mockMvc.perform(post(BASE_URL + "/detail/{recordId}", recordId)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("EDMT-4002"))
                .andExpect(jsonPath("$.message").value("바이트 수는 0 이상이어야 합니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-fail/negative-byte-count",
                        pathParameters(
                                parameterWithName("recordId").description("학생의 생기부 레코드 ID")
                        ),
                        requestFields(
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

    @Test
    @DisplayName("적절하지 않은 Semester 형식으로 요청을 보내면 실패한다.")
    void invalidSemesterFormat() throws Exception {
        // given
        String invalidSemester = "2025-3";

        doThrow(new InvalidSemesterFormatException(INVALID_SEMESTER_FORMAT, invalidSemester))
                .when(studentRecordFacade)
                .getStudentDetails(anyString(), any(StudentRecordType.class), any(String.class));

        // when & then
        mockMvc.perform(get(BASE_URL + "/{recordType}/students", RECORD_TYPE)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .param("semester", invalidSemester))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("EDMT-4000202"))
                .andExpect(jsonPath("$.message").value(
                        String.format("입력하신 %s는 유효하지 않은 학기 형식입니다. 올바른 형식은 'YYYY-1' 또는 'YYYY-2'입니다.", invalidSemester)))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "get-names-fail/invalid-semester-format",
                        pathParameters(
                                parameterWithName("recordType").description("생활기록부 항목 타입")
                        ),
                        queryParameters(
                                parameterWithName("semester").description("학기 (YYYY-1 또는 YYYY-2 형식)")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("권한이 없는 사용자가 생기부 수정 시 UpdatePermissionDeniedException이 발생한다.")
    void updateStudentRecord_PermissionDenied() throws Exception {
        // given
        StudentRecordUpdateRequest request = new StudentRecordUpdateRequest("허용되지 않은 수정", 100);
        long recordId = 99L;
        doThrow(new UpdatePermissionDeniedException(
                UPDATE_PERMISSION_DENIED))
                .when(studentRecordFacade)
                .updateStudentRecord(anyString(), anyLong(), anyString(), anyInt());

        // when & then
        mockMvc.perform(post(BASE_URL + "/detail/{recordId}", recordId)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.code").value(UPDATE_PERMISSION_DENIED.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_PERMISSION_DENIED.getMessage()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-fail/permission-denied",
                        pathParameters(
                                parameterWithName("recordId").description("학생의 생기부 레코드 ID")
                        ),
                        requestFields(
                                fieldWithPath("description").description("기록 내용"),
                                fieldWithPath("byteCount").description("기록 데이터 크기")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("생기부 한 개를 성공적으로 저장한다.")
    void insertStudentRecord() throws Exception {
        // given
        StudentRecordCreateRequest request = new StudentRecordCreateRequest(
                "2025-1", new StudentRecordCreateInfo("2020123", "유태근", "프로그래밍 동아리에서 웹 프로젝트를 진행하며 백엔드 개발을 맡아 로그인 기능과 데이터 저장 기능을 구현함.", 149)
        );
        doNothing().when(studentRecordFacade)
                .createStudentRecord(anyString(), any(StudentRecordType.class), anyString(), any());

        // when & then
        mockMvc.perform(post(BASE_URL + "/{recordType}/students", RECORD_TYPE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.code").value("EDMT-201"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "create-one-success",
                        pathParameters(
                                parameterWithName("recordType").description("생활기록부 항목 타입")
                        ),
                        requestFields(
                                fieldWithPath("semester").description("학기 정보"),
                                fieldWithPath("studentRecord").description("생활기록부 정보 객체"),
                                fieldWithPath("studentRecord.studentNumber").description("학번"),
                                fieldWithPath("studentRecord.studentName").description("학생 이름"),
                                fieldWithPath("studentRecord.description").description("기록 내용"),
                                fieldWithPath("studentRecord.byteCount").description("기록 데이터 크기")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }
}
