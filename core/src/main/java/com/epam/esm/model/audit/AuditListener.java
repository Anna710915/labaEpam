package com.epam.esm.model.audit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

/**
 * The type Audit listener. The listener class which looks for save, update, delete and load
 * operations with entities.
 * @author Anna Merkul
 */
public class AuditListener {

    private static final Logger logger = LogManager.getLogger();

    /**
     * On pre persist.
     *
     * @param o the o
     */
    @PrePersist
    public void onPrePersist(Object o) {
        logger.log(Level.WARN, "PRE_INSERT object + " + LocalDateTime.now());
    }

    /**
     * On pre update.
     *
     * @param o the o
     */
    @PreUpdate
    public void onPreUpdate(Object o) {
        logger.log(Level.WARN, "PRE_UPDATE object + " +  LocalDateTime.now());
    }

    /**
     * On pre remove.
     *
     * @param o the o
     */
    @PreRemove
    public void onPreRemove(Object o) {
        logger.log(Level.WARN, "PRE_DELETE object + " + LocalDateTime.now());
    }

}
