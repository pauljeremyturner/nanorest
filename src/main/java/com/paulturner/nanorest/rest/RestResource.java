package com.paulturner.nanorest.rest;

public interface RestResource<Req, Res> {

    RestEntity<Res> doGet();

    RestEntity<Res> doPost(Req req);

    Class<?> getRequestType();
}
