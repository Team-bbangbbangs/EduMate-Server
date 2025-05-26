package com.edumate.eduserver.docs;

import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.snippet.Snippet;

public class CustomRestDocsUtils {

    public static RestDocumentationResultHandler document(final String identifier, final Snippet... snippets) {
        return MockMvcRestDocumentation.document(identifier,
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                snippets
        );
    }
}
