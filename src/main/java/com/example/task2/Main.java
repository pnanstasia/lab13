package com.example.task2;

public class Main {
    public static void main(String[] args) {
        Database db = new Database();
        Authorization authorization = new Authorization();
        if (authorization.login(db)) {
            ReportBuilder br = new ReportBuilder(db);
            System.out.println(br);
        }
    }
}
