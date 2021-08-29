/**
 * depends on layer.js, bootstrap.css
 * 
 * how to use:
 * 1, config dic on dic manage page 
 * 2, reference dicControl.js
 * 3, config dic params, e.g <select id="groupCode" name="groupCode" class="condition hidden" dic-code="paramGroup" th:value="${model.groupCode}"></select>
 * 		class="condition" if it is used to list page, it is required, will be used to filter result
 * 		dic-code: code of dic, it is required
 * 		dic-default-value: default value if value is null, it is not required
 * 		multiple: single choice or multiple choice, it is not required, default is single choice 
 * @returns
 */

$(function() {
	$("select[dic-code]").each(function(index, dic) {
		initDicControl(dic);
	});
});

//init dic control and init select value or default value
function initDicControl(dic) {
	var dicControl = $(dic); 
	var id = dicControl.attr("id");
	var value = dicControl.val();
	var dicCode = dicControl.attr("dic-code");
	var defaultValue = dicControl.attr("dic-default-value");
	var optionNames = "";
	if (value != null)
		optionNames = value;
	else if (null != defaultValue)
		optionNames = defaultValue;
	var control = $('<input id="dicLabelControl' + id + '" control-reference="' + id + '" type="text" class="form-control" dic-code="paramGroup" value="' + optionNames + '" readonly="readonly">');
	control.on("click", showSelector);
	dicControl.after(control);
	if ((value != null && value.length > 0) || (null != defaultValue && typeof(exp) != "undefined")) {
		ajaxPost("/dic/getDicOptions", {"dicCode":dicCode, "optionCodes": value != null ? value : defaultValue}, function(result) {
			if (result.data && result.data.length > 0){
				var option = result.data[0];
				optionName = option.name;
				for (var i = 1; i < result.data.length; i++){
					option = result.data[0];
					optionName += "," + option.name;
				}
			}
			control.val(optionNames);
		});
	}
}

var currDicLabelControl;
var currDicCodeControl;
var dicOptionSelectorIndex;
function showSelector(event) {
	currDicLabelControl = $(event.target);
	var currDicControlId = currDicLabelControl.attr("control-reference");
	currDicCodeControl = $("#" + currDicControlId);
	var dicCode = currDicCodeControl.attr("dic-code");
	var multiple= currDicCodeControl.attr("multiple");
	var selectedCodes = currDicCodeControl.val().join(",");

	var objW = 340;
	var objH = 500;
	var controlLeft = event.pageX;
	var controlTop = event.pageY;
	var selfX = objW + controlLeft;
	var selfY = objH + controlTop;
	var bodyW = document.documentElement.clientWidth + document.documentElement.scrollLeft;
	var bodyH = document.documentElement.clientHeight + document.documentElement.scrollTop;
	var top = 0;
	var left = 0;
	if (selfX > bodyW && selfY > bodyH) {
		top = (bodyH - objH);
		left = (bodyW - objW);
	} else if (selfY > bodyH) {
		top = (bodyH - objH);
		left = controlLeft;
	} else if (selfX > bodyW) {
		top = controlTop;
		left = (bodyW - objW);
	} else {
		top = controlTop;
		left = controlLeft;
	}
	
	dicOptionSelectorIndex = layer.open({
		type : 2,
		title : false,
		closeBtn : 0, 
		shadeClose : true,
		shade : [ 0.3 ],
		area : [ objW + 'px', objH + 'px' ],
		offset:[top, left],
		anim : 2,
		content : ['/dic/dicOptionSelector?dicCode=' + dicCode + (multiple ? '&multiple=' + multiple : '') + "&selectedCodes=" + selectedCodes, 'no' ]
	});
}

function isSelected(code) {
	var exists = false;
	$.each(currDicCodeControl.find("option"), function(index, option){
		if ($(option).val() == code){
			exists = true;
			return false;
		}
	});
	return exists;
}

function selectSingle(code, name) {
	currDicLabelControl.val(name);
	currDicCodeControl.empty();
	if (code){
		currDicCodeControl.append('<option value="' + option.code + '"  selected="selected">' + option.name + '</option>');	
	}
	
	layer.close(dicOptionSelectorIndex);
}


/**
 * 
 * @param codes array. e.g ["xxx", "yyy"]
 * @param names string, separated by commas, e.g shippingCost, clearanceCost
 * @returns
 */
function selectMultiple(code, name) {
	currDicCodeControl.append('<option value="' + code + '"  selected="selected">' + name + '</option>');
	showSelectedNames();
}

function deselectMultiple(code, name) {
	currDicCodeControl.find('option[value="' + code + '"]').remove();
	showSelectedNames();
}

function showSelectedNames(){
	var names = "";
	$.each(currDicCodeControl.find("option"), function(index, option){
		names += "," + $(option).text();
	});
	if (names.length > 0){
		currDicLabelControl.val(names.substring(1));	
	} else {
		currDicLabelControl.val(names);
	}
}
