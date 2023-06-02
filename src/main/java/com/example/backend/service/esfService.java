package com.example.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class esfService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public esfService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> executeQueryWithOptions(String filter, String search, String startDate, String endDate, String[] groupField) {
        String query = "SELECT ";
        String queryEnd = " sum(TURNOVER_SIZE) as Total, sum(QUANTITY) as QUANTITY " +
                "FROM esf.esf_new " +
                "WHERE IIN_CUSTOMER IN ('130440030933', '130440030933', '130940009448', '140640015225') " +
                "AND toDate(TURNOVER_DATE) >= toDate('2020-01-01') " +
                "AND toDate(TURNOVER_DATE) <= toDate('2023-01-01') " +
                "GROUP BY NAMES_SELLER, NAME_CUSTOMER " +
                "LIMIT 10000";
        for (String field : groupField) {
            query = query + field + ", ";
        }
        String[] iins = search.split(",");
        query = query + "sum(TURNOVER_SIZE) as Total, sum(QUANTITY) as QUANTITY FROM esf.esf_new ";
        if (filter == "seller") {
            query = query + "WHERE IIN_SELLER IN (";
            int l = iins.length;
            for (int i = 0; i < l; i++) {
                if (i != l-1) {
                    query = query + "\'" + iins[i] + "\',";
                } else {
                    query = query + "\'" + iins[i] + "\') ";
                }
            }
        } else {
            query = query + "WHERE IIN_CUSTOMER IN (";
            int l = iins.length;
            for (int i = 0; i < l; i++) {
                if (i != l-1) {
                    query = query + "\'" + iins[i] + "\',";
                } else {
                    query = query + "\'" + iins[i] + "\') ";
                }
            }
        }
        query = query +" AND toDate(TURNOVER_DATE) >= toDate(\'" + startDate + "\') " +
        "AND toDate(TURNOVER_DATE) <= toDate(\'" + endDate + "\') GROUP BY ";
        for (String field : groupField) {
            query = query + field + ", ";
        }
        query = query + "NAMES_SELLER, NAME_CUSTOMER LIMIT 1000000";

        return jdbcTemplate.queryForList(query);
    }
}
