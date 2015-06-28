/*
 * Copyright (c) 2014 Karumien s.r.o.
 * 
 * The contractor, Karumien s.r.o., does not take any responsibility for defects
 * arising from unauthorized changes to the source code.
 */
package cz.i24.util.resource.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import cz.i24.util.resource.entity.ResourceProperty;

/**
 * Implementace pro {@link ResourcePropertyDao}.
 *
 * @author <a href="miroslav.svoboda@karumien.com">Miroslav Svoboda</a>
 * @version 1.0
 * @since 27.04.2014 10:11:12
 */
public class ResourcePropertyDaoImpl extends JdbcDaoSupport implements ResourcePropertyDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResourceProperty> loadAllValid(String application, String location) {

        List<Map<String, Object>> rows = this.getJdbcTemplate().queryForList(
                        "SELECT * FROM CFG_RES_PROPS WHERE (APPLICATION = ? AND APPLICATION IS NOT NULL OR APPLICATION IS NULL) AND VALID_TO IS NOT NULL",
                        application);
        List<ResourceProperty> resourceProperties = new ArrayList<ResourceProperty>();

        for (Map<String, Object> row : rows) {
            ResourceProperty res = new ResourceProperty();
            res.setId((Long) row.get("ID"));
            res.setName((String) row.get("NAME"));
            res.setApplication((String) row.get("APPLICATION"));
            res.setLocation((String) row.get("LOCATION"));
            // res.setValidFrom();
            // res.setValidTo();
            resourceProperties.add(res);
        }


        // Conjunction conjunction = new Conjunction();
        // if(StringUtils.isNotEmpty(application)) {
        // conjunction.add(Restrictions.eq("application", application));
        // }
        // if(StringUtils.isNotEmpty(location)) {
        // conjunction.add(Restrictions.eq("location", location));
        // }
        // conjunction.add(Restrictions.isNull("validTo"));

        return resourceProperties;
    }
    
}
