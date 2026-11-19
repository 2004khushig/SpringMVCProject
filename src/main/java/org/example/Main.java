
package org.example;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.example.config.WebConfig;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {

        // 1. Start Tomcat
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();

        String contextPath = "";
        String baseDoc = new File("src/main/webapp").getAbsolutePath();

        Context context = tomcat.addContext(contextPath, baseDoc);

        // 2. Create Spring IoC container
        AnnotationConfigWebApplicationContext springContext =
                new AnnotationConfigWebApplicationContext();

        springContext.register(WebConfig.class);

        // 3. Create DispatcherServlet
        DispatcherServlet dispatcherServlet =
                new DispatcherServlet(springContext);

        // 4. Register servlet with Tomcat
        Wrapper servlet = Tomcat.addServlet(
                context, "dispatcherServlet", dispatcherServlet
        );

        servlet.setLoadOnStartup(1);

        // 5. Map requests
        context.addServletMappingDecoded("/", "dispatcherServlet");

        // 6. Start server
        tomcat.start();
        System.out.println("tomcat started");
        tomcat.getServer().await();
    }
}
