/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lk.gov.health.entity.SingleKeyWord;

/**
 *
 * @author Neo
 */
@Stateless
public class SingleKeyWordFacade extends AbstractFacade<SingleKeyWord> {
    @PersistenceContext(unitName = "circularsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SingleKeyWordFacade() {
        super(SingleKeyWord.class);
    }
    
}
