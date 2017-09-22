package vertx.web.swagger.generator;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.swagger.models.*;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.vertx.core.json.JsonObject;

/**
 * The type Product endpoints.
 */
final class ProductEndpoints {

    private static final String DEFAULT_HOST = "localhost:8080";

    /**
     * Gets file name.
     *
     * @return the file name
     */
    static String getFileName() {
        return "openAPI2.json";
    }

    private static final Info INFO = new Info()
            .title("Product Editor Application")
            .description("This editor provide a user interface for product management.")
            .termsOfService("https://smartbear.com/terms-of-use-on-prem/")
            .contact(new Contact().email("apiteam@swagger.io"))
            .license(new License().name("© 2017 SmartBear Software. All Rights Reserved.").url("https://smartbear.com"));


    /**
     * Build swagger swagger.
     *
     * @return the swagger
     */
    static Swagger buildSwagger() {

        Swagger swagger = new Swagger();
        swagger.info(ProductEndpoints.INFO)
                .host(ProductEndpoints.getHost())

                .consumes(HttpHeaderValues.APPLICATION_JSON.toString())
                .produces(HttpHeaderValues.APPLICATION_JSON.toString())

                .tag(ProductEndpoints.getVenueTag())

                .path("/products", ProductEndpoints.getProductListPath())
                .path("/products/{id}", ProductEndpoints.getProductById())
                .path("/products/{id} ", ProductEndpoints.addNewProduct())

                .addDefinition("Product", ProductEndpoints.getProductDefinition());
        return swagger;
    }

    /**
     * Gets host for property file (not implement yet) or use default 'localhost'.
     *
     * @return the host
     */
    private static String getHost() {
        return DEFAULT_HOST;
    }

    private static Tag getVenueTag() {
        return new Tag().name("Product").description("The product that present in our shop")
                .externalDocs(new ExternalDocs().description("Sell on Amazon").url("https://services.amazon.com/content/sell-on-amazon.htm/ref=footer_soa?ld=AZFSSOA-dT1"));
    }

    private static Path getProductListPath() {
        return new Path().get(
                new Operation()
                        .summary("Get list of Products")
                        .description("Return list of Products that contain an information about Product address and etc.")
                        .tag("Product")
                        .operationId("getProductListPath")
                        .consumes(HttpHeaderValues.APPLICATION_JSON.toString())
                        .produces(HttpHeaderValues.APPLICATION_JSON.toString())
                        .response(HttpResponseStatus.OK.code(),
                                new Response().description("Successful operation").schema(
                                        new ArrayProperty().items(new RefProperty("Product"))))

                        .response(HttpResponseStatus.NOT_FOUND.code(),
                                new Response().description("Products not found"))

                        .response(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(),
                                new Response().description("Internal server error"))
        );
    }

    private static Path getProductById() {
        return new Path().get(
                new Operation()
                        .summary("Return specific Product")
                        .description("Return specific Product by id or nothing if not found.")
                        .tag("Product")
                        .operationId("getProductById")
                        .consumes(HttpHeaderValues.APPLICATION_JSON.toString())
                        .produces(HttpHeaderValues.APPLICATION_JSON.toString())
                        .parameter(new PathParameter().name("id").description("Product id that will be return").required(true).type("long").format("int64"))
                        .response(HttpResponseStatus.OK.code(),
                                new Response().description("Successful operation").schema(
                                        new RefProperty("Product")))

                        .response(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(),
                                new Response().description("Internal server error"))
        );
    }

    private static Path addNewProduct() {
        BodyParameter body = new BodyParameter().name("body")
                .description("Product that should be added to the DB")
                .schema(new RefModel("Product"));
        body.setRequired(true);

        return new Path().put(
                new Operation()
                        .summary("Add new Product in list of Products")
                        .description("Add new Product into database.")
                        .tag("Product")
                        .operationId("handleAddProduct")
                        .consumes(HttpHeaderValues.APPLICATION_JSON.toString())
                        .produces(HttpHeaderValues.APPLICATION_JSON.toString())
                        .parameter(body)
                        .response(HttpResponseStatus.OK.code(),
                                new Response().description("Successful operation").schema(
                                        new RefProperty("Product")))

                        .response(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(),
                                new Response().description("Internal server error"))
        );
    }

    private static Model getProductDefinition() {
        return new ModelImpl()
                .example(new JsonObject()
                        .put("id", 1120L)
                        .put("name", "Logitech C922x Pro Stream Webcam")
                        .put("description", "Web Camera specifically designed and optimized for Professional quality video streaming on social gaming and entertainment sites like Twitch and YouTube")
                        .put("img_path", "https://images-na.ssl-images-amazon.com/images/I/81fR76MTvQL._SL1500_.jpg"))
                .description("The product that can be buy in our shop.")
                .property("id", new LongProperty().example(1120L))
                .property("name", new StringProperty().example("Logitech C922x Pro Stream Webcam"))
                .property("description", new StringProperty().example("Web Camera specifically designed and optimized for Professional quality video streaming on social gaming and entertainment sites like Twitch and YouTube"))
                .property("img_path", new StringProperty().example("https://images-na.ssl-images-amazon.com/images/I/81fR76MTvQL._SL1500_.jpg"));
    }
}
