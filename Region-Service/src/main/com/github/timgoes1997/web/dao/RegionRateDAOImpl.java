package com.github.timgoes1997.web.dao;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.web.dao.interfaces.RegionRateDAO;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class RegionRateDAOImpl implements RegionRateDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(RegionRate regionRate) {
        em.persist(regionRate);
    }

    @Override
    public void edit(RegionRate regionRate) {
        em.merge(regionRate);
    }

    @Override
    public void remove(RegionRate regionRate) {
        em.remove(regionRate);
    }

    @Override
    public boolean exists(long id) {
        TypedQuery<RegionRate> query =
                em.createNamedQuery(RegionRate.FIND_ID, RegionRate.class);
        return query.setParameter("id", id).getResultList().size() > 0;
    }

    @Override
    public RegionRate find(long id) {
        TypedQuery<RegionRate> query =
                em.createNamedQuery(RegionRate.FIND_ID, RegionRate.class);
        return query.setParameter("id", id).getSingleResult();
    }

    @Override
    public List<RegionRate> findRates(Region region) {
        TypedQuery<RegionRate> query =
                em.createNamedQuery(RegionRate.FIND_BY_REGION, RegionRate.class);
        return query.setParameter("id", region.getId()).getResultList();
    }
}
