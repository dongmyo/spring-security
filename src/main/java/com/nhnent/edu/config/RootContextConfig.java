package com.nhnent.edu.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(basePackages = "com.nhnent.edu", excludeFilters = {@ComponentScan.Filter(Controller.class)})
public class RootContextConfig {

}
