package com.paulturner.nanorest.http;

public enum HttpRequestReadProgress {
    RECEIVE, STATUSLINE_READ, HEADERS_READ, COMPLETE
}
