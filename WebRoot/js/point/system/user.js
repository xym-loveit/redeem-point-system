/**
 * 用户管理
 */
function userManage(){
	/**
	 * userReader - 用户信息解析器
	 */
	var userReader = new Ext.data.JsonReader({
		totalProperty : "totalCount",
		root : "userList"
	},[
		{name:"userId"},//唯一id
		{name:"userCode"},//用户编号
		{name:"userName"},//用户名
		{name:"telphoneNo"},//电话
		{name:"phoneNo"},//手机
		{name:"privence"},//省
		{name:"city"},//城市
		{name:"address"},//地址
		{name:"zip"},//邮编
		{name:"email"}//电子邮件
	]);
	
	var proxyUrl = path+"/user/userList.action?method=userManageList";
	/**
	 * userStore:用户数据仓库
	 */
	var userStore = new Ext.data.Store({
		proxy:new Ext.data.HttpProxy({
			url:proxyUrl
		}),
		reader:userReader
	});
	
	/**
	 * userSM:数据展现样式
	 */
	var userSM = new Ext.grid.CheckboxSelectionModel();
	/**
	 * userCM:数据列展示样式
	 */
	var userCM = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),userSM,{
		dataIndex:"userId",
		hidden:true,
		hideable:false//不允许将隐藏的字段显示出来
	},{
		header:"用户名称",
		dataIndex:"userName",
		width:150
	},{
		header:"电话号码",
		dataIndex:"telphoneNo",
		width:150
	},{
		header:"手机号码",
		dataIndex:"phoneNo",
		width:180
	},{
		header:"省",
		dataIndex:"privence",
		sortable:true,
		width:80
	},{
		header:"市",
		dataIndex:"city",
		width:130
	},{
		header:"具体地址",
		dataIndex:"address",
		width:130
	},{
		header:"邮编号码",
		dataIndex:"zip",
		width:130
	},{
		header:"电子邮件",
		dataIndex:"email",
		width:130
	}]);
	
	/**
	 * userGrid: 菜单展示列表
	 */
	var userGrid = new Ext.grid.GridPanel({
		collapsible:true,//是否可以展开
		animCollapse:true,//展开时是否有动画效果
		autoScroll:true,
		width:Ext.get("user_div").getWidth(),
		height:Ext.get("user_div").getHeight()-20,
		loadMask:true,//载入遮罩动画（默认）
		frame:true,
		autoShow:true,		
		store:userStore,
		renderTo:user_div,
		cm:userCM,
		sm:userSM,
		viewConfig:{forceFit:true},//若父容器的layout为fit，那么强制本grid充满该父容器
		split: true,
		bbar:new Ext.PagingToolbar({
			pageSize:50,//每页显示数
			store:userStore,
			displayInfo:true,
			displayMsg:"显示{0}-{1}条记录，共{2}条记录",
			nextText:"下一页",
			prevText:"上一页",
			emptyMsg:"无相关记录"
		}),
		tbar:[]
	});
	
	/**
	 * 按钮存储器，尚未执行查询
	 */
	var buttonRightStore = buttonRight();
	/**
	 * 执行权限按钮加载, 并且加载列表数据, 显示权限按钮
	 * see buttonRight.js
	 */
	loadButtonRight(buttonRightStore, userStore, userGrid, "user_div");
	/**
	 * 添加用户信息
	 * @param {} url
	 */
	this.addUser = function(url){
		var userForm = showUserForm(url, false, false);
		var button = [{
			text:"保存",
			handler:function(){
				if(userForm.form.isValid()){
					saveUser("addUserWindow", userForm);
				}
			}
		},{
			text:"关闭窗口",
			handler:function(){
				var userWindow = Ext.getCmp("addUserWindow");
				if(userWindow){
					userWindow.close();
				}
			}
		}];
		showUserWindow("addUserWindow", "添加用户信息",500, 320, userForm, button);
	}
	/**
	 * 编辑用户信息
	 * @param {} url
	 */
	this.editUser = function(url){
		var gridSelectionModel = userGrid.getSelectionModel();
		var gridSelection = gridSelectionModel.getSelections();
		if(gridSelection.length != 1){
			Ext.MessageBox.alert('提示','请选择一条用户信息！');
		    return false;
		}
		
		var userForm = showUserForm(url, false, true);
		var button = [{
			text:"保存",
			handler:function(){
				if(userForm.form.isValid()){
					saveUser("editUserWindow", userForm);
				}
			}
		},{
			text:"关闭窗口",
			handler:function(){
				var userWindow = Ext.getCmp("editUserWindow");
				if(userWindow){
					userWindow.close();
				}
			}
		}];
		showUserWindow("editUserWindow", "修改用户信息",500, 320, userForm, button);
		userForm.getForm().loadRecord(gridSelection[0]);
	}
	/**
	 * 删除用户信息
	 * @param {} url
	 */
	this.deleteUser = function(url){
		var gridSelectionModel = userGrid.getSelectionModel();
		var gridSelection = gridSelectionModel.getSelections();
		if(gridSelection.length < 1){
			Ext.MessageBox.alert('提示','请至少选择一条用户信息！');
		    return false;
		}
	}
	
	/**
	 * 窗口, 用于新增，修改
	 * @param {} id 窗口ID
	 * @param {} title 窗口名字
	 * @param {} width 窗口宽度
	 * @param {} height 窗口高度
	 * @param {} items 窗口的内部
	 * @param {} buttons 窗口的按钮
	 */
	function showUserWindow(id, title, width, height, items, buttons){
		var userWindow = new Ext.Window({
			id:id,
			title:title,
			width:width,
			height:height,
			items:items,
			buttons:buttons,
			modal:true,
			layout:"fit",
			resizable:false
		});
		userWindow.show();
	}
	/**
	 * 表单信息
	 * @param {} url
	 * @param {} isNull
	 * @return {}
	 */
	function showUserForm(url,isNull,readOnly){
		var userForm = new Ext.form.FormPanel({
			frame: true,
			labelAlign: 'right',
			labelWidth:60,
			autoScroll:false,
			waitMsgTarget:true,
			url:url,
			items:[{
				layout:"column",
				border:false,
				labelSeparator:'：',
				items:[{
					layout:"form",
					columnWidth:.5,
					height:50,
					items:[{
						xtype: 'textfield',
						name:"userName",
						anchor:"90%",
						fieldLabel:"用户名",
						maxLength:50,
						readOnly:readOnly,
						allowBlank:isNull
					}]
				},{
					layout:"form",
					columnWidth:.5,
					height:50,
					items:[{
						xtype: 'textfield',
						name:"telphoneNo",
						anchor:"90%",
						fieldLabel:"电话号码",
						maxLength:50,
						allowBlank:isNull
					}]
				}]
			},{
				layout:"column",
				border:false,
				labelSeparator:'：',
				items:[{
					layout:"form",
					columnWidth:.5,
					height:50,
					items:[{
						xtype: 'textfield',
						name:"phoneNo",
						anchor:"90%",
						fieldLabel:"手机号码",
						maxLength:50
					}]
				}]
			},{
				layout:"column",
				border:false,
				labelSeparator:'：',
				items:[{
					layout:"form",
					columnWidth:.5,
					height:50,
					items:[{
						xtype: 'textfield',
						name:"privence",
						anchor:"90%",
						fieldLabel:"省/直辖市",
						maxLength:50
					}]
				},{
					layout:"form",
					columnWidth:.5,
					height:50,
					items:[{
						xtype: 'textfield',
						name:"city",
						anchor:"90%",
						fieldLabel:"市/县",
						maxLength:50
					}]
				}]
			},{
				layout:"column",
				border:false,
				labelSeparator:'：',
				items:[{
					layout:"form",
					columnWidth:.9,
					height:50,
					items:[{
						xtype: 'textfield',
						name:"address",
						anchor:"90%",
						fieldLabel:"地址",
						maxLength:500
					},{
						xtype:"hidden",
						name:"userId"
					},{
						xtype:"hidden",
						name:"userCode"
					}]
				}]
			},{
				layout:"column",
				border:false,
				labelSeparator:'：',
				items:[{
					layout:"form",
					columnWidth:.5,
					height:50,
					items:[{
						xtype: 'textfield',
						name:"zip",
						anchor:"90%",
						fieldLabel:"邮编",
						maxLength:50
					}]
				},{
					layout:"form",
					columnWidth:.5,
					height:50,
					items:[{
						xtype: 'textfield',
						name:"email",
						anchor:"90%",
						fieldLabel:"电子邮件",
						maxLength:50
					}]
				}]
			}]
		});
		return userForm;
	}
	
	/**
	 * 保存用户信息
	 * @param {} windowId
	 * @param {} form
	 */
	function saveUser(windowId, form){
		Ext.MessageBox.show({
			msg:"正在保存用户信息，请稍候...",
			progressText:"正在保存用户信息，请稍候...",
			width:300,
			wait:true,
			waitConfig: {interval:200},
			icon:Ext.Msg.INFO
		});
		form.getForm().submit({
			success: function(form, action) {
				Ext.Msg.hide();
				Ext.Msg.alert('系统提示信息', '用户信息保存成功!', function(btn, text) {
					if (btn == 'ok') {
						var msg = Ext.decode(action.response.responseText);
						userStore.reload();
						Ext.getCmp(windowId).close();
					}
				});
			},
			failure: function(form, action) {//action.result.errorMessage
				Ext.Msg.hide();
				Ext.Msg.alert('系统提示信息', "用户信息保存过程中出现异常!");
			}
		});
	}
}
/**
 * 用户管理
 */
Ext.onReady(function(){
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'under';
	userManage();
});