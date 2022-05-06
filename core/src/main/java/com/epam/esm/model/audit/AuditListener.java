package com.epam.esm.model.audit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AuditListener {

    private static final Logger logger = LogManager.getLogger();

    @PrePersist
    public void onPrePersist(Object o) {
        logger.log(Level.WARN, "PRE_INSERT object + " + LocalDateTime.now());
    }

    @PreUpdate
    public void onPreUpdate(Object o) {
        logger.log(Level.WARN, "PRE_UPDATE object + " +  LocalDateTime.now());
    }

    @PreRemove
    public void onPreRemove(Object o) {
        logger.log(Level.WARN, "PRE_DELETE object + " + LocalDateTime.now());
    }

}
