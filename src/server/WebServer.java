package server;

import worker.ServerWorker;
import java.io.IOException;

import configuration.*;

public class WebServer{
  private static final int THREAD_COUNT = 4;

  public static HTTPDConf httpdConf = (HTTPDConf) ConfigurationReader.getConfig(ConfigurationReader.HTTPD_CONF);
  public static MIMETypes mimeTypes = (MIMETypes) ConfigurationReader.getConfig(ConfigurationReader.MIME_TYPE);
  public static int port = Integer.parseInt(httpdConf.lookUp("Listen", "HTTPD_CONF"));

  public static void main(String[] args) throws IOException {
    ServerWorker server = new ServerWorker(port, THREAD_COUNT);
    new Thread(server).start();
  }

}
