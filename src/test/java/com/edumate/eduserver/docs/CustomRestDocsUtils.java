package com.edumate.eduserver.docs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.snippet.Snippet;

public class CustomRestDocsUtils {

    public static RestDocumentationResultHandler Documents(final String identifier,
                                                                final Snippet... snippets) {
        return document("{class-name}/" + identifier, getDocumentRequest(), getDocumentResponse(), snippets);
    }

    private static OperationRequestPreprocessor getDocumentRequest() {
        return Preprocessors.preprocessRequest(prettyPrint());
    }

    private static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(prettyPrint());
    }
}
