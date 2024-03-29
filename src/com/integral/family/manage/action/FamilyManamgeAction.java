package com.integral.family.manage.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.integral.common.action.BaseAction;
import com.integral.family.manage.bean.FamilyInfo;
import com.integral.family.manage.service.IFamilyInfoService;
import com.integral.family.member.bean.FamilyMember;
import com.integral.family.member.service.IFamilyMemberService;
import com.integral.system.invitation.bean.SystemInviteProcess;
import com.integral.system.invitation.service.ISystemInviteProcessService;
import com.integral.system.user.service.IUserService;
import com.integral.util.ListUtils;
import com.integral.util.RequestUtil;
import com.integral.util.Tools;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * @author  <a href="mailto: swpigris81@126.com">代超</a>
 * @version $Revision$ 
 */
public class FamilyManamgeAction extends BaseAction implements ServletRequestAware, ServletResponseAware {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private IFamilyInfoService familyManageService;
    private IFamilyMemberService familyMemberService;
    private IUserService userService;
    private ISystemInviteProcessService systemInviteProcessService;
    private DataSourceTransactionManager transactionManager;
    
    private String manegerRole;
    
    private List dataList;
    
    public ISystemInviteProcessService getSystemInviteProcessService() {
        return systemInviteProcessService;
    }

    public void setSystemInviteProcessService(ISystemInviteProcessService systemInviteProcessService) {
        this.systemInviteProcessService = systemInviteProcessService;
    }

    public IUserService getUserService() {
        return userService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public String getManegerRole() {
        return manegerRole;
    }

    public void setManegerRole(String manegerRole) {
        this.manegerRole = manegerRole;
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @return IFamilyInfoService familyManageService.
     */
    public IFamilyInfoService getFamilyManageService() {
        return familyManageService;
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @param familyManageService The familyManageService to set.
     */
    public void setFamilyManageService(IFamilyInfoService familyManageService) {
        this.familyManageService = familyManageService;
    }

    public IFamilyMemberService getFamilyMemberService() {
        return familyMemberService;
    }

    public void setFamilyMemberService(IFamilyMemberService familyMemberService) {
        this.familyMemberService = familyMemberService;
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @return DataSourceTransactionManager transactionManager.
     */
    public DataSourceTransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @param transactionManager The transactionManager to set.
     */
    public void setTransactionManager(DataSourceTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    
    public List getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }
    
    public String begin(){
        return SUCCESS;
    }
    /**
     * 某用户所在家庭列表(一个用户可以加入到多个家庭)
     * <p>Discription:[方法功能中文描述]</p>
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public String familyList(){
        int start = NumberUtils.toInt(request.getParameter("start"), 0);
        int limit = NumberUtils.toInt(request.getParameter("limit"), 50);
        String userId = request.getParameter("userId");
        String viewAll = request.getParameter("viewAll");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JsonFormat jf = new JsonFormat(true);
        jf.setAutoUnicode(true);
        PrintWriter out = null;
        if(viewAll == null || "".equals(viewAll)){
            if(userId == null || "".equals(userId)){
                resultMap.put("success", false);
                resultMap.put("msg", "用户ID为空，不能查询您所在家庭信息！");
            }
        }
        try{
            out = super.getPrintWriter(request, response);
            List<FamilyInfo> list = null;
            int listSize = 0;
            if(viewAll != null && "yes".equals(viewAll)){
                list = this.familyManageService.findAllFamilyList(start, limit);
                listSize = this.familyManageService.findAllFamilyListSize();
            }else{
                list = this.familyManageService.findFamilyListByUserId(userId, start, limit);
                listSize = this.familyManageService.findFamilyListSizeByUserId(userId);
            }
            
            if(listSize < 1){
                resultMap.put("success", false);
                resultMap.put("msg", "您当前未加入任何家庭，请加入家庭或者创建家庭！");
                resultMap.put("msg1", "申请加入家庭，或者是创建家庭？");
            }else{
                resultMap.put("success", true);
                resultMap.put("familyList", list);
                resultMap.put("totalCount", listSize);
            }
        }catch(Exception e){
            resultMap.put("success", false);
            resultMap.put("msg", "系统错误！错误代码："+ e.getMessage());
            LOG.error(e.getMessage());
        }finally{
            if(out != null){
                out.print(Json.toJson(resultMap, jf));
                out.flush();
                out.close();
            }
        }
        return null;
    }
    /**
     * <p>Discription:[创建家庭]</p>
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public String familyManageAdd(){
        Map<String, Object> paramMap = RequestUtil.getRequestMap(request);
        
        // 定义TransactionDefinition并设置好事务的隔离级别和传播方式。
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        // 代价最大、可靠性最高的隔离级别，所有的事务都是按顺序一个接一个地执行
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        // 开始事务
        TransactionStatus status = transactionManager.getTransaction(definition);
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JsonFormat jf = new JsonFormat(true);
        jf.setAutoUnicode(true);
        PrintWriter out = null;
        
        try{
            out = super.getPrintWriter(request, response);
            RequestUtil.repalceAsDate(paramMap, "familyCreateDate", "yyyy-MM-dd");
            FamilyInfo family = new FamilyInfo();
            BeanUtils.populate(family, paramMap);
            String familyId = Tools.getUUID();
            if(family.getFamilyId() == null || "".equals(family.getFamilyId())){
                family.setFamilyId(familyId);
            }
            //用户自己创建的家庭，需要将该用户添加到家庭成员中
            this.familyManageService.save(family);
            
            List<FamilyMember> ml = this.familyMemberService.findByProperty("systemMemberId", family.getFamilyHouseHolder());
            FamilyMember member = new FamilyMember();
            member.setFamilyId(familyId);
            member.setSystemMemberId(family.getFamilyHouseHolder());
            if(ml != null && ml.size() > 0){
                FamilyMember mll = ml.get(0);
                member.setFamilyMemberName(mll.getFamilyMemberName());
                member.setFamilyMemberCard(mll.getFamilyMemberCard());
                member.setFamilyMemberBirthdate(mll.getFamilyMemberBirthdate());
                member.setFamilyMemberBirthplace(mll.getFamilyMemberBirthplace());
                member.setFamilyMemberSex(mll.getFamilyMemberSex());
                member.setFamilyMemberHeight(mll.getFamilyMemberHeight());
                member.setFamilyMemberEducational(mll.getFamilyMemberEducational());
                member.setFamilyMemberProfession(mll.getFamilyMemberProfession());
                member.setFamilyMemberDeaddate(mll.getFamilyMemberDeaddate());
            }
            
            this.familyMemberService.save(member);
            
            resultMap.put("success", true);
            resultMap.put("msg", "家庭信息创建成功，您已经成为：" + family.getFamilyName() + " 家庭的户主。<br><br>系统已自动将您加入到家庭成员中，请完善您的家庭成员信息！");
        }catch(Exception e){
            status.setRollbackOnly();
            resultMap.put("success", false);
            resultMap.put("msg", "家庭信息创建失败，错误代码：" + e.getMessage());
        }finally{
            transactionManager.commit(status);
            if(out != null){
                out.print(Json.toJson(resultMap, jf));
                out.flush();
                out.close();
            }
        }
        return null;
    }
    /**
     * <p>Discription:[修改家庭信息]</p>
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public String familyManageEdit(){
        Map<String, Object> paramMap = RequestUtil.getRequestMap(request);
        // 定义TransactionDefinition并设置好事务的隔离级别和传播方式。
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        // 代价最大、可靠性最高的隔离级别，所有的事务都是按顺序一个接一个地执行
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        // 开始事务
        TransactionStatus status = transactionManager.getTransaction(definition);
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JsonFormat jf = new JsonFormat(true);
        jf.setAutoUnicode(true);
        PrintWriter out = null;
        
        try{
            out = super.getPrintWriter(request, response);
            RequestUtil.repalceAsDate(paramMap, "familyCreateDate", "yyyy-MM-dd");
            FamilyInfo family = new FamilyInfo();
            BeanUtils.populate(family, paramMap);
            if(family.getFamilyId() == null || "".equals(family.getFamilyId())){
                String familyId = Tools.getUUID();
                family.setFamilyId(familyId);
            }
            this.familyManageService.saveOrUpdate(family);
            
            resultMap.put("success", true);
            resultMap.put("msg", "家庭信息更新成功！");
        }catch(Exception e){
            status.setRollbackOnly();
            resultMap.put("success", false);
            resultMap.put("msg", "家庭信息更新失败，错误代码：" + e.getMessage());
        }finally{
            transactionManager.commit(status);
            if(out != null){
                out.print(Json.toJson(resultMap, jf));
                out.flush();
                out.close();
            }
        }
        return null;
    }
    /**
     * <p>Discription:[删除家庭信息]</p>
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public String familyManageDelete(){
        String familyIds = request.getParameter("familyId");
        // 定义TransactionDefinition并设置好事务的隔离级别和传播方式。
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        // 代价最大、可靠性最高的隔离级别，所有的事务都是按顺序一个接一个地执行
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        // 开始事务
        TransactionStatus status = transactionManager.getTransaction(definition);
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JsonFormat jf = new JsonFormat(true);
        jf.setAutoUnicode(true);
        PrintWriter out = null;
        try{
            out = super.getPrintWriter(request, response);
            if(familyIds == null || "".equals(familyIds.trim())){
                resultMap.put("success", false);
                resultMap.put("msg", "未选择家庭信息或所选家庭信息无效！");
            }else{
                String [] familyId = familyIds.split(",");
                List<FamilyInfo> familyList = new ArrayList<FamilyInfo>();
                List<FamilyMember> memberList = new ArrayList<FamilyMember>();
                for(int i=0; i<familyId.length; i++){
                    FamilyInfo family = new FamilyInfo();
                    family.setFamilyId(familyId[i]);
                    familyList.add(family);
                    memberList.addAll(this.familyMemberService.findByProperty("familyId", familyId[i]));
                }
                //删除家庭之前需要将其中成员删除
                this.familyManageService.deleteAll(familyList);
                this.familyMemberService.deleteAll(memberList);
                resultMap.put("success", true);
                resultMap.put("msg", "所选家庭信息以及家庭成员已成功删除！");
            }
        }catch(Exception e){
            status.setRollbackOnly();
            resultMap.put("success", false);
            resultMap.put("msg", "系统错误！错误代码："+ e.getMessage());
        }finally{
            transactionManager.commit(status);
            if(out != null){
                out.print(Json.toJson(resultMap, jf));
                out.flush();
                out.close();
            }            
        }
        return null;
    }
    /**
     * <p>Discription:[家庭用户列表]</p>
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public String familyUserList(){
        int start = NumberUtils.toInt(request.getParameter("start"), 0);
        int limit = NumberUtils.toInt(request.getParameter("limit"), 50);
        String userId = request.getParameter("userId");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JsonFormat jf = new JsonFormat(true);
        jf.setAutoUnicode(true);
        PrintWriter out = null;
        try{
            out = super.getPrintWriter(request, response);
            int userSize = this.userService.findUserByPageCondition("From UserInfo model where model.userName != ?", -1, -1, new Object[]{userId}).size();
            //List userList = this.userService.findUserByPageWithProtect(start, limit);
            List userList = this.userService.findUserByPageCondition("From UserInfo model where model.userName != ?", start, limit, new Object[]{userId});
            resultMap.put("success", true);
            resultMap.put("userList", userList);
            resultMap.put("totalCount", userSize);
        }catch(Exception e){
            resultMap.put("success", false);
            resultMap.put("msg", "系统错误！错误代码："+e.getMessage());
            LOG.error(e.getMessage());
        }finally{
            if(out != null){
                out.print(Json.toJson(resultMap,jf));
                out.flush();
                out.close();
            }
        }
        return null;
    }
    /**
     * <p>Discription:[发出邀请加入家庭]</p>
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public String familyManageInvite(){
        String userIds = request.getParameter("userId");
        String familyIds = request.getParameter("familyId");
        String familyNames = request.getParameter("familyName");
        String sponsor = request.getParameter("sponsor");
        String menuId = request.getParameter("menuId");
        // 定义TransactionDefinition并设置好事务的隔离级别和传播方式。
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        // 代价最大、可靠性最高的隔离级别，所有的事务都是按顺序一个接一个地执行
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        // 开始事务
        TransactionStatus status = transactionManager.getTransaction(definition);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JsonFormat jf = new JsonFormat(true);
        jf.setAutoUnicode(true);
        PrintWriter out = null;
        try{
            out = super.getPrintWriter(request, response);
            if(userIds == null || "".equals(userIds.trim()) || familyIds == null || "".equals(familyIds.trim())){
                resultMap.put("success", false);
                resultMap.put("msg", "所选用户为空或者是所选家庭为空！");
            }else{
                String userId[] = userIds.split(",");
                String familyId[] = familyIds.split(",");
                String familyName[] = familyNames.split(",");
                        
                List<SystemInviteProcess> processList = new ArrayList<SystemInviteProcess>();
                
                for(int j=0; j<familyId.length; j++){
                    String family_id = familyId[j];
                    FamilyMember member = new FamilyMember();
                    member.setFamilyId(family_id);
                    member.setFamilyName(familyName[j]);
                    for(int i=0; i<userId.length;i++){
                        member.setSystemMemberId(userId[i]);
                        SystemInviteProcess process = new SystemInviteProcess();
                        process.setSponsor(sponsor);
                        process.setSponsorTime(new Date());
                        process.setRecipient(userId[i]);
                        process.setInvitationMenu(menuId);
                        //未处理
                        process.setProcessStatus("1");
                        String event = "用户 "+sponsor+" 邀请您加入他的家庭：【"+familyName[j] +"】";
                        process.setInvitationEvent(event);
                        String nextAction = "/family_manage/familyProcessInvition.action?method=familyProcessInvition";
                        process.setNextaction(nextAction);
                        
                        process.setRelationEntityName(FamilyMember.class.getName());
                        
                        JsonFormat jf1 = new JsonFormat(true);
                        process.setRelationData(Json.toJson(member, jf1));
                        processList.add(process);
                    }
                }
                
                this.systemInviteProcessService.saveOrUpdateAll(processList);
                resultMap.put("success", true);
                resultMap.put("msg", "已向所选用户发出邀请，请等待用户回应！");
            }
        }catch(Exception e){
            status.setRollbackOnly();
            resultMap.put("success", false);
            resultMap.put("msg", "系统错误！错误代码：" + e.getMessage());
            LOG.error(e.getMessage());
        }finally{
            transactionManager.commit(status);
            if(out != null){
                out.print(Json.toJson(resultMap, jf));
                out.flush();
                out.close();
            }
        }
        return null;
    }
    /**
     * <p>Discription:[加入家庭]</p>
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public String familyProcessInvition(){
        // 定义TransactionDefinition并设置好事务的隔离级别和传播方式。
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        // 代价最大、可靠性最高的隔离级别，所有的事务都是按顺序一个接一个地执行
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        // 开始事务
        TransactionStatus status = transactionManager.getTransaction(definition);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JsonFormat jf = new JsonFormat(true);
        jf.setAutoUnicode(true);
        PrintWriter out = null;
        try{
            out = super.getPrintWriter(request, response);
            //List<FamilyMember> relationDataList = (List<FamilyMember>) request.getSession().getAttribute("relationDataList");
            List<FamilyMember> relationDataList = (List<FamilyMember>) this.dataList;
            List<FamilyMember> memberList = new ArrayList<FamilyMember>();
            StringBuffer sb = new StringBuffer();
            
            if(relationDataList != null){
                //剔除重复的
                List<FamilyMember> ll = ListUtils.removeDuplication(relationDataList, "familyId", "systemMemberId");
                
                for(FamilyMember m : ll){
                    List l = this.familyMemberService.findByExample(m);
                    if(l != null && l.size() >0){
                        sb.append("您已经是【"+m.getFamilyName()+"】的成员，无需重复加入！<br><br>");
                        continue;
                    }else{
                        memberList.add(m);
                        sb.append("您已成功接受邀请，加入【"+m.getFamilyName()+"】，请立即完善您的成员信息！<br><br>");
                    }
                }
                if(memberList != null && memberList.size() >0){
                    List<FamilyMember> finalList = new ArrayList<FamilyMember>();
                    //如果用户已经是其他家庭的成员，则复制用户的信息到新的家庭
                    for(FamilyMember mem : memberList){
                        List<FamilyMember> ml = this.familyMemberService.findByProperty("systemMemberId", mem.getSystemMemberId());
                        if(ml != null && ml.size() > 0){
                            FamilyMember mll = ml.get(0);
                            mem.setFamilyMemberName(mll.getFamilyMemberName());
                            mem.setFamilyMemberCard(mll.getFamilyMemberCard());
                            mem.setFamilyMemberBirthdate(mll.getFamilyMemberBirthdate());
                            mem.setFamilyMemberBirthplace(mll.getFamilyMemberBirthplace());
                            mem.setFamilyMemberSex(mll.getFamilyMemberSex());
                            mem.setFamilyMemberHeight(mll.getFamilyMemberHeight());
                            mem.setFamilyMemberEducational(mll.getFamilyMemberEducational());
                            mem.setFamilyMemberProfession(mll.getFamilyMemberProfession());
                            mem.setFamilyMemberDeaddate(mll.getFamilyMemberDeaddate());
                            finalList.add(mem);
                        }
                    }
                    this.familyMemberService.saveOrUpdateAll(finalList);
                }
                
                resultMap.put("success", true);
                resultMap.put("msg", sb.substring(0, sb.lastIndexOf("<br><br>")).toString());
            }else{
                resultMap.put("success", false);
                resultMap.put("msg", "家庭信息不完整，无法处理！");
            }
        }catch(Exception e){
            status.setRollbackOnly();
            resultMap.put("success", false);
            resultMap.put("msg", "系统错误！错误代码："+e.getMessage());
            LOG.info(e.getMessage());
        }finally{
            this.transactionManager.commit(status);
            if(out != null){
                out.print(Json.toJson(resultMap, jf));
                out.flush();
                out.close();
            }
        }
        return null;
    }
    /**
     * <p>Discription:[申请户主]</p>
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public String familyApplyHolder(){
        String familyId = request.getParameter("familyId");
        String familyName = request.getParameter("familyName");
        String holder = request.getParameter("holder");
        String sponsor = request.getParameter("sponsor");
        String menuId = request.getParameter("menuId");
        
        // 定义TransactionDefinition并设置好事务的隔离级别和传播方式。
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        // 代价最大、可靠性最高的隔离级别，所有的事务都是按顺序一个接一个地执行
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        // 开始事务
        TransactionStatus status = transactionManager.getTransaction(definition);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JsonFormat jf = new JsonFormat(true);
        jf.setAutoUnicode(true);
        PrintWriter out = null;
        try{
            out = super.getPrintWriter(request, response);
            FamilyInfo family = new FamilyInfo();
            family.setFamilyId(familyId);
            family.setFamilyHouseHolder(sponsor);
            family.setFamilyName(familyName);
            
            SystemInviteProcess process = new SystemInviteProcess();
            process.setSponsor(sponsor);
            process.setSponsorTime(new Date());
            //接收人户主或者管理员
            List manager = this.userService.getUserByRole(this.getManegerRole());
            if(!manager.contains(holder)){
                manager.add(holder);
            }
            String recipient = StringUtils.join(manager, ",");
            
            process.setRecipient(recipient);
            
            process.setInvitationMenu(menuId);
            //未处理
            process.setProcessStatus("1");
            String event = "用户 "+sponsor+" 申请家庭：【"+ familyName +"】的户主";
            process.setInvitationEvent(event);
            String nextAction = "/family_manage/familyApplyHolderProcess.action?method=familyApplyHolderProcess";
            process.setNextaction(nextAction);
            process.setRelationEntityName(FamilyInfo.class.getName());
            JsonFormat jf1 = new JsonFormat(true);
            process.setRelationData(Json.toJson(family, jf1));
            this.systemInviteProcessService.save(process);
            resultMap.put("success", true);
            resultMap.put("msg", "已向家庭【"+familyName+"】的户主发出请求，请等待请求结果！");
        }catch(Exception e){
            LOG.error(e.getMessage());
            status.setRollbackOnly();
            resultMap.put("success", false);
            resultMap.put("msg","系统错误！错误代码："+ e.getMessage());
        }finally{
            transactionManager.commit(status);
            if(out != null){
                out.print(Json.toJson(resultMap, jf));
                out.flush();
                out.close();
            }
        }
        return null;
    }
    /**
     * <p>Discription:[处理申请户主]</p>
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public String familyApplyHolderProcess(){
        // 定义TransactionDefinition并设置好事务的隔离级别和传播方式。
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        // 代价最大、可靠性最高的隔离级别，所有的事务都是按顺序一个接一个地执行
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        // 开始事务
        TransactionStatus status = transactionManager.getTransaction(definition);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JsonFormat jf = new JsonFormat(true);
        jf.setAutoUnicode(true);
        PrintWriter out = null;
        try{
            out = super.getPrintWriter(request, response);
            List<FamilyInfo> relationDataList = (List<FamilyInfo>) request.getSession().getAttribute("relationDataList");
            List<FamilyInfo> familyList = new ArrayList<FamilyInfo>();
            StringBuffer sb = new StringBuffer();
            
            if(relationDataList != null){
                //剔除重复的
                List<FamilyInfo> ll = ListUtils.removeDuplication(relationDataList, "familyId", "familyHouseHolder");
                
                for(FamilyInfo m : ll){
                    //List l = this.familyManageService.findByExample(m);
                    Map paramMap = new HashMap();
                    paramMap.put("familyId", BeanUtils.getProperty(m, "familyId"));
                    paramMap.put("familyHouseHolder", BeanUtils.getProperty(m, "familyHouseHolder"));
                    List l = this.familyManageService.findFamilyListByParam(-1, -1, paramMap);
                    if(l != null && l.size() >0){
                        sb.append("您已经是【"+m.getFamilyName()+"】的户主，无需重复申请！<br><br>");
                        continue;
                    }else{
                        familyList.add(m);
                        sb.append("您的申请已被批准，成为【"+m.getFamilyName()+"】的户主！<br><br>");
                    }
                }
                this.familyManageService.updateAllHolder(familyList);
                resultMap.put("success", true);
                resultMap.put("msg", sb.substring(0, sb.lastIndexOf("<br><br>")).toString());
            }else{
                resultMap.put("success", false);
                resultMap.put("msg", "家庭信息不完整，无法处理！");
            }
        }catch(Exception e){
            status.setRollbackOnly();
            resultMap.put("success", false);
            resultMap.put("msg", "系统错误！错误代码："+e.getMessage());
            LOG.info(e.getMessage());
        }finally{
            this.transactionManager.commit(status);
            if(out != null){
                out.print(Json.toJson(resultMap, jf));
                out.flush();
                out.close();
            }
        }
        return null;
    }
    /**
     * <p>Discription:[变更户主]</p>
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public String familyHolderUpdate(){
        String familyId = request.getParameter("familyId");
        String newHolder = request.getParameter("newHolder");
        String userRole = request.getParameter("userRole");
        // 定义TransactionDefinition并设置好事务的隔离级别和传播方式。
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        // 代价最大、可靠性最高的隔离级别，所有的事务都是按顺序一个接一个地执行
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        // 开始事务
        TransactionStatus status = transactionManager.getTransaction(definition);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JsonFormat jf = new JsonFormat(true);
        jf.setAutoUnicode(true);
        PrintWriter out = null;
        try{
            out = super.getPrintWriter(request, response);
            if(userRole == null || "".equals(userRole) || !userRole.equals(this.manegerRole)){
                resultMap.put("success", false);
                resultMap.put("msg", "您无权更改户主信息！");
            }else{
                FamilyInfo f = this.familyManageService.findById(familyId);
                if(f.getFamilyHouseHolder().equals(newHolder)){
                    resultMap.put("success", false);
                    resultMap.put("msg", "成员【"+newHolder+"】已经是家庭【"+f.getFamilyName()+"】的户主！");
                }else{
                    FamilyInfo info = new FamilyInfo();
                    info.setFamilyHouseHolder(newHolder);
                    info.setFamilyId(familyId);
                    List<FamilyInfo> l = new ArrayList<FamilyInfo>();
                    l.add(info);
                    this.familyManageService.updateAllHolder(l);
                    resultMap.put("success", true);
                    resultMap.put("msg", "户主信息已经成功更新！");
                }
            }
        }catch(Exception e){
            LOG.error(e.getMessage());
            resultMap.put("success", false);
            resultMap.put("msg", "系统错误！错误代码："+e.getMessage());
            status.setRollbackOnly();
        }finally{
            transactionManager.commit(status);
            if(out != null){
                out.print(Json.toJson(resultMap, jf));
                out.flush();
                out.close();
            }
        }
        return null;
    }
}
