package br.tec.gtech.pdf.config;

import java.util.Properties;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VelocityEngineConfig {

    private static final String RESOURCE_LOADER_STRING_CLASS = "resource.loader.string.class";
    private static final String STRING = "string";

    @Bean
    public VelocityEngine velocityEngine() {
        VelocityEngine velocityEngine = new VelocityEngine();
        Properties props = new Properties();
        props.setProperty(RuntimeConstants.RESOURCE_LOADERS, STRING);
        props.setProperty(RESOURCE_LOADER_STRING_CLASS, StringResourceLoader.class.getName());
        velocityEngine.init(props);
        return velocityEngine;
    }
}