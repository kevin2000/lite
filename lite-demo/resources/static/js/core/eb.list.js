/***
 * version 1.0.1
 */
var listUrl, formUrl, removeUrl;
var bootstrapTableOptions;
var bootstrapTableOptionColumns = [];
var dataTable;
var defaultSort;
var defaultOrder;
var defaultShowColumns = true;
var defaultShowMultiSort = true;
var defaultExportTypes = ['csv','txt', 'excel', 'json', 'sql', 'pdf'];
var postBodyEvent;
var customeQueryParamsEvent;
var customeLoadSuccessEvent;
var autoLoadTable = true;
var reloadTableAfterItemChange = true;
var customDetailFormatter = null;
/*$(function() {
	load();
});*/

function setAutoLoadTable(isLoadTable){
	this.autoLoadTable = isLoadTable;
}

function setReloadTableAfterItemChange(isReloadTableAfterItemChange){
	this.reloadTableAfterItemChange = isReloadTableAfterItemChange;
}

/**
 * 
 * @param columns 
 * @param listUrl getPageData url
 * @param sort defaultSort 
 * @param order defaultOrder (e.g. asc/desc)
 * @returns
 */
function initDataTable(columns,listUrl,sort,order, isLoadTable, isReloadTableAfterItemChange){
	bootstrapTableOptionColumns = columns;
	this.listUrl = listUrl;
	this.defaultSort = sort;
	this.defaultOrder = order;
	if (null != isLoadTable)
		this.autoLoadTable = isLoadTable;
	if (null != isReloadTableAfterItemChange)
		reloadTableAfterItemChange = isReloadTableAfterItemChange;
	if (autoLoadTable)
		loadDataTable();
}

function setDataTableProperty(showColumns, showMultiSort, exportTypes){
	defaultShowColumns = showColumns;
	defaultShowMultiSort = showMultiSort;
	if (null != exportTypes)
		defaultExportTypes = exportTypes;
}

function setCustomeQueryParamsEvent(customeQueryParamsEvent){
	this.customeQueryParamsEvent = customeQueryParamsEvent;
}
/**
 * called when table load successfully 
 * @param customeLoadSuccessEvent
 * @returns
 */
function setCustomeLoadSuccessEvent(customeLoadSuccessEvent){
	this.customeLoadSuccessEvent = customeLoadSuccessEvent;
}

/**
 * if customDetailFormatter is not null then show detail view
 * @returns
 */
function setCustomDetailFormatter(customDetailFormatter){
	this.customDetailFormatter = customDetailFormatter;
}
/**
 * 
 * @param formUrl addUrl or editUrl
 * @param removeUrl
 * @returns
 */
function initUrls(formUrl, removeUrl){
	this.formUrl = formUrl;
	this.removeUrl = removeUrl;
}
function setPostBodyEvent(postBodyEvent){
	this.postBodyEvent = postBodyEvent;
}

function refreshBootStrapTableOptions(){
	bootstrapTableOptions = {
			method : 'get', // 服务器数据的请求方式 get or post
			url : listUrl, // 服务器数据的加载地址
			// showRefresh : true,
			// showToggle : true,
			//showColumns : true,
			//iconSize : 'outline',
			toolbar : '#toolbar',
			cache:false,
			striped : false, // 设置为true会有隔行变色效果
			dataType : "json", // 服务器返回的数据类型
			pagination : true, // 设置为true会在底部显示分页条
			// queryParamsType : "limit",
			// //设置为limit则会发送符合RESTFull格式的参数
			singleSelect : false, // 设置为true将禁止多选
			contentType : "application/x-www-form-urlencoded",
			// //发送到服务器的数据编码类型
			pageSize : 50, // 如果设置了分页，每页数据条数
			pageNumber : 1, // 如果设置了分布，首页页码
			//sortable:true,
			showMultiSort:defaultShowMultiSort,
			showJumpto:true,
			sortOrder:"desc",
			// search : true, // 是否显示搜索框
			showExport:true,//show export button
			showColumns : defaultShowColumns, // 是否显示内容下拉框（选择显示的列）
			exportTypes: defaultExportTypes,
			sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者
			// "server"
			queryParams:queryParams,
			/*queryParams : function(params) {
				return {
					// 说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
					limit : params.limit,
					offset : params.offset
				// name:$('#searchName').val(),
				// username:$('#searchName').val()
				};
			},*/
			// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
			// queryParamsType = 'limit' ,返回参数必须包含
			// limit, offset, search, sort, order 否则, 需要包含:
			// pageSize, pageNumber, searchText, sortName,
			// sortOrder.
			// 返回false将会终止请求
			columns : bootstrapTableOptionColumns,
			detailView: customDetailFormatter ? true : false,
			detailFormatter:function (index, row) {
				if (null != customDetailFormatter)
					return customDetailFormatter(index, row);
				else
					return "";
			}
		};
}

function loadDataTable() {
	refreshBootStrapTableOptions();
	dataTable=$('#DataTable');
	dataTable.on("load-success.bs.table",function(e,status){
		if (status.total >= 0){
			$("#spanSuccess").text(status.total + " items found");
			$("#spanError").text("");
		}else{
			$("#spanError").text(status.msg ? status.msg : "search fail");
			$("#spanSuccess").text("");
			if (1 == status.code) {
				showLoginWindow();
			}
			//dataTable.bootstrapTable('destroy');
		}
		if (customeLoadSuccessEvent) {
			customeLoadSuccessEvent();
		}
	});
	dataTable.on("load-error.bs.table",function(e,status){
		$("#spanError").text("search fail");
		$("#spanSuccess").text("");
		//dataTable.bootstrapTable('destroy');
	});
	
	dataTable.on("post-body.bs.table",function(){
		if (postBodyEvent){		
			postBodyEvent();			
		}
		if (customDetailFormatter){
			//$("#exampleTable").bootstrapTable('expandRow', index);//
			dataTable.bootstrapTable('expandAllRows');
		}
	});
	dataTable.bootstrapTable(bootstrapTableOptions);
}

function queryParams(params) {
	var conditions={};
	if (customeQueryParamsEvent)
		conditions = customeQueryParamsEvent();
	var orders={};
	$(".condition").each(function() {
		var value = $(this).val();
		if ("checkbox" == $(this).attr("type")) {
        	if (!$(this).is(":checked")) {
        		return true;
        	} else{
        		if (null == conditions[this.name]){
        			conditions[this.name]= new Array();
        		}
        		conditions[this.name].push(value);
        	}
        } else {
            if (!this.name || !value || ($.isArray(value) && value.length < 1)) return true;
            if (!$.isArray(value))
            	conditions[this.name]=$.trim(value);
            else
            	conditions[this.name]=value;
        }
	});
	
	if (params.sort && params.sort != "") {
		orders[params.sort] = params.order;
	} else if(params.multiSort && params.multiSort.length > 0) {
		$.each(params.multiSort,function(index,value){
			orders[value.sortName] = value.sortOrder;
		});
	} else if(defaultSort && defaultOrder) {
		orders[defaultSort] = defaultOrder;
	}
	
	params.conditions = JSON.stringify(conditions);
	params.orders =JSON.stringify(orders);
	
	return params;
}

function reLoadDataTable() {
	if (dataTable == null)
		loadDataTable();
	else {
		dataTable.bootstrapTable('refresh',bootstrapTableOptions);
	}
}

function updateRow(dataIndex, row){
	dataTable.bootstrapTable('updateRow', {index: dataIndex, row: row});
}

function itemChangeHandler(){
	if (reloadTableAfterItemChange)
		reLoadDataTable();
}

function search(){
	reLoadDataTable();
}
function add(title) {
	layer.open({
		type : 2,
		title : title ? title : 'New',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '90%', '90%' ],
		content : formUrl // iframe的url
	});
}
function edit(id, title) {
	layer.open({
		type : 2,
		title : title ? title : 'Edit',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '90%', '90%' ],
		content : formUrl + '?id=' + encodeURIComponent(id) // iframe的url
	});
}
function remove(id) {
	layer.confirm('Are you sure you want to remove the selected record?', {
		btn : [ 'OK', 'Cancel' ]
	}, function() {
		$.ajax({
			url : removeUrl,
			type : "post",
			data : {
				'id' : id
			},
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					reLoadDataTable();
				} else {
					layer.msg(r.msg);
				}
			},
			error:function(e){
				layer.msg("remove fail");
		    }
		});
	})
}

function preview(url) {
	window.open(url);    
}

function reset(){
	var url = window.location.href;
	var signIndex = url.indexOf("?"); 
	if (signIndex > 0){
		url = url.substring(0, signIndex);
		window.location.href = url;
	}else
		window.location.reload();
}
