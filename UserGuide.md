# SwiftServe User Guide

- [SwiftServe User Guide](#swiftserve-user-guide)
    - [Overview](#overview)
    - [Goals for SwiftServe](#goals-for-swiftserve)
    - [SwiftServe Users](#swiftserve-users)
    - [Using SwiftServe](#using-swiftserve)
        - [Simple example](#simple-example)
            - [Using lamba expression](#using-lamba-expression)
        - [Queries and route parameters](#queries-and-route-parameters)
        - [Extract the request body](#extract-the-request-body)
            - [Form-data format](#form-data-format)
            - [Json format](#json-format)
            - [String format](#string-format)
            - [Url-encoded format](#url-encoded-format)
            - [Instance of the specified class](#instance-of-the-specified-class)
        - [Route mapping](#route-mapping)
        - [Middlewares](#middlewares)
        - [Creating web services](#creating-web-services)
            - [Creation of a REST API manually](#creation-of-a-rest-api-manually)
            - [Automate the creation of a REST API](#automate-the-creation-of-a-rest-api)
            - [Complex design](#complex-design)

## Overview

SwiftServe is a web framework follows the MVC2 pattern where you can build with it web application and web services in simple and efficient way with less time.


## Goals for SwiftServe

* Follow the pattern MVC2.
* Robust routing.
* Automate the creation of web services.
* Simplicity.
* Easy way for set up middleware.
* Executable for generating applications quickly.


## SwiftServe Users

SwiftServe is web framework design for java developer and non-java developer who has experience in web development for creating web application and web services in less time.

## Using SwiftServe

The primary class to use is [`Application`](src/main/java/com/creativityfactory/swiftserver/app/Application.java) which you can just extend your class from it to have a single controller to achieve the MVC2 pattern.


### Simple example

Here is a simple example of using SwiftServe framework for rendering a hello world html page `/hello`:

Here `HelloAction.java` :

```java
public class HelloAction implements HttpRequestHandler {
  @Override
  public void method(Request req, Response res) {
      res.setHeader("Content-Type", "text/html")
      .write("<h1>")
      .write("Hello world")
      .write("</h1>");
  }
}
```

Here our MVC2 controller `MyApplication.java` :

```java

@WebServlet("/*")
public class MyApplication extends Application {
  @Override
  protected void execute() {
    get("/hello", new HelloAction());
  }
}
```


#### Using lamba expression

The same example using lambda expression :

```java

@WebServlet("/*")
public class MyApplication extends Application {
  @Override
  protected void execute() {
    get("/hello", (req, res) -> {
      res.setHeader("Content-Type", "text/html")
      .write("<h1>")
      .write("Hello world")
      .write("</h1>");
    });
  }
}
```

### Queries and route parameters
SwiftServe provides us a simple way to extract queries and route parameters.

Here is an example for `/path/:id/src` with `GET /path/1/src?limit=8`:

```java
public class MyAction implements HttpRequestHandler {
  @Override
  public void method(Request req, Response res) {
      /*
       * extract query with name `limit`
       * output: limit = 8
       */
      String limit = req.query("limit");

      /*
       * extract route parameter with name `id`
       * output: id = 1
       */
      String id = req.params("id");

  }
}
```

### Extract the request body

In order to extract the body of request SwiftServe offers many way basing on the mime-type of request to insure the compatibilty.

#### Form-data format

```java
public class MyAction implements HttpRequestHandler {
  @Override
  public void method(Request req, Response res) {
      Map<String, String> body = req.formDataBody();
      // ...
  }
}
```

#### Json format

```java
public class MyAction implements HttpRequestHandler {
  @Override
  public void method(Request req, Response res) {
      String body = req.jsonBody();
      // ...
  }
}
```

#### String format

```java
public class MyAction implements HttpRequestHandler {
  @Override
  public void method(Request req, Response res) {
      String body = req.body();
      // ...
  }
}
```

#### Url-encoded format

```java
public class MyAction implements HttpRequestHandler {
  @Override
  public void method(Request req, Response res) {
      String body = req.urlEncodedFormatBody();
      // ...
  }
}
```

#### Instance of the specified class

SwiftServe provides a way to parse the body of the request and maps it to an instance of the specified class.

Here is an example:

```java
public class Book {
  private String isbn;
  private String title;
  // ...
}
```

If the request is in form of json format and you want to parse it and map it to an instance of `Book` :

```java
public class MyAction implements HttpRequestHandler {
  @Override
  public void method(Request req, Response res) {
      Book book = (Book) req.body(Book.class);
      // ...
  }
}
```

We are working to support other format...

### Route mapping

SwiftServe has simple and robust routing system, where you can map your route with an action as we saw in the first example. It provide simple way to do your mapping for specific http method for a route with an action. As It support the most used http methods: `GET`, `POST`, `PUT`, `PATCH` and `DELETE`:

```Java
@WebServlet("/*")
public class MyApplication extends Application {
  // ...
  @Override
  protected void execute() {
    get("/resource", action1);
    post("/resource", action2);
    put("/resource", action3);
    patch("/resource", action4);
    delete("/resource", action5);
    // ...

  }
}
```

### Middlewares
SwiftServe offers us writing middleware in simple, efficient and modern way.

Here is an example of using middlewares for a specific route :

```Java
@WebServlet("/*")
public class MyApplication extends Application {
  @Override
  protected void execute() {
    post("/resource", new AuthAction());
    post("/resource", new MyAction());
    // ...
  }
}
```

For controlling the flow of middlewares, the request object contains `next(boolean n)` method for indication of passing this request for the next middleware or not.

Here `AuthAction.java`

```java
public class AuthAction implements HttpRequestHandler {
  @Override
  public void method(Request req, Response res) {
      // ...
      if (isAuth) req.next(true);
      // ...
  }
}
```

### Creating web services

#### Creation of a REST API manually

SwiftServe offers a simple and efficient way to create web services in form of REST API. In this example we will try to create a REST API for a model called `Todo`. Let's start to see the simplicity.

Here we have the model `Todo.java` :
```java
public class Todo {
  private Integer id;
  private String content;
  private Boolean completed;
  // ... ctor, setters & getters
}
```

Let's assume that we have a dao oject for persistence tier named `todoDao`.

For `GET /todos` :
```java
public class GetAllTodo implements HttpRequestHandler {
  @Override
  public void method(Request req, Response res) {
      // ... 
      List<Todo> todos = todoDao.getAll();
      res.json(todos);
  }
}
```

For `GET /todos/:id` :
```java
public class GetTodoById implements HttpRequestHandler {
  @Override
  public void method(Request req, Response res) {
      // ... 
      String id = req.params("id");
      Todo todo = todoDao.getById(id);
      res.json(todo);
  }
}
```

For `POST /todos/` :
```java
public class AddTodo implements HttpRequestHandler {
  @Override
  public void method(Request req, Response res) {
      // ... 
      Todo todo = (Todo) req.body(Todo.class);
      Todo savedTodo = todoDa.save(todo);

      res.status(201)
        .json(savedTodo);
  }
}
```


For `PUT /todos/:id` :
```java
public class UpdateTodo implements HttpRequestHandler {
  @Override
  public void method(Request req, Response res) {
      // ... 
      String id = req.params("id");
      Todo todo = (Todo) req.body(Todo.class);
      todo.setId(id);
      Todo updatedTodo = todoDao.update(todo);

      res.json(updatedTodo);
  }
}
```

For `DLETE /todos/:id` :
```java
public class DeleteTodo implements HttpRequestHandler {
  @Override
  public void method(Request req, Response res) {
      // ... 
      String id = req.params("id");
      Todo todo = todoDao.getById(id);
      Todo deleted = todoDao.delete(todo);

      res.json(updatedTodo);
  }
}
```

For now let's do the route mapping in our controller:

```java
@WebServlet("/*")
public class MyApplication extends Application {
  @Override
  protected void execute() {
    get("/todos", new GetAllTodo());
    get("/todos/:id", new GetTodoById());
    post("/todos", new AddTodo());
    put("/todos/:id", new UpdateTodo());
    delete("/todos/:id", new DeleteTodo());
  }
}
```

Maybe this was not a great example but it still simple and easy to understand the mecanism, and we will see next how this hard and repetitive work can be done by SwiftServe.

#### Automate the creation of a REST API

With SwiftServe the creation of REST API can be done automatically. All what you need is only the creation of models. In addition, SwiftServe adds the validation of received that if you want to add it to your manually created REST is a bunch of headache. SwiftServe support inheritance and complex design, well written error message and more...

Here an example where you can use SwiftServe to automate the creation of REST API:

```java
// we suppose we have a persistence object for todo named: todoDao

@DataSource("todoDtSrc")
public class TodoPersistence implements Persistence<Todo> {
  @Override
  public List<Todo> getAll() {
    return todoDao.getAll();
  }
  // other methods ...
}
```

Here we have the model:
```java
@Rest
@FromDataSource("todoDtSrc")
public class Todo {
  private Integer id;
  private String content;
  private Boolean completed;
}
```

and now let's to generate the REST API:
```java
@WebServlet("/*")
public class MyApplication extends Application {
  @Override
  protected void execute() throws Exception {
    generateRest();
  }
}
```
Congratulation 🎊 now you have generated your REST API with the next endpoints:
- `GET /todos`: get all todos
- `GET /todos?limit=n`: get todos limited to n.
- `GET /todos/:id`: get a todo by id.
- `POST /todos`: add a new todo.
- `PUT /todos/:id`: update all the field of an existed todo.
- `PATCH /todos/:id`: update one field from an existed todo.
- `DELETE /todos/:id`: delete an existed todo.

The automatation of create REST APIs include the feature of auto-plural the name of models in the given models.

SwiftServe offers also a way to generate your REST APIs without writting the persistence tier, in this way you can test your application without needs of persistence tier. This is done by creating a local file database, which is called in the context of this framework `Default datasource`.

Here is an example:
```java
@Rest
@FromDataSource
public class Todo {
  private Integer id;
  private String content;
  private Boolean completed;
}
```

This will give you the same result as the last example, with difference of the where the data is stored (persistence tier).

#### Complex design

In the last example, the created REST API was only from one model, which was a simple example to demonstrate how to the automatic creation is done. Now we will create a little more complex design :

Here our class from the last example `Todo`:

```java
@Rest
@FromDataSource
public class Todo {
  private Integer id;
  private String content;
  private Boolean completed;
  @BelongTo
  private User user;
}
```

Here the new class `User`:
```java
@Rest
@FromDataSource
public class User {
  String private id;
  private String name;
  @HasMany
  private List<Todo> todos;
}
```

Let's to generate the REST API:
```java
@WebServlet("/*")
public class MyApplication extends Application {
  @Override
  protected void execute() throws Exception {
    generateRest();
  }
}
```

In this example we have a design: `a user has many todos` and `todo belongs to a user`, this means the relationship of One-To-Many.
```
+------+               +------+
| User | 1 --------- * | Todo |
+------+               +------+
 ```

This will generate a REST API with the next endpoints:

for `Todo` model:
- `GET /todos`: get all todos
- `GET /todos?limit=n`: get todos limited to n.
- `GET /todos/:id`: get a todo by id.
- `POST /todos`: add a new todo.
- `PUT /todos/:id`: update all the field of an existed todo.
- `PATCH /todos/:id`: update one field from an existed todo.
- `DELETE /todos/:id`: delete an existed todo.

for `User` model:
- `GET /users`: get all users
- `GET /users?limit=n`: get users limited to n.
- `GET /users/:id/todos`: get all todos of user with matched id.
- `GET /users/:id`: get a user by id.
- `POST /users`: add a new user.
- `PUT /users/:id`: update all the field of an existed user.
- `PATCH /users/:id`: update one field from an existed user.
- `DELETE /users/:id`: delete an existed user.