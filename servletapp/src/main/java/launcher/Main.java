package launcher;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import servlet.DummyServlet;

import javax.servlet.ServletException;
import java.io.File;

public class Main {

    public static void main(String[] args)
            throws LifecycleException, InterruptedException, ServletException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();

        Context ctx = tomcat.addContext("", new File(".").getAbsolutePath());

        //TODO: generic way to add servlets to context, by looking into WebServlet annotation
        Tomcat.addServlet(ctx, "dummy", new DummyServlet());
        ctx.addServletMappingDecoded("/execute", "dummy");

        tomcat.start();
        tomcat.getServer().await();
    }
}
