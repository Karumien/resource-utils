/*
 * Copyright (c) 2014 Karumien s.r.o.
 * 
 * The contractor, Karumien s.r.o., does not take any responsibility for defects
 * arising from unauthorized changes to the source code.
 */
package cz.i24.util.resource.spring;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import cz.i24.util.resource.service.ResourcePropertyService;

/**
 * Konfigurace prostredi z DB.
 * 
 * @author <a href="miroslav.svoboda@karumien.com">Miroslav Svoboda</a>
 * @version 1.0
 * @since 27.04.2014 10:11:12
 */
public class DBResourceConfigurationFactory implements InitializingBean, FactoryBean<Properties> {

    /** Sluzba rizeni nastaveni zdroju */
    @Setter
    private ResourcePropertyService resourcePropertyService;

    /** Seznam zdroju v souborech */
    @Getter
    @Setter
    private Resource[] locations;

    /** Seznam zdroju, ktere se nesmi upgradovat */
    @Getter
    @Setter
    private List<String> noupgrade;

    /** Vysledny seznam zdroju pro aplikovani v aplikaci */
    private Properties properties = new Properties();

    /**
     * {@inheritDoc}
     */
    @Override
    public Properties getObject() {
        return this.properties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<Properties> getObjectType() {
        return Properties.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() {

        if (this.locations != null) {
            this.properties = this.resourcePropertyService.getMergedProperties(
                    this.resourcePropertyService.getApplication(), Arrays.asList(this.locations), this.noupgrade,
                    this.resourcePropertyService.isDev());
        }

    }

}
