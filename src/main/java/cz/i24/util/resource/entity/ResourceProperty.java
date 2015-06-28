/*
 * Copyright (c) 2014 Karumien s.r.o.
 * 
 * The contractor, Karumien s.r.o., does not take any responsibility for defects
 * arising from unauthorized changes to the source code.
 */
package cz.i24.util.resource.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Datova reprezentace nastaveni z Properties souboru.
 *
 * @author <a href="miroslav.svoboda@karumien.com">Miroslav Svoboda</a>
 * @version 1.0
 * @since 27.04.2014 10:11:12
 */
@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "CFG_RES_PROPS")
@EqualsAndHashCode(of = "id")
public class ResourceProperty implements Serializable {

    /** Primarni klic */
    @Id
    @GeneratedValue
    private Long id;

    /** Prislusnost k systemu */
    @Column(name = "[APPLICATION]", nullable = true, length = 100)
    private String application;

    /** Nazev zdroje / souboru */
    @Column(name = "[LOCATION]", nullable = false, length = 500)
    private String location;

    /** Nazev / klic */
    @Column(name = "[NAME]", nullable = false, length = 500)
    private String name;

    /** Hodnota */
    @Column(name = "[VALUE]", nullable = true, length = 2048)
    private String value;

    /** Datum vytvoreni - validni od */
    @Temporal(TemporalType.DATE)
    @Column(name = "VALID_FROM", nullable = false)
    private Date validFrom = new Date();

    /** Datum vytvoreni - validni od */
    @Temporal(TemporalType.DATE)
    @Column(name = "VALID_TO", nullable = true)
    private Date validTo;

}
