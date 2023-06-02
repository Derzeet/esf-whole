package com.example.backend.controller;


//import com.lowagie.text.DocumentException;
import com.example.backend.service.esfService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3589)
public class BaseController {
    @Autowired
    esfService esf_Service;
    @GetMapping("/esf")
    public List<Map<String, Object>> getTest(@RequestParam String filter, @RequestParam String search, @RequestParam String startDate, @RequestParam String endDate, @RequestParam String[] groupField) {
        return esf_Service.executeQueryWithOptions(filter, search, startDate, endDate, groupField);
    }
}
