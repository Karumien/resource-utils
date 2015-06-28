/*
 * Copyright (c) 2014 Karumien s.r.o.
 * 
 * The contractor, Karumien s.r.o., does not take any responsibility for defects
 * arising from unauthorized changes to the source code.
 */
package cz.i24.util.resource.dao;

import java.util.List;

import cz.i24.util.resource.entity.ResourceProperty;

/**
 * Rozhrani DAO vrstvy pro {@link ResourceProperty}.
 *
 * @author <a href="miroslav.svoboda@karumien.com">Miroslav Svoboda</a>
 * @version 1.0
 * @since 27.04.2014 10:11:12
 */
public interface ResourcePropertyDao {

    /**
     * Nacteni validniho nastaveni dle aplikace a umisteni konfigurace.
     *
     * @param application oznaceni aplikace
     * @param location umisteni konfigurace
     * @return validni nastaveni
     */
    List<ResourceProperty> loadAllValid(String application, String location);
}
