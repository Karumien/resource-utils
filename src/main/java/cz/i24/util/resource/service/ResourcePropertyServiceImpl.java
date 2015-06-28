/*
 * Copyright (c) 2014 Karumien s.r.o.
 * 
 * The contractor, Karumien s.r.o., does not take any responsibility for defects
 * arising from unauthorized changes to the source code.
 */
package cz.i24.util.resource.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import cz.i24.util.resource.dao.ResourcePropertyDao;
import cz.i24.util.resource.entity.ResourceProperty;

/**
 * Implementace pro {@link ResourcePropertyService}.
 *
 * @author <a href="miroslav.svoboda@karumien.com">Miroslav Svoboda</a>
 * @version 1.0
 * @since 27.04.2014 10:11:12
 */
public class ResourcePropertyServiceImpl implements ResourcePropertyService {

    /** DAO */
    private ResourcePropertyDao resourcePropertyDao;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Properties getMergedProperties(String application, List<Resource> locations, List<String> noupgrade,
            boolean noDB) {

        Properties properties = new Properties();

        // prazdna konfigurace
        if (CollectionUtils.isEmpty(locations)) {
            return properties;
        }

        // boolean upgrade = true;

        // nacteni zdroju z puvodnich konfiguraci
        for (Resource location : locations) {


            Properties locationProperties = new Properties();
            try {
                String filename = location.getFilename();

                if (!CollectionUtils.isEmpty(noupgrade) && noupgrade.contains(filename)) {
                    // upgrade = false;
                }

                InputStream is = location.getInputStream();
                if (filename != null && filename.endsWith(".xml")) {
                    locationProperties.loadFromXML(is);
                } else {
                    locationProperties.load(is);
                }
            } catch (IOException e) {
                throw new BeanInitializationException("Could not load properties " + location.getDescription(), e);
            }

            // konfigurace pouze ze souboru
            if (noDB) {
                CollectionUtils.mergePropertiesIntoMap(locationProperties, properties);
                continue;
            }

            // nacteni existujicich validnich
            List<ResourceProperty> existingProperties = this.resourcePropertyDao.loadAllValid(application,
                    location.getFilename());

            // if (upgrade) {
            // // vytvoreni seznamu zmen
            // List<ResourceProperty> changedProperties = this.mergeProperties(existingProperties, locationProperties,
            // application, location.getFilename());
            //
            // // ulozit zmeny v rozdilne konfiguraci a refresh
            // if (!CollectionUtils.isEmpty(changedProperties)) {
            //
            // for (ResourceProperty changedProperty : changedProperties) {
            // this.resourcePropertyDao.saveOrUpdate(changedProperty);
            // }
            //
            // existingProperties = this.resourcePropertyDao.loadAllValid(application, location.getFilename());
            // }
            //
            // }

            CollectionUtils.mergePropertiesIntoMap(this.getProperties(existingProperties), properties);

        }

        return properties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Properties getProperties(List<ResourceProperty> validProperties) {

        Properties properties = new Properties();

        for (ResourceProperty resourceProperty : validProperties) {
            properties.setProperty(resourceProperty.getName(), StringUtils.trimToEmpty(resourceProperty.getValue()));
        }

        return properties;
    }

    /**
     * Ziska seznam sloucenych zdroju {@link ResourceProperty} se zmenou stavu pro jejich persistenci.
     *
     * @param existingProperties existujici zdroje v DB
     * @param locationProperties stavajici seznam zdroju v souboru
     * @param application nazev aplikace
     * @param filename nazev souboru
     * @return {@link List} z {@link ResourceProperty} seznam sloucenych zdroju se zmenou stavu
     */
    protected List<ResourceProperty> mergeProperties(List<ResourceProperty> existingProperties,
            Properties locationProperties, String application, String locationName) {

        List<ResourceProperty> changedProperties = new ArrayList<ResourceProperty>();

        // kontrola zaznamu v DB
        for (ResourceProperty existingProperty : existingProperties) {

            // deaktivovat existujici pokud neni v puvodni seznamu v souboru
            if (locationProperties.getProperty(existingProperty.getName()) == null) {
                existingProperty.setValidTo(new Date());
                changedProperties.add(existingProperty);
            }

            // odstranit zpracovane zaznamy
            locationProperties.remove(existingProperty.getName());
        }

        // nove zaznamy zalozit pro zbyle klice
        for (Object locationPropertyKey : locationProperties.keySet()) {
            ResourceProperty resourceProperty = new ResourceProperty();
            resourceProperty.setLocation(locationName);
            resourceProperty.setName((String) locationPropertyKey);
            resourceProperty.setApplication(application);
            resourceProperty.setValue(locationProperties.getProperty((String) locationPropertyKey));
            changedProperties.add(resourceProperty);
        }

        return changedProperties;
    }

    /**
     * Nastavuje hodnotu hodnotu atributu resourcePropertyDao.
     *
     * @param resourcePropertyDao nastavuje resourcePropertyDao
     */
    public void setResourcePropertyDao(ResourcePropertyDao resourcePropertyDao) {
        this.resourcePropertyDao = resourcePropertyDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly=true)
    public List<ResourceProperty> loadAllValid(String application, String location) {
        return this.resourcePropertyDao.loadAllValid(application, location);
	}

    // /**
    // * {@inheritDoc}
    // */
    // @Override
    // @Transactional
    // public void saveOrUpdate(ResourceProperty entity) {
    // this.resourcePropertyDao.saveOrUpdate(entity);
    // }

}
