package org.sirma.diankarailievemployees.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class ErrorController {

    @RequestMapping("/errorPage")
    public String handleError() {
        log.error("An error occurred. Redirecting to error page.");
        return "errorPage";
    }

}
