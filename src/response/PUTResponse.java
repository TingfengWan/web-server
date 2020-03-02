
package response;

import resource.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;

public class PUTResponse extends Response {

  private String absolutePath;
  //Put creates a file

  public PUTResponse(Resource resource) throws IOException {
    this.resource = resource;
    this.request = resource.getRequest();
    this.absolutePath = resource.absolutePath();
    this.file = new File(absolutePath);
    this.body = this.request.getBody();
    this.bodyBytes = this.body.getBytes();

    if(this.validFile()) {
      if (!this.body.equals("")){
        this.statusCode = 201;
        this.reasonPhrase = "CREATED";
      }
      else{
        this.statusCode = 204;
        this.reasonPhrase = "NOT CREATED";
      }
    }
    else{
      this.statusCode = 404;
      this.reasonPhrase = "NOT FOUND";
    }
  }

  public void send(OutputStream out) throws IOException {
    out.write(this.getResponseHeaders());
    if(this.validFile()) {
      out.write(this.getResource());
    }
    out.flush();
    out.close();
  }
}
