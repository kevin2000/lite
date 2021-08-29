$(function() {
	isMultiple = "multiple" == $("#multiple").val();

	customClickRow = function(row, element) {
		var codeElement = element.find("span.code");
		if (codeElement != null){
			if (codeElement.hasClass("hidden")){
				select(row.code, row.name);
				codeElement.removeClass("hidden");
			} else {
				deselect(row.code, row.name);
				codeElement.addClass("hidden");
			}
		}
	};
	
	$('#searchValue').bind('keydown', function (event) {
	    var event = window.event || arguments.callee.caller.arguments[0];
	    if (event.keyCode == 13){
	       $("#btnSearch").click();
	    }
	});

	initDataTable(getOptions(), "/dic/getPageOption");
	// $("#status").selectpicker({width:"100px"});
	/*if (selectedOptions != null) {
		$.each(selectedOptions, function(index, dicOption){
			$("#selectedOptions").append('<label class="checkbox-inline">  <input type="checkbox" value="' + dicOption.code + '"> ' + dicOption.name + ' </label>');
		});
	}*/
	
});

var isMultiple = false;
function getOptions() {
	var options = [
			{
				checkbox : true,
				visible : false
			},
			{
				field : "code",
				title : "Code",
				visible : false
			},
			{
				field : "name",
				title : "Name"
			},
			{
				title : 'select',
				field : 'code',
				align : 'center',
				formatter : function(value, row, index) {
					if (parent.isSelected(value)) {
						return '<span class="code glyphicon glyphicon-ok"></span>';
					} else {
						return '<span class="code glyphicon glyphicon-ok hidden"></span>';	
					}
					
					//return '<a class="btn btn-info btn-xs" onclick="select(\'' + value + '\', \'' + row.name + '\')">Select</a> ';
				}
			} ];
	return options;
}

/*function isSelected(code) {
	if (selectedOptions != null){
		var isSelected = false;
		$.each(selectedOptions, function(index, option){
			if (option.code == code){
				isSelected = true;
				return false;
			}
		});
		return isSelected;
	} else {
		return false;
	}
}
*/
function deselect(code, name) {
	if (isMultiple) {
		parent.deselectMultiple(code, name);
	} else {
		// single
		//parent.selectSingle(code, name);
	}
	/*$.each(selectedOptions, function(index, option){
		if (option.code == code){
			selectedOptions.splice(index, 1);
			return false;
		}
	});
	parent.select();*/
	/*if (selectedOptions.length > 0){
		var option = selectedOptions[0];
		var codes = option.code, names = option.name;
		for (var i = 1; i < selectedOptions.length; i++){
			option = selectedOptions[i];
			codes += "," + option.code;
			names += "," + option.name;
		}
		parent.selectMultiple(codes, names);	
	} else{
		parent.selectMultiple();
	}*/
}

function select(code, name) {
	if (isMultiple) {
		// multiple
		/*var codes = code, names = name;
		if (null == selectedOptions){
			selectedOptions = [];
		} else {
			$.each(selectedOptions, function(index, option){
				codes += "," + option.code;
				names += "," + option.name;
			});		
		}
		var option = {"code":code, "name":name};
		selectedOptions.push(option);
		
		parent.selectMultiple(selectedOptions);*/
		parent.selectMultiple(code, name);
	} else {
		// single
		parent.selectSingle(code, name);
	}
}

function deselectAll() {
	parent.selectSingle();
}

