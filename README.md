# Nanorest

This was initially an interview exercise to create a RFC-2616 compliant http server without any additional dependencies

Nanorest uses NIO.2 async IO to process the http protocol for ReST GET and POST requests.

# What does it do?

Nano rest lets you code HTTP ReST operations to a java interface.  That means you define a class with a method that
accepts the request payload as a parameter and returns the response payload - as java types.

The conversion to/from json is done using jackson.

# How do I do that

There are 2 things you need to do:

1: Add an entry in httproutes file located in `src/main/resources`

2: Define your class that implements `com.paulturner.nanorest.rest.RestResource`.

## The httproutes file

This is a text file that determines the mapping between a uri and a class to process it.  Here is an example:

```
/sum com.paulturner.nanorest.modules.sum.SumResource
```

The first part is the path and the secnf part is the FQ name of the class to process it

## The `RestResource` interface

You have 3 methods you need to implement:

```
    RestEntity<Res> doGet();

    RestEntity<Res> doPost(Req req);

    Class<?> getRequestType();
```

The class `RestEntity` is a simple wrapper class.  It is not practical for the `doGet` and `doPost` methods to return the
response payload type because it is impossible to return anything useful in the event of a non 2xx response.

## Using the Sum module

An example module has already been written and included.  This keeps the sum and allows http POST requests to increase it and http GET requests to query it. 

GET Request

Use http://localhost:8080/sum


POST Requests

Use http://localhost:8080/sum

You have to specify the content-length as a header and a json payload with the value to increment by:

```
{ "sum" : "666"}
```
