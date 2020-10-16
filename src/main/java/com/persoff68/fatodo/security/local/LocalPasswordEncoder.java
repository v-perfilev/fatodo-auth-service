package com.persoff68.fatodo.security.local;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class LocalPasswordEncoder extends BCryptPasswordEncoder {

    public LocalPasswordEncoder() {
        super(10);
    }
}
