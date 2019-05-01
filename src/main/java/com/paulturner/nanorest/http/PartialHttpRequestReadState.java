package com.paulturner.nanorest.http;

public enum PartialHttpRequestReadState {

    RECEIVE, STATUSLINE_READ, HEADERS_READ, COMPLETE;
}
