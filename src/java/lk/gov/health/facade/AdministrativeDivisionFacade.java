/*
 * Author
 * Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 */
package lk.gov.health.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lk.gov.health.entity.AdministrativeDivision;

/**
 *
 * @author Dr. M H B Ariyaratne <buddhika.ari at gmail.com>
 */
@Stateless
public class AdministrativeDivisionFacade extends AbstractFacade<AdministrativeDivision> {
    @PersistenceContext(unitName = "circularsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdministrativeDivisionFacade() {
        super(AdministrativeDivision.class);
    }
    
}

