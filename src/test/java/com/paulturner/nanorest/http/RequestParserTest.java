package com.paulturner.nanorest.http;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class RequestParserTest {

    @Test
    public void shouldParseSimpleGetRequest() throws Exception {

        byte[] get = "GET /hello.html HTTP/1.1".getBytes(StandardCharsets.US_ASCII);

        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put(get);
        buf.rewind();
        RequestParser requestParser = new RequestParser(buf);

        requestParser.parse();

    }

}