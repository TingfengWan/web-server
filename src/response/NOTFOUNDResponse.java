package response;

import resource.*;
import java.io.IOException;

public class NOTFOUNDResponse extends Response{
    public NOTFOUNDResponse(Resource resource) throws IOException {
    this.resource = resource;
    this.request = resource.getRequest();
    this.statusCode = 404;
    this.reasonPhrase = "NOTFOUND";
    }
}
