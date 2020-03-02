
package response;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.nio.file.Files;
import java.io.OutputStream;

import request.*;
import resource.*;
import server.WebServer;

public abstract class Response {


  Resource resource;
  Request request;
  File file;

  public String body;
  public byte[] bodyBytes;
  public int statusCode;
  public String reasonPhrase;

  public void send(OutputStream out) throws IOException {
    out.write(this.getResponseHeaders());
    out.flush();
    out.close();
  }

  public Boolean validFile() {
    if ((this.request.getIdentifier().equals("/ab/"))
    || (this.request.getIdentifier().equals("/~traciely/"))) {
      return true;
    }
    return file.exists() && !file.isDirectory();
  }

  public byte[] getResponseHeaders() throws IOException {
    StringBuilder headers = new StringBuilder();
    Date localDate = new Date();
    switch(this.statusCode){
      case 200:
      reasonPhrase = "OK";
      break;
      case 201:
      reasonPhrase = "CREATED";
      break;
      case 204:
      reasonPhrase = "NOTCREATED";
      break;
      case 304:
      reasonPhrase = "NOTMODIFIED";
      break;
      case 400:
      reasonPhrase = "BADREQUEST";
      break;
      case 401:
      reasonPhrase = "UNAUTHORIZED";
      break;
      case 403:
      reasonPhrase = "FORBIDDEN";
      break;
      case 404:
      reasonPhrase = "NOTFOUND";
      break;
      case 500:
      reasonPhrase = "INTERNALSERVERERROR";
      break;
      default:
      throw new IOException();
    }
    


    headers.append(this.request.getVersion());
    headers.append(" ");
    headers.append(this.statusCode);
    headers.append(" ");
    headers.append(this.reasonPhrase);
    headers.append("\n");
    headers.append("Date: ");
    headers.append(localDate);
    headers.append("\n");
    headers.append("Server: HTTP/1.1");
    headers.append("\n");
    headers.append("Status: ");
    headers.append(" ");
    headers.append(this.statusCode);
    headers.append(" ");
    headers.append(reasonPhrase);
    headers.append("\n");
    if(reasonPhrase == "UNAUTHORIZED"){
       headers.append("WWW-Authenticate: Basic");
       headers.append("\n");
    }
    if(file != null){
      headers.append("Content-Type: " + this.getContentType());
      headers.append("\n");
      headers.append("Content-Length: " + this.getResource().length);
      headers.append("\n");
      headers.append("\n");
    }
    byte[] headersBytes = headers.toString().getBytes();
    return headersBytes;
  }
 

  public String getContentType() {
    String[] identifiers = file.getName().split("\\.");
    String lastElement = identifiers[identifiers.length - 1];
    String mimeType = WebServer.mimeTypes.lookUp(lastElement, "MIME_TYPE");

    return mimeType;
  }

  public byte[] getResource() throws IOException {
	  byte[] fileContent;
	  
	  if(this.file.exists()){
      
      System.out.println(file.canRead());
		  fileContent = Files.readAllBytes(this.file.toPath());
	  }
	  
	  else{
		  fileContent = new byte[0];
	  }
	  return fileContent;
  }

  public int getByteLength() {
    if (this.bodyBytes == null ) {
      return 0;
    }

    return this.bodyBytes.length;
  }

  public int getStatusCode() {
    return this.statusCode;
  }
  public File getFile(){
    return this.file;
  }
}
