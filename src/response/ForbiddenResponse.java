package response;

import resource.*;
import java.io.IOException;

public class ForbiddenResponse extends Response {

  public ForbiddenResponse(Resource resource) throws IOException {
    this.resource = resource;
    this.request = resource.getRequest();
    this.statusCode = 403;
    this.reasonPhrase = "FORBIDDEN";
  }
}
