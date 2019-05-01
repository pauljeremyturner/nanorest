package com.paulturner.nanorest.http;

import java.nio.ByteBuffer;
import java.util.Optional;

import com.paulturner.nanorest.collections.CircularByteArrayQueue;

public class Parsers {

    public static Optional<byte[]> readBufferUntil(byte[] readuntil, ByteBuffer byteBuffer) {

        CircularByteArrayQueue queue = new CircularByteArrayQueue(128);

        int start = byteBuffer.position();
        int length = 0;

        byteBuffer.mark();

        while (byteBuffer.hasRemaining()) {
            queue.offer(byteBuffer.get());
            length++;
            if (queue.contains(readuntil)) {
                final byte[] dst = new byte[length];
                byteBuffer.rewind();
                byteBuffer.get(dst, start, length);
                return Optional.of(dst);
            }
        }

        return Optional.empty();
    }


}
