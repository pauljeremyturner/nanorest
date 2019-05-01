package com.paulturner.nanorest.http;

import static com.paulturner.nanorest.http.PartialHttpRequestReadState.COMPLETE;
import static com.paulturner.nanorest.http.PartialHttpRequestReadState.HEADERS_READ;
import static com.paulturner.nanorest.http.PartialHttpRequestReadState.RECEIVE;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;


public class HttpRequestParser {

    private static final byte[] TOKEN_CRLF = new byte[]{0xd, 0xa};
    private static final byte[] TOKEN_CRLF_2TIMES = new byte[]{0xd, 0xa, 0xd, 0xa};

    private PartialHttpRequestReadState readProgress = PartialHttpRequestReadState.RECEIVE;
    private byte[] body;
    private HttpMethod httpMethod;
    private String uri;
    private ByteBuffer byteBuffer;
    private HttpHeaders httpHeaders = new HttpHeaders();

    public HttpRequestParser(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public boolean parse() {


        if (readProgress == RECEIVE) {
            parseStatusLine();
        }

        if (readProgress == PartialHttpRequestReadState.STATUSLINE_READ) {
            parseHeaders();
        }

        if (readProgress == HEADERS_READ && httpMethod == HttpMethod.POST) {
            parseBody();
        } else if (httpMethod == HttpMethod.GET) {
            readProgress = COMPLETE;
            if (byteBuffer.hasRemaining()) {
                byteBuffer.clear();
            }
        }

        byteBuffer.limit(byteBuffer.capacity());

        return readProgress == COMPLETE;

    }

    public HttpRequest httpRequest() {
        return HttpRequest
            .builder()
            .withUri(uri)
            .withHttpMethod(httpMethod)
            .withBody(body)
            .withHttpHeaders(httpHeaders)
            .build();
    }

    private void parseStatusLine() {

        Optional<byte[]> statusLineBytes = Parsers.readBufferUntil(TOKEN_CRLF, byteBuffer);
        if (statusLineBytes.isPresent()) {
            readProgress = PartialHttpRequestReadState.STATUSLINE_READ;
            String[] statusLineTokens = new String(statusLineBytes.get(), StandardCharsets.US_ASCII).split(" ");
            httpMethod = HttpMethod.lookup(statusLineTokens[0]).orElse(HttpMethod.INVALID);
            uri = statusLineTokens[1];
            int newLimit = byteBuffer.limit() - byteBuffer.position();
            byteBuffer.compact();
            byteBuffer.limit(newLimit);
            byteBuffer.rewind();
        }

    }

    private void parseHeaders() {

        Optional<byte[]> headerBytes = Parsers.readBufferUntil(TOKEN_CRLF_2TIMES, byteBuffer);
        if (headerBytes.isPresent()) {
            readProgress = HEADERS_READ;
            try {
                Arrays
                    .stream(new String(headerBytes.get(), StandardCharsets.ISO_8859_1).split("\r\n"))
                    .forEach(ht -> {
                        String[] keyValueArray = ht.split(":", 2);

                        System.out.println(Arrays.toString(keyValueArray));
                        httpHeaders.addHeader(keyValueArray[0], keyValueArray[1]);
                    });
            } catch (ArrayIndexOutOfBoundsException e) {
                //malformed headers, we'll be kind
            }
            int newLimit = byteBuffer.remaining();
            byteBuffer.compact();
            byteBuffer.rewind();
            byteBuffer.limit(newLimit);
        }
        if (httpMethod == HttpMethod.GET) {
            readProgress = COMPLETE;
        }
    }


    private void parseBody() {


        final Optional<String> contentLengthHeader = httpHeaders.getHeader("Content-Length");
        boolean contentLengthAvailable = contentLengthHeader.isPresent();
        int contentLength = contentLengthHeader.map(h -> Integer.parseInt(h.trim())).orElse(Integer.MIN_VALUE);

        int remaining = byteBuffer.remaining();
        boolean canReadUsingContentLength = contentLengthAvailable && remaining >= contentLength;
        boolean canReadRemainingBytes = !contentLengthAvailable && remaining > 0;

        if (canReadRemainingBytes || canReadUsingContentLength) {

            int bytesToRead = contentLengthAvailable ? contentLength : byteBuffer.remaining();
            byte[] bodyBytes = new byte[bytesToRead];
            byteBuffer.get(bodyBytes);

            //RFC 2616.  We could look in Content-Type header, but this is the default.
            body = bodyBytes;
            readProgress = COMPLETE;
            byteBuffer.clear();
        }
    }


}
