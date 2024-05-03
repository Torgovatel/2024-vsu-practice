package ru.management.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import ru.management.api.dto.ErrorDTO;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class CustomErrorController implements ErrorController {
    private static final String PATH = "/error";
    private final ErrorAttributes errorAttributes; // Добавлено final

    @RequestMapping(PATH)
    public ResponseEntity<ErrorDTO> error(WebRequest webRequest) {
        Map<String, Object> attributes = errorAttributes.getErrorAttributes(
                webRequest,
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.EXCEPTION, ErrorAttributeOptions.Include.MESSAGE)
        );
        HttpStatus httpStatus = HttpStatus.valueOf((Integer) attributes.get("status"));

        ErrorDTO errorDTO = ErrorDTO.builder()
                .type("about:blank")
                .title(httpStatus.getReasonPhrase())
                .status((Integer) attributes.get("status"))
                .detail((String) attributes.get("message"))
                .instance((String) attributes.get("path"))
                .build();

        return ResponseEntity
                .status(httpStatus)
                .body(errorDTO);
    }
}