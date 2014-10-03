package com.arusland.bozor.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ruslan on 03.10.2014.
 */
@Controller
public class PartialTemplateController {

    @RequestMapping("/partials/{name}")
    public String getPartial(@PathVariable String name)
    {
        // partial template file has prefix
        return  "partials/_" + name;
    }
}
