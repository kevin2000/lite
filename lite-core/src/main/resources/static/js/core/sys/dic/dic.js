 
$(function() {
	initDataTable(getOptions(),"/dic/getPageDic");
	initUrls("/dic/dicForm");
	//$("#status").selectpicker({width:"100px"});
});

function getOptions() {
	var options =  [
					/*{
						checkbox : true
					}, */
					{
						field: "code",
						title:	"Code",
						sortable:true
					},
					/*{
						field: "name",
						title:	"Name"
					},*/
					{
						field: "remark",
						title:	"Remark"
					},
					{
						field: "type",
						title:	"Type"
					},
					{
						field: "table",
						title: "Table"
					},
					{
						field: "codeFiled",
						title: "Code Field",
						visible:false
					},
					{
						field: "nameFiled",
						title: "Name Field",
						visible:false
					},
					{
						field: "filter",
						title: "Filter",
						visible:false
					},
					{
						field: "sort",
						title: "Sort",
						visible:false
					},
					{
						field : 'updateTime',
						title : 'Update Time',
						sortable:true,
						formatter:function(value, row, index){
							return timeFormat(value);
						}
					},
					{
						field: "creator",
						title: "Creator",
						visible: false
					},
					{
						field : 'createTime',
						title : 'Create&nbsp;Time',
						sortable:true,
						formatter:function(value, row, index){
							return timeFormat(value);
						},
						visible:false
					}, 
					{
						title : 'Operation',
						field : 'code',
						align : 'center', 
						formatter : function(value, row, index) {
							var f = '<a class="btn btn-info btn-xs" onclick="edit(\'' + value + '\')">Edit</a> ';
							return f ;
						}
					}];
		 
	return options;
}

