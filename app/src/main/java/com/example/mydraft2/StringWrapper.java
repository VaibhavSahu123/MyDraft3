package com.example.mydraft2;

/**
 * Created by dikki on 03/09/2017.
 * Wrapper class to pass string to functions
 */

public class StringWrapper {
        private String string;
        public StringWrapper()
        {
            string = new String();
        }
        public StringWrapper(String s) {
            this.string = s;
        }
        public String getString() {
            return this.string;
        }
        public void setString(String s) {
            this.string = s;
        }
    }
