
package response;

import resource.*;
import java.io.IOException;
import java.io.File;

public class DELETEResponse extends Response {
  //202, 204, 200

  private String absolutePath;
  //Deletes file

  public DELETEResponse(Resource resource) throws IOException {
    this.resource = resource;
    this.request = resource.getRequest();
    this.absolutePath = resource.absolutePath();
    this.file = new File(absolutePath);

    if(this.validFile()) {
      this.statusCode = 204;
      this.reasonPhrase = "NO CONTENT";
      this.file.delete();
    }
  }
}
