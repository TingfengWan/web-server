import java.io.IOException;

import configuration.*;
import worker.*;

public class WebServer{
  private static final int THREAD_COUNT = 4;

  private static ConfigurationReader configReader;
  private static HTTPDConf httpdConf;
  private static MIMETypes mimeTypes;
  private static int port;

  public static void main(String[] args) throws IOException {
    loadConfiguration();
    startServer();
  }

  private static void startServer() throws IOException {
    ServerWorker server = new ServerWorker(port, THREAD_COUNT);
    new Thread(server).start();
  }

  private static void loadConfiguration() {
    configReader = new ConfigurationReader();
    httpdConf = (HTTPDConf) configReader.getConfig(ConfigurationReader.HTTPD_CONF);
    mimeTypes = (MIMETypes) configReader.getConfig(ConfigurationReader.MIME_TYPE);
    port = Integer.parseInt(httpdConf.lookUp("Listen", "HTTPD_CONF"));
  }

}
