/*
 * Copyright (c) 2014 Karumien s.r.o.
 * 
 * The contractor, Karumien s.r.o., does not take any responsibility for defects
 * arising from unauthorized changes to the source code.
 */
package cz.i24.util.resource.service;


import java.util.List;
import java.util.Properties;

import org.springframework.core.io.Resource;

import cz.i24.util.resource.entity.ResourceProperty;

/**
 * Rozhrani pro nacitani a aktualizaci nastaveni zdroju, prace s {@link ResourceProperty}.
 *
 * @author <a href="miroslav.svoboda@karumien.com">Miroslav Svoboda</a>
 * @version 1.0
 * @since 27.04.2014 10:11:12
 */
public interface ResourcePropertyService {

    /**
     * Nacte finalni verzi nastaveni dle klice a vsech pripravenych nastaveni.
     *
     * @param application
     *            oznaceni aplikace
     * @param locations
     *            seznam vsech nastaveni
     * @param noupgrade
     *            seznam nastaveni, ktere nemaji aplikovat zmeny v DB
     * @param noDB
     *            konfigurace nacitane pouze ze souboru
     * @return {@link Properties} finalni nastaveni systemu
     */
    Properties getMergedProperties(String application, List<Resource> locations, List<String> noupgrade, boolean noDB);

    /**
     * Preklad hodnot {@link ResourceProperty} na {@link Properties}.
     *
     * @param validProperties
     *            seznam hodnot zdroju
     * @return {@link Properties} prelozeny seznam
     */
    Properties getProperties(List<ResourceProperty> validProperties);

    /**
     * Nacte vsechny validni nastaveni z DB dle aplikace a zdrojove konfigurace.
     * 
     * @param application
     *            oznaceni aplikace
     * @param location
     *            konfiguracni souboru
     * @return {@link List} aktivni konfigurace
     */
    List<ResourceProperty> loadAllValid(String application, String location);

    /**
     * Nazev aplikace (klic konfigurace).
     * 
     * @return {@link String} nazev aplikace
     */
    String getApplication();

    /**
     * Vraci <code>true</code> pro lokalni nedatabazovou konfiguraci -DdevOnly
     * 
     * @return boolean <code>true</code> pro lokalni nedatabazovou konfiguraci
     */
    boolean isDev();

    // void saveOrUpdate(ResourceProperty entity);

}
