package com.example.task3;

import lombok.Builder;
import lombok.ToString;

@Builder @ToString
public class CompanyData {
    private String domain;
    private String logo;
    private String address;
}
