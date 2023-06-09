package com.example.backend.controller;


//import com.lowagie.text.DocumentException;
import com.example.backend.modelsDossier.Logs;
import com.example.backend.repositoryDossier.LogsRepository;
import com.example.backend.security.jwt.JwtTokenUtil;
import com.example.backend.security.services.UserDetailsServiceImpl;
import com.example.backend.service.esfService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class BaseController {
    @Autowired
    esfService esf_Service;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    LogsRepository logsRepository;


    @GetMapping(value = "/download")
    public void getTest(@RequestParam String filter, @RequestParam String search, @RequestParam String startDate,
                        @RequestParam String endDate, @RequestParam String[] groupField, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> data = esf_Service.executeQueryWithOptions(filter, search, startDate, endDate, groupField);
        esf_Service.exportToExcel(data, response);
    }
    @GetMapping("/esf")
    public List<Map<String, Object>> getTest(@RequestParam String filter,
                                             @RequestParam String search,
                                             @RequestParam String startDate,
                                             @RequestParam String endDate,
                                             @RequestParam String[] groupField,
                                             @RequestParam String testReason,
                                             @RequestParam String dopInfo,
                                             @RequestHeader("Authorization") String token) {
        String requestParams = "Запрос: " + filter + ", поиск: " + search + ", дата с: " + startDate + ", дата до: " + endDate;
        String jwtToken = token.substring(7);

        // Get the username from the token
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

        // Load the user details using the username
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Insert the values into the ClickHouse table
        LocalDateTime now = LocalDateTime.now();

// Format the LocalDateTime to match ClickHouse's expected format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        String insertQuery = "INSERT INTO pfr.logs (requestParams, approvalBody, executionTime, userLogin) VALUES (?, ?, ?, ?)";
        String approvalBody = "Основания проверки: " + testReason + ", " + dopInfo;
        jdbcTemplate.update(insertQuery, requestParams, approvalBody, formattedDateTime, userDetails.getUsername());

        return esf_Service.executeQueryWithOptions(filter, search, startDate, endDate, groupField);
    }
}
