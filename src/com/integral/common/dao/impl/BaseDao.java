package com.integral.common.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.integral.common.dao.IBaseDao;

public class BaseDao extends HibernateDaoSupport implements IBaseDao {
    private static final Log log = LogFactory.getLog(BaseDao.class);
    protected void initDao() {
        //do nothing
    }

    @Override
    public List queryByHQL(final String hql, final Object[] params) {
        log.info("query by hql: " + hql);
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session){
                Query query = session.createQuery(hql);
                if(params!=null && params.length>0){
                    for(int i=0;i<params.length;i++){
                        query.setParameter(i, params[i]);
                    }
                }
                return query.list();
            }
        });
    }

    @Override
    public List queryBySQL(final String sql, final Object[] params) {
        log.info("query by sql: " + sql);
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session){
                Query query = session.createSQLQuery(sql);
                if(params!=null && params.length>0){
                    for(int i=0;i<params.length;i++){
                        query.setParameter(i, params[i]);
                    }
                }
                return query.list();
            }
        });
    }

    @Override
    public List queryPageByHQL(final String hql, final Object[] params, final int start, final int limit) {
        log.info("query by sql: " + hql);
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session){
                Query query = session.createQuery(hql);
                if(params!=null && params.length>0){
                    for(int i=0;i<params.length;i++){
                        query.setParameter(i, params[i]);
                    }
                }
                if(start>-1 && limit>0){
                    query.setFirstResult(start);
                    query.setMaxResults(limit);
                }
                return query.list();
            }
        });
    }

    @Override
    public List queryPageBySQL(final String sql, final Object[] params, final int start, final int limit) {
        log.info("query by sql: " + sql);
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session){
                Query query = session.createSQLQuery(sql);
                if(params!=null && params.length>0){
                    for(int i=0;i<params.length;i++){
                        query.setParameter(i, params[i]);
                    }
                }
                if(start>-1 && limit>0){
                    query.setFirstResult(start);
                    query.setMaxResults(limit);
                }
                return query.list();
            }
        });
    }
    
    public int excuteSQL(final String sql, final Object[] params) throws DataAccessResourceFailureException, HibernateException, IllegalStateException, SQLException{
        log.info("excute by sql: " + sql);
        PreparedStatement prepareStatement = getSession().connection().prepareStatement(sql);
        if(params != null){
            for(int i=0;i<params.length;i++){
                prepareStatement.setObject(i+1, params[i]);
            }
        }
        prepareStatement.execute();
        return 0;
    }
}