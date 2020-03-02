
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
  private static String ALIAS        = "ALIAS";
  private static String DOCUMENTROOT = "DocumentRoot";
  private static String PROTECTED    = ".htaccess";
  private static String abSCRIPTED   = "/ab/";
  private static String TRACIELY     = "/~traciely/";
  private static String SCRIPTAlias  = "/cgi-bin/";

  private Request request;
  public String uri;

  public Resource(Request request) throws IOException {
    this.request = request;
    this.uri = this.request.getIdentifier();
  }

  public String absolutePath() {
    if(this.isScripted()) {

      if(this.uri.equals(abSCRIPTED)) {
        return WebServer.httpdConf.lookUp(this.uri, ALIAS).concat("index.html");
      }

      if(this.uri.equals(SCRIPTAlias)) {
        return WebServer.httpdConf.lookUp(this.uri, "SCRIPT_ALIAS").concat("perl_env");
      }
      //handle TRACIELY scripted
      if(this.uri.equals(TRACIELY)) {
        return WebServer.httpdConf.lookUp(this.uri, "ALIAS").concat("index.html");
      }
    }

    if(this.uri.endsWith("/")) {
      return WebServer.httpdConf.lookUp(DOCUMENTROOT, HTTPD_CONF)
        .concat(this.trimedUri()).concat("index.html");
    }

    return WebServer.httpdConf.lookUp(DOCUMENTROOT, HTTPD_CONF)
      .concat(this.trimedUri());
  }

  private Boolean isScripted() {
    return this.uri.equals(
      abSCRIPTED) || this.uri.equals(TRACIELY) || this.uri.equals(SCRIPTAlias);
  }

  public Boolean isProtected() {
    File accessFile = new File(this.getHtaccessPath());
    if (accessFile.exists())
      return true;
    else
      return false;
  }

  public String getUri() {
    return this.request.getIdentifier();
  }

  public String trimedUri() {
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
