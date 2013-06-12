/*
 * Author
 * Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 */
package lk.gov.health.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lk.gov.health.entity.CircularReplacement;

/**
 *
 * @author Dr. M H B Ariyaratne <buddhika.ari at gmail.com>
 */
@Stateless
public class CircularReplacementFacade extends AbstractFacade<CircularReplacement> {
    @PersistenceContext(unitName = "circularsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CircularReplacementFacade() {
        super(CircularReplacement.class);
    }
    
}
