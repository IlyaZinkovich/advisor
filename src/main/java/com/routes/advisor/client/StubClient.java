package com.routes.advisor.client;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.EMPTY_LIST;

@Service
public class StubClient {

    public List<String> serve() {
        return EMPTY_LIST;
    }
}
