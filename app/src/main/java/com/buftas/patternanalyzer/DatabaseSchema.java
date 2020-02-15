//Nikolaos Katsiopis
//icsd13076

package com.buftas.patternanalyzer;

//This class constructs the Database's schema
public final class DatabaseSchema {

    private DatabaseSchema(){ }

    public static class UsernameEntry {
        public static final String TABLE_NAME = "usernames";
        public static final String USERNAME_ID = "username_id";
        public static final String USERNAME = "username";
        public static final String COLOR_PATTERN_SUBMISSION = "color_pattern_submission";
        public static final String PIANO_PATTERN_SUBMISSION = "piano_pattern_submission";
    }
}
