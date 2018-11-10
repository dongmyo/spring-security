package com.nhnent.edu;

import com.nhnent.edu.config.RootContextConfig;
import com.nhnent.edu.config.ServletContextConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

// WebApplicationInitializer -> SpringServletContainerInitializer
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    /*
        <context-param>
            <param-name>contextClass</param-name>
            <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
        </context-param>

        <context-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>com.nhnent.edu.config.RootContextConfig</param-value>
        </context-param>

        <listener>
            <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
        </listener>
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { RootContextConfig.class };
    }

    /*
        <servlet>
            <servlet-name>appServlet</servlet-name>
            <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
            <init-param>
                <param-name>contextClass</param-name>
                <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
            </init-param>
            <init-param>
                <param-name>contextConfigLocation</param-name>
                <param-value>com.nhnent.edu.config.ServletContextConfig</param-value>
            </init-param>
            <load-on-startup>1</load-on-startup>
        </servlet>
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { ServletContextConfig.class };
    }

    /*
        <servlet-mapping>
            <servlet-name>appServlet</servlet-name>
            <url-pattern>/</url-pattern>
        </servlet-mapping>
     */
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

}
