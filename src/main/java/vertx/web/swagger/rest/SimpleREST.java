package vertx.web.swagger.rest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import vertx.web.swagger.util.Runner;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Simple rest.
 */
public class SimpleREST extends AbstractVerticle {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Runner.runExample(SimpleREST.class);
    }
    // DB
    private Map<String, JsonObject> products = new HashMap<>();

    @Override
    public void start(Future<Void> startFuture) {
        setUpInitialData();
        routerConfigure();
    }

    private void routerConfigure() {

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.get("/products/:id").handler(this::handleGetProduct);
        router.put("/products/:id").handler(this::handleAddProduct);
        router.get("/products").handler(this::handleListProduct);

        router.route().handler(StaticHandler.create().setCachingEnabled(false).setIndexPage("api2/index.html"));

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

    private void handleGetProduct(RoutingContext routingContext) {
        String productID = routingContext.request().getParam("id");
        HttpServerResponse response = routingContext.response();
        if (productID == null) {
            sendError(400, response);
        } else {
            JsonObject product = products.get(productID);
            if (product == null) {
                sendError(404, response);
            } else {
                response.putHeader("content-type", "application/json").end(product.encodePrettily());
            }
        }
    }

    private void handleAddProduct(RoutingContext routingContext) {
        String productID = routingContext.request().getParam("id");
        HttpServerResponse response = routingContext.response();
        if (productID == null) {
            sendError(400, response);
        } else {
            JsonObject product = routingContext.getBodyAsJson();
            if (product == null) {
                sendError(400, response);
            } else {
                products.put(productID, product);
                response.end();
            }
        }
    }

    private void handleListProduct(RoutingContext routingContext) {
        JsonArray arr = new JsonArray();
        products.forEach((k, v) -> arr.add(v));
        routingContext.response().putHeader("content-type", "application/json").end(arr.encodePrettily());
    }

    private void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end();
    }

    private void setUpInitialData() {
        addProduct(new JsonObject().put("id", "57749")
                .put("name", "Logitech C922x Pro Stream Webcam – Full 1080p HD Camera")
                .put("description", "Web Camera specifically designed and optimized for Professional quality video streaming on social gaming and entertainment sites like Twitch and YouTube")
                .put("img_path", "https://images-na.ssl-images-amazon.com/images/I/81fR76MTvQL._SL1500_.jpg"));

        addProduct(new JsonObject().put("id", "82803").put("name", "AmazonBasics Lightweight Mini Tripod").put("description", "Available in two sizes: 50 and 60 inch. Lightweight tripod with adjustable-height legs and rubber feet. Compatible with most video cameras, digital cameras and GoPro devices").put("img_path", "https://images-na.ssl-images-amazon.com/images/G/01/apparel/rcxgs/tile._CB211431200_.gif"));

        addProduct(new JsonObject().put("id", "278973")
                .put("name", "Logitech Privacy Cover for C920 and C930e").put("description", "Logitech Privacy Cover for c930e\n" +
                        "Logitech Privacy Cover for c920\n" +
                        "Logitech Privacy Cover for c922x").put("img_path", "https://images-na.ssl-images-amazon.com/images/I/41IEC9HM7XL.jpg"));

        addProduct(new JsonObject().put("id", "337348")
                .put("name", "Logitech C270 Desktop or Laptop Webcam").put("description", "HD 720p video calling and HD video recording, 2.4 GHz Intel Core2 Duo, 2 GB RAM,200 MB hard drive space").put("img_path", "https://images-na.ssl-images-amazon.com/images/I/41mOPjlA%2BYL.jpg"));
    }

    private void addProduct(JsonObject venue) {
        products.put(venue.getString("id"), venue);
    }
}
