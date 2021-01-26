package embedded.container.impl;

import embedded.container.EmbeddedContainer;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.reflections.Reflections;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;
import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TomcatEmbeddedContainer implements EmbeddedContainer, Runnable {
    private Tomcat server;

    public void start() {
        try {
            if(server != null) {
                server.stop();
            }

            server = new Tomcat();
            server.setPort(getPort());
            server.getConnector();

            setupContext();

            server.start();

            ExecutorService executor = Executors.newFixedThreadPool(1);
            executor.execute((TomcatEmbeddedContainer) this);
        } catch (Exception e) {
            throw new RuntimeException("Error when initializing embedded container.", e);
        }
    }

    public void stop() {
        if(server != null) {
            try {
                server.stop();
            } catch (LifecycleException e) {
                throw new RuntimeException("Error when stopping embedded container.", e);
            }
        }
    }

    public void setupContext() throws IllegalAccessException, InstantiationException {
        Context ctx = server.addContext("", new File(".").getAbsolutePath());

        Reflections reflections = new Reflections(getPackages()); //TODO: should provide feature for more then one package
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(WebServlet.class);

        for (Class<?> clazz : annotated) {
            Servlet servlet = (Servlet) clazz.newInstance();
            String servletName = clazz.getSimpleName().toLowerCase().replaceAll("servlet", "");
            Tomcat.addServlet(ctx, servletName, servlet);
            WebServlet annotation = clazz.getAnnotation(WebServlet.class);
            String url = Arrays.toString(annotation.urlPatterns());
            ctx.addServletMappingDecoded(url.substring(1, url.length() - 1), servletName);
        }
    }

    protected String getPackages() {
        //TODO: should have some configuration, but since its proof of concept, we go easy way
        return "servlet";
    }

    protected int getPort() {
        //TODO: should have some configuration, but since its proof of concept, we go easy way
        return 8080;
    }

    public void run() {
        if(server != null) {
            server.getServer().await();
        }
    }
}
