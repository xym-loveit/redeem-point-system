package com.integral.family.manage.dao;

import java.util.List;
import java.util.Map;

import com.integral.family.manage.bean.FamilyInfo;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * @author  <a href="mailto: swpigris81@126.com">代超</a>
 * @version $Revision$ 
 */
public interface IFamilyInfoDAO {
    /**
     * <p>Discription:[自定义sql查询]</p>
     * @param sql
     * @param isHql
     * @param start
     * @param limit
     * @param params
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public List<FamilyInfo> findByParams(String sql, boolean isHql, int start, int limit,
            Map<String, Object> params);
    /**
     * <p>Discription:[自定义查询语句，查询数量]</p>
     * @param sql
     * @param isHql
     * @param start
     * @param limit
     * @param params
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public int findCountByParams(String sql, boolean isHql, int start, int limit, Map<String, Object> params);
    /**
     * <p>Discription:[创建家庭信息]</p>
     * @param instance
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public void save(FamilyInfo instance);
    /**
     * <p>Discription:[新增或修改信息]</p>
     * @param instance
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public void saveOrUpdate(FamilyInfo instance);
    /**
     * <p>Discription:[批量删除家庭信息]</p>
     * @param persistentInstances
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public void deleteAll(List<FamilyInfo> persistentInstances);
    /**
     * <p>Discription:[批量修改/新增家庭信息]</p>
     * @param persistentInstances
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public void saveOrUpdateAll(List<FamilyInfo> persistentInstances);
    /**
     * <p>Discription:[通过家庭信息查询]</p>
     * @param instance
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public List<FamilyInfo> findByExample(FamilyInfo instance);
    /**
     * <p>Discription:[主键查询]</p>
     * @param id
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public FamilyInfo findById(java.lang.String id);
}
