
package response;

import resource.*;
import java.io.IOException;
import java.io.OutputStream;

import java.io.File;

public class GETResponse extends Response {

  private String absolutePath;

  public GETResponse(Resource resource) throws IOException {
    this.resource = resource;
    this.request = resource.getRequest();
    this.absolutePath = resource.absolutePath();
    this.file = new File(absolutePath);

    if(this.validFile()) {
      this.statusCode = 200;
      this.reasonPhrase = "OK";
    }
    
    else{
      this.statusCode = 404;
      this.reasonPhrase = "NOTFOUND";
    }
  }
  //Get sends file content

  public void send(OutputStream out) throws IOException {
    out.write(this.getResponseHeaders());
    if(this.validFile())
      out.write(this.getResource());
    out.flush();
    out.close();
  }
  public String getContenType() {
    String[] identifiers = file.getName().split("\\.");
    String lastElement = identifiers[identifiers.length - 1];
    String mimeType = mimeTypes.lookUp(lastElement, "MIME_TYPE");

    return mimeType;
  }
}
