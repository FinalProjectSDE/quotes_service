package introsde.motivationalphrases;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("motivational-phrases")
public class ServiceConfig extends ResourceConfig {
    public ServiceConfig () {
        packages("introsde.motivationalphrases");
    }
}
