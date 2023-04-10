package com.creativityfactory.swiftserver.app;

import com.creativityfactory.swiftserver.annotation.*;
import com.creativityfactory.swiftserver.middleware.*;
import com.creativityfactory.swiftserver.middleware.rest.*;
import com.creativityfactory.swiftserver.request.HttpRequest;
import com.creativityfactory.swiftserver.request.Request;
import com.google.gson.Gson;
import com.creativityfactory.swiftserver.persistence.Persistence;
import com.creativityfactory.swiftserver.persistence.SingletonDataSource;
import com.creativityfactory.swiftserver.response.HttpResponse;
import com.creativityfactory.swiftserver.response.Response;
import com.creativityfactory.swiftserver.utils.FieldUtils;
import com.creativityfactory.swiftserver.utils.IdUtils;
import com.creativityfactory.swiftserver.utils.RouteUtils;
import com.creativityfactory.swiftserver.request.*;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.atteo.evo.inflector.English;
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

/**
 * <p>This class is the core of this framework where it's act as your single controller.</p>
 * <p>It extends from {@link HttpServlet} and override its functionalities to make it
 * more simple to use. </p>
 *
 * <p>This class is holding a route mapping container of your application, so a robust routing system.</p>
 * <p>Here is an example of web application: </p>
 * <pre>{@code
 * @WebServlet("/*")
 * public MyApp extends Application {
 *      @Override
 *      protected void execute() throws Exception {
 *          get("/", (req, res) -> {
 *             res.setHeader("Content-Type", "text/html")
 *             .write("<h1>Hello world</h1>");
 *          });
 *      }
 * }
 * }</pre>
 */
public class Application extends HttpServlet {
    private Map<String, List<HttpRequestHandler>> urlGetMap;
    private Map<String, List<HttpRequestHandler>> urlPostMap;
    private Map<String, List<HttpRequestHandler>> urlPutMap;
    private Map<String, List<HttpRequestHandler>> urlDeleteMap;
    private Map<String, List<HttpRequestHandler>> urlPatchMap;
    private Map<String, Date> cacheableResources;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        try {
            initiator();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is responsible for initialize the containers of this class and run the code of client.
     * @throws Exception
     */
    private void initiator() throws Exception {
        urlGetMap = new LinkedHashMap<>();
        urlPostMap = new LinkedHashMap<>();
        urlPutMap = new LinkedHashMap<>();
        urlDeleteMap = new LinkedHashMap<>();
        urlPatchMap = new LinkedHashMap<>();
        cacheableResources = new HashMap<>();
        gson = new Gson();

        // run the code of the client
        execute();
    }

    /**
     * <p>This method is a central method which intercept every incoming http request and look
     * for its handlers.</p>
     *
     * <p>It's responsible for managing middlewares for every endpoint where it starts executing them
     * in the given order from the client code inside a loop.</p>
     * @param mappedMethods
     * @param request
     * @param response
     * @throws IOException
     */
    protected void process(Map<String, List<HttpRequestHandler>> mappedMethods, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // get the path of this request to call the matched method mapped to this path
        String path = request.getRequestURI().replaceFirst(request.getContextPath(), "").replaceFirst(request.getServletPath(), "");
        for (Map.Entry<String, List<HttpRequestHandler>> set: mappedMethods.entrySet()) {
            if (RouteUtils.isUrlPatternMatched(set.getKey(), path)) {
                request.setAttribute("pattern", set.getKey());
                try {
                    // creating our custom request & response
                    Request req = new HttpRequest(request);
                    Response res = new HttpResponse(request, response);

                    // calling the mapped methods with this request
                    int i = 0;
                    List<HttpRequestHandler> handlerList = set.getValue();
                    do {
                        // default value
                        req.next(false);

                        HttpRequestHandler handler = handlerList.get(i++);
                        handler.method(req, res);
                    } while (req.shouldContinue() && i < handlerList.size());

                } catch (Exception exception) {
                    System.out.println("[Process err]: " + exception.getMessage());
                    exception.printStackTrace();
                    try {
                        response.sendError(response.SC_INTERNAL_SERVER_ERROR);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return;
            }
        }

        response.sendError(response.SC_NOT_FOUND);
    }

    // for carrying and executing the code of the client
    protected void execute() throws Exception {}
    // scan classes and call for generating rest

    /**
     * This method is responsible for automate the creation of web service based on models with
     * annotation {@link Rest}. It scans classes and process them and do the necessary mapping, for
     * creation of a web service for a model.
     */
    protected void generateRest() throws Exception {
        Gson gson = new Gson();
        Reflections reflections = new Reflections();
        // get all classes with @Rest annotation
        Set<Class<?>> restClasses = reflections.get(SubTypes.of(TypesAnnotated.with(Rest.class)).asClass());
        // get all classes with @DataSource annotation
        Set<Class<?>> dsrClasses = reflections.get(SubTypes.of(TypesAnnotated.with(DataSource.class)).asClass());

        Map<String, Class<? extends Persistence<?>>> dtSrcMap = new HashMap<>();
        // map data sources instances with its name
        for (Class<?> clazz: dsrClasses) {
            Class <?> [] interfaces = clazz.getInterfaces();
            boolean isTrue = true;
            for (Class<?> interfaze: interfaces) {
                if(interfaze == Persistence.class){
                    isTrue = false;
                    String dataSource = clazz.getAnnotation(DataSource.class).value();
                    dtSrcMap.put(dataSource, (Class<? extends Persistence<Object>>) clazz);
                }
            }

            if (isTrue) throw new Exception("The data source class does not implement the interface persistence");
        }
        // initialize
        SingletonDataSource.init(dtSrcMap);
        // iterate all the models with Rest annotation
        for (Class<?> clazz: restClasses) {
            if (FieldUtils.getAllFields(clazz).size() == 0) throw new Exception("Cannot generate a rest api from an empty model");
            // get the data source name from the rest class
            if (!clazz.isAnnotationPresent(FromDataSource.class)) throw  new Exception("Can not generate a rest api from data source does not exist");

            createRest(clazz);
        }
    }

    /**
     * This method is responsible for creating a rest api of a given model.
     * @param model The model which the client want to have a rest api on it.
     */
    protected void createRest(Class<?> model) throws InstantiationException, IllegalAccessException, IOException {
        HttpRequestHandler setUpModel = new SetUpModel(model, this.gson);
        HttpRequestHandler setDataSource = new SetDataSource();
        HttpRequestHandler idConverter = new IdConverter();
        HttpRequestHandler contentTypeValidator = new ContentTypeValidation();
        HttpRequestHandler objectMapper = new ObjectMapper();
        HttpRequestHandler receivedDataValidator = new ReceivedDataValidation();
        HttpRequestHandler cacheableResource = new CacheResource(cacheableResources);
        HttpRequestHandler updateCacheableResource = new UpdateCacheableResource(cacheableResources);
        // Plural the name of the model
        String path = "/" + English.plural(model.getSimpleName().toLowerCase()) + "/";
        System.out.println("Path: " + path);
        // TODO: loosely coupling between classes [Almost DONE]
        // cacheable
        Date d = new Date();
        System.out.println("Created at: " + d);
        cacheableResources.put(model.getName(), d);

        // get method: [DONE]
        get(path, setUpModel);
        get(path, setDataSource);
        get(path, cacheableResource);
        get(path, new Get());

        // get by id method [Almost DONE]
        get(path + ":id", setUpModel);
        get(path + ":id", setDataSource);
        get(path + ":id", idConverter);
        get(path + ":id", cacheableResource);
        get(path + ":id", new GetById());

        // post method [Almost DONE]
        post(path, setUpModel);
        post(path, setDataSource);
        post(path, contentTypeValidator);
        post(path, objectMapper);
        post(path, receivedDataValidator);
        post(path, new Post());
        post(path, updateCacheableResource);

        // put method
        put(path + ":id", setUpModel);
        put(path + ":id", setDataSource);
        put(path + ":id", contentTypeValidator);
        put(path + ":id", idConverter);
        put(path + ":id", objectMapper);
        put(path + ":id", receivedDataValidator);
        put(path + ":id", new Update());
        put(path + ":id", updateCacheableResource);

        // patch method
        patch(path + ":id", setUpModel);
        patch(path + ":id", setDataSource);
        patch(path + ":id", contentTypeValidator);
        patch(path + ":id", idConverter);
        patch(path + ":id", objectMapper);
        patch(path + ":id", receivedDataValidator);
        patch(path + ":id", new Update());
        patch(path + ":id", updateCacheableResource);

        // delete method
        delete(path + ":id", setUpModel);
        delete(path + ":id", setDataSource);
        delete(path + ":id", idConverter);
        delete(path + ":id", new Delete());
        delete(path + ":id", updateCacheableResource);

        List<Field> fields = FieldUtils.getAllFields(model);
        for (Field field: fields) {
            if (field.isAnnotationPresent(HasMany.class)) {
                ParameterizedType fieldListType = (ParameterizedType) field.getGenericType();
                Class<?> fieldListClass = (Class<?>) fieldListType.getActualTypeArguments()[0];
                Persistence<Object> persistence = SingletonDataSource.getInstance(fieldListClass);
                String pathd = path + ":id/" + English.plural(fieldListClass.getSimpleName().toLowerCase());

                get(pathd, setUpModel);
                get(pathd, idConverter);
                get(pathd, (req, res) -> {
                    Object id = req.getAttribute("id");
                    // TODO: change it
                    List<Object> list = persistence.getAll();

                    List<Object> responseList = new ArrayList<>();
                    for (Object object: list) {
                        List<Field> fields1 = FieldUtils.getAllFields(object.getClass());

                        for (Field field1: fields1) {
                            if (field1.isAnnotationPresent(BelongTo.class) && (field1.getType() == model)) {
                                field1.setAccessible(true);

                                Object obj = field1.get(object);
                                Object id1 = IdUtils.extractIdValue(obj);

                                if (id1.equals(id)) {
                                    responseList.add(IdUtils.mapIdToObject(object, fieldListClass));
                                }
                            }
                        }
                    }

                    res.json(responseList);
                });
                System.out.println(pathd);
            }
        }
    }

    /**
     * <p>This method is responsible for mapping a HttpRequestHandler object to a pattern.</p>
     * <p><strong>route mapping</strong></p>
     * @param urlPattern The URL pattern to map the handler to.
     * @param handler The HTTP request handler object to handle this requests.
     * @param urlVerbMap The container of the method of this request
     */
    private void mapHandlerToEndpoint(String urlPattern, HttpRequestHandler handler, Map<String, List<HttpRequestHandler>> urlVerbMap) {
        List<HttpRequestHandler> handlerList = urlVerbMap.computeIfAbsent(urlPattern, k -> new ArrayList<>());

        handlerList.add(handler);
    }
    // TODO: find a solution to emulate the use function that exists in express
    protected void use(String urlPattern, HttpRequestHandler handler) {
        get(urlPattern, handler);
        post(urlPattern, handler);
        put(urlPattern, handler);
        delete(urlPattern, handler);
        patch(urlPattern, handler);
    }
    /**
     * Maps a GET request with the specified URL pattern to the given HTTP request handler.
     *
     * @param urlPattern The URL pattern to map the handler to.
     * @param handler The HTTP request handler object to handle GET requests.
     */
    protected void get(String urlPattern, HttpRequestHandler handler) {
        mapHandlerToEndpoint(urlPattern, handler, urlGetMap);
    }
    /**
     * Maps a PoST request with the specified URL pattern to the given HTTP request handler.
     *
     * @param urlPattern The URL pattern to map the handler to.
     * @param handler The HTTP request handler object to handle PoST requests.
     */
    protected void post(String urlPattern, HttpRequestHandler handler) {
        mapHandlerToEndpoint(urlPattern, handler, urlPostMap);
    }
    /**
     * Maps a PUT request with the specified URL pattern to the given HTTP request handler.
     *
     * @param urlPattern The URL pattern to map the handler to.
     * @param handler The HTTP request handler object to handle PUT requests.
     */
    protected void put(String urlPattern, HttpRequestHandler handler) {
        mapHandlerToEndpoint(urlPattern, handler, urlPutMap);
    }
    /**
     * Maps a DELETE request with the specified URL pattern to the given HTTP request handler.
     *
     * @param urlPattern The URL pattern to map the handler to.
     * @param handler The HTTP request handler object to handle DELETE requests.
     */
    protected void delete(String urlPattern, HttpRequestHandler handler) {
        mapHandlerToEndpoint(urlPattern, handler, urlDeleteMap);
    }
    /**
     * Maps a PATCH request with the specified URL pattern to the given HTTP request handler.
     *
     * @param urlPattern The URL pattern to map the handler to.
     * @param handler The HTTP request handler object to handle PATCH requests.
     */
    protected void patch(String urlPattern, HttpRequestHandler handler) {
        mapHandlerToEndpoint(urlPattern, handler, urlPatchMap);
    }
    // Supporting patch method and override the behavior of get
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (method.equals("PATCH")) {
            this.doPatch(req, resp);
        } else if (method.equals("GET")) {
            this.doGet(req, resp);
        } else {
            super.service(req, resp);
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        process(urlGetMap, req, res);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        process(urlPostMap, req, res);
    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        process(urlPutMap, req, res);
    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        process(urlDeleteMap, req, res);
    }
    protected void doPatch(HttpServletRequest req, HttpServletResponse res) throws IOException {
        process(urlPatchMap, req, res);
    }
}
