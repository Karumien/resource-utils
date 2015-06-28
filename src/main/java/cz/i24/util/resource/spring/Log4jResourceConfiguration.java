/*
 * Copyright (c) 2014 Karumien s.r.o.
 * 
 * The contractor, Karumien s.r.o., does not take any responsibility for defects
 * arising from unauthorized changes to the source code.
 */
package cz.i24.util.resource.spring;

import java.util.Arrays;
import java.util.Properties;

import lombok.Data;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import cz.i24.util.resource.service.ResourcePropertyService;

/**
 * Definice konfigurace logovani Log4j s moznosti refresh v runtime.
 *
 * @author <a href="miroslav.svoboda@karumien.com">Miroslav Svoboda</a>
 * @version 1.0
 * @since 27.04.2014 10:11:12
 */
@Data
public class Log4jResourceConfiguration implements InitializingBean {

    /** Sluzba rizeni nastaveni zdroju */
    private ResourcePropertyService resourcePropertyService;

    /** Nazev konfiguracniho souboru */
    private Resource configuration;

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() {
        this.refresh();
    }

    /**
     * Provede refresh konfigurace logovani.
     */
    public void refresh() {

        // zadna zmena bez konfigurace
        if (this.configuration == null) {
            return;
        }

        // nacteni hodnot pro logovani
        Properties log4jProperties = this.resourcePropertyService.getMergedProperties(
                this.resourcePropertyService.getApplication(), Arrays.asList(this.configuration),
                Arrays.asList(this.configuration.getFilename()), this.resourcePropertyService.isDev());

        // reset log manazera a zavedeni logovani dle nove konfigurace
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(log4jProperties);

    }

}
