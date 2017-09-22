package vertx.web.swagger.generator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.Swagger;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * The type Open Api v 2.0 JSON generator.
 */
public class OpenAPI2Generator {

    private static File outputFolder = new File("src/main/resources/webroot/api2", "api");

    /**
     * The entry point of application that generate Open API v.2.0 file in JSON format.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        clearFolder();
        //we can collect from different endpoints
        Swagger swagger = ProductEndpoints.buildSwagger();

        FileUtils.writeStringToFile(new File(outputFolder, ProductEndpoints.getFileName()), toJson(swagger));
    }

    private static void clearFolder() throws IOException {
        if (outputFolder.exists()) {
            FileUtils.deleteDirectory(outputFolder);
        }
    }

    private static String toJson(Object value) throws JsonProcessingException {
        return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .writerWithDefaultPrettyPrinter().writeValueAsString(value);
    }

}
