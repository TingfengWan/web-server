package configuration;

public class ConfigurationReader {

  public static final String HTTPD_CONF = "HTTPD_CONF";
  public static final String MIME_TYPE = "MIME_TYPE";
  public static final String HTACCESS = "HTACCESS";
  public static final String HTPASSWD = "HTPASSWD";
  
  public Config getConfig(String string) {
    switch(string) {
      case HTTPD_CONF:
        return new HTTPDConf("public_html\\httpd.conf");
      case MIME_TYPE:
        return new MIMETypes("public_html\\mime.types");
      default:
        return null;
    }
  }
}
