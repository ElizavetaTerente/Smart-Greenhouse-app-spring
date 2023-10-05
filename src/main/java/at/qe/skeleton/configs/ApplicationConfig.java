package at.qe.skeleton.configs;

import java.net.InetAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Value("${MYSQL_HOST:localhost}")
    private String mysqlHost;

    public ApplicationConfig() {
        try {
            InetAddress serverAddress = InetAddress.getLocalHost();
            mysqlHost = serverAddress.getHostAddress();
        } catch (Exception e) {
            // Handle any exceptions
        }
    }
}