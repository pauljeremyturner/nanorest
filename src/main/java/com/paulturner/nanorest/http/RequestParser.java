package com.paulturner.nanorest.http;

import static com.paulturner.nanorest.http.HttpRequestReadProgress.COMPLETE;
import static com.paulturner.nanorest.http.HttpRequestReadProgress.HEADERS_READ;
import static com.paulturner.nanorest.http.HttpRequestReadProgress.RECEIVE;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;


public class RequestParser {

    private static final byte[] TOKEN_CRLF = new byte[]{0xd, 0xa};
    private static final byte[] TOKEN_CRLF_2TIMES = new byte[]{0xd, 0xa, 0xd, 0xa};

    private HttpRequestReadProgress readProgress = HttpRequestReadProgress.RECEIVE;
    private byte[] body;
    private Method method;
    private String uri;
    private ByteBuffer byteBuffer;
    private Headers httpHeaders = new Headers();

    public RequestParser(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public boolean parse() {


        if (readProgress == RECEIVE) {
            parseStatusLine();
        }

        if (readProgress == HttpRequestReadProgress.STATUSLINE_READ) {
            parseHeaders();
        }

        if (readProgress == HEADERS_READ && method == Method.POST) {
            parseBody();
        } else if (method == Method.GET) {
            readProgress = COMPLETE;
            if (byteBuffer.hasRemaining()) {
                byteBuffer.clear();
            }
        }

        byteBuffer.limit(byteBuffer.capacity());

        return readProgress == COMPLETE;

    }

    public Request httpRequest() {
        return Request
            .builder()
            .withUri(uri)
            .withHttpMethod(method)
            .withBody(body)
            .withHttpHeaders(httpHeaders)
            .build();
    }

    private void parseStatusLine() {

        Parsers.readBufferUntil(TOKEN_CRLF, byteBuffer).ifPresent(bytes -> {
                readProgress = HttpRequestReadProgress.STATUSLINE_READ;
                String[] statusLineTokens = new String(bytes, StandardCharsets.US_ASCII).split("\\s+");
                method = Method.lookup(statusLineTokens[0]).orElse(Method.INVALID);
                uri = statusLineTokens[1];
                int newLimit = byteBuffer.limit() - byteBuffer.position();
                byteBuffer.compact();
                byteBuffer.limit(newLimit);
                byteBuffer.rewind();
            }
        );
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
        if (method == Method.GET) {
            readProgress = COMPLETE;
        }
    }

    private void parseBody() {

        final Optional<Integer> contentLength = httpHeaders.getContentLength();

        int remaining = byteBuffer.remaining();
        boolean canReadUsingContentLength = contentLength.map(cl -> remaining >= cl).orElse(false);
        boolean canReadRemainingBytes = contentLength.map(cl -> false).orElse(remaining > 0);

        if (canReadRemainingBytes || canReadUsingContentLength) {

            int bytesToRead = contentLength.orElse(byteBuffer.remaining());
            byte[] bodyBytes = new byte[bytesToRead];
            byteBuffer.get(bodyBytes);

            //RFC 2616.  We could look in Content-Type header, but this is the default.
            body = bodyBytes;
            readProgress = COMPLETE;
            byteBuffer.clear();
        }
    }

}
