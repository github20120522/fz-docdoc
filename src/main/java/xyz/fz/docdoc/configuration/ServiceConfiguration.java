package xyz.fz.docdoc.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"xyz.fz.docdoc.service", "xyz.fz.docdoc.util"})
public class ServiceConfiguration {
}
