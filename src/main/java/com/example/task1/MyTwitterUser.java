package com.example.task1;

import java.time.LocalDate;

public class MyTwitterUser implements User {
    private TwitterUser twitterUser;

    public MyTwitterUser(TwitterUser twitterUser) {
        this.twitterUser = twitterUser;
    }

    @Override
    public String getEmail() {
        return twitterUser.getGetUserMail();
    }

    @Override
    public String getCountry() {
        return twitterUser.getGetCountry();
    }

    @Override
    public LocalDate getLastActiveDate() {
       return twitterUser.getGetLastActiveTime().toLocalDate();
    }
    
}
