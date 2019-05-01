package com.paulturner.nanorest.modules.sum;

import java.util.Date;
import java.util.logging.Logger;

import com.paulturner.nanorest.http.Headers;
import com.paulturner.nanorest.http.Status;
import com.paulturner.nanorest.rest.RestEntity;
import com.paulturner.nanorest.rest.RestResource;

public class SumResource implements RestResource<Sum, Sum> {

    private static final Logger logger = Logger.getLogger(SumResource.class.getName());

    private final static SumState sumState = new SumState();


    public RestEntity<Sum> doGet() {
        return new RestEntity<Sum>(Status.OK, new Sum(sumState.getSum()), getDefaultHeaders());
    }

    public RestEntity<Sum> doPost(final Sum sum) {
        sumState.add(sum.getSum());

        return new RestEntity<>(Status.OK, new Sum(sumState.getSum()), getDefaultHeaders());
    }

    public Headers getDefaultHeaders() {
        Headers httpHeaders = new Headers();
        httpHeaders.addHeader("Content-Type", "application/json");
        httpHeaders.addHeader("Date", new Date().toString());
        httpHeaders.addHeader("Host", "localhost");
        return httpHeaders;
    }

    @Override
    public Class<?> getRequestType() {
        return Sum.class;
    }

}
