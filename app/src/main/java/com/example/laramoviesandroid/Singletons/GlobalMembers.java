package com.example.laramoviesandroid.Singletons;

public class GlobalMembers {
    private static GlobalMembers INSTANCE = null;

    public String accessToken;

    public static GlobalMembers getInstance() {
        if(GlobalMembers.INSTANCE == null) {
            INSTANCE = new GlobalMembers();
        }
        return INSTANCE;
    }
}
