
package resource;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.Instant;
import java.io.*;

import request.*;
import server.WebServer;

public class Resource {

  private static String HTTPD_CONF   = "HTTPD_CONF";
  private static String SCRIPT_ALIAS = "SCRIPT_ALIAS";
  private static String ALIAS        = "ALIAS";
  private static String DOCUMENTROOT = "DocumentRoot";
  private static String PROTECTED    = ".htaccess";

  private Request request;
  public String uri;
  private String root;

  public Resource(Request request) throws IOException {
    this.request = request;
    this.uri = this.request.getIdentifier();
    if(!this.uri.endsWith("/")){
      String[] tokens = this.uri.split("/");
      this.root = this.uri.replace(tokens[tokens.length - 1], "");
    }
    else
      this.root = this.uri;
    if(!isAliased())
      this.root = DOCUMENTROOT;
    System.out.println(this.uri);
  }

  public String absolutePath() {
    String lookupKey;
    String defaultKey = "";
    if(this.isAliased()) {
      if(isScripted()) 
        lookupKey = SCRIPT_ALIAS;
      else 
        lookupKey = ALIAS;
    }
    else{
      lookupKey = HTTPD_CONF;
    }
    if(this.uri.endsWith("/")){
      defaultKey = "index.html";
    }
    return WebServer.httpdConf.lookUp(root, lookupKey).concat(this.trimmedUri()).concat(defaultKey);
  }

  public Boolean isAliased() {
    if (WebServer.httpdConf.lookUp(this.root, ALIAS) != null
      || WebServer.httpdConf.lookUp(this.root, SCRIPT_ALIAS) != null)
      return true;
    return false;
  }
  public Boolean isScripted(){
    if (WebServer.httpdConf.lookUp(this.root, SCRIPT_ALIAS) != null)
      return true;
    return false;
  }

  public Boolean isProtected() {
    if(this.getHtaccessPath() != null){
      File accessFile = new File(this.getHtaccessPath());
      if (accessFile.exists())
        return true;
    }
    return false;
  }

  public String getUri() {
    return this.request.getIdentifier();
  }

  public String trimmedUri() {
    if(isAliased())
      return this.uri.substring(root.length());
    else
      return this.uri.substring(1);
  }

  public Request getRequest() throws IOException {
    return this.request;
  }

  public String getHtaccessPath() {
    String path = this.absolutePath();
    String[] tokens = path.split("/");

    if (!path.endsWith("/") && tokens.length > 0) {
      String lastToken = tokens[tokens.length - 1];
      return path.replace(lastToken, PROTECTED);
    }

    return path + PROTECTED;
  }

  public ZonedDateTime getLastModified() {
    File file = new File(this.absolutePath());
    ZonedDateTime dateTime = Instant.ofEpochMilli(file.lastModified())
      .atZone(ZoneId.of(ZonedDateTime.now().getOffset().toString()));

    return dateTime;
  }
}
