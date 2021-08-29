/***
 * version 1.0.2
 */
// Submit form
function ezSubmitForm(formName) {
	document.forms[formName].submit();
	return true;
}

// Submit form to new target
function ezSubmitFormOther(formName, target, action) {
	var form = document.forms[formName];
	if (!form)
		return;
	if (target)
		form.setAttribute("target", target); 
	if (action)
		form.setAttribute("action", action);
	form.submit();
	return true;
}

// Offset for nav menu
function _ezOffsetForMenu() {
    $('body').css('padding-top', $('#navMenu').height() + 'px' );
}
function ezOffsetForMenu() {
	$(window).resize(_ezOffsetForMenu);
	$(document).ready(_ezOffsetForMenu);
}

// Load tool tip
function ezLoadTooltip() {
	$(function() {
	    $('[data-toggle="tooltip"]').tooltip();
	});
}

// Check or uncheck all child checkbox
function clickCheckboxParent(parentId, childName) {
	var checked = document.getElementById(parentId).checked;
	var children = document.getElementsByName(childName);
	for (var i=0; i<children.length; i++) {
		$(children[i]).prop('checked',checked).trigger('change');
		//children[i].checked = checked;
	}
}

// Join and submit all checked checkboxes value to url 
function submitCheckboxes(childName, urlPrefix) {
	var url = urlPrefix;
	var children = document.getElementsByName(childName);
	var empty = true;
	for (var i=0; i<children.length; i++) {
		if (children[i].checked) {
			url += (children[i].value + ',');
			empty = false;
		}
	}
	if (!empty)
		window.open(url);
}

//Move an item before or after
function moveHtmlItem(item, beforeOrAfter, sameChildrenCount, sameChildren2Count) {
	var sibling = beforeOrAfter ? item.previousElementSibling : item.nextElementSibling;
	var parent = item.parentNode;
	if (!sibling || !parent)
		return false;
	var childrenCnt = sameChildrenCount ? item.childNodes.length : null;
	var children2Cnt = sameChildrenCount && sameChildren2Count && childrenCnt ? item.childNodes[0].childNodes.length : null;
	if (childrenCnt != null && sibling.childNodes.length != childrenCnt)
		return false;
	if (children2Cnt != null && sibling.childNodes[0].childNodes.length != children2Cnt)
		return false;
	if (beforeOrAfter)
		parent.insertBefore(item, sibling);
	else
		parent.insertBefore(sibling, item);
	return true;
}

// Add row above the cell, and copy "rowContentId".innerHTML to the first cell
// And divided by "divideColIds", copy "cellContentId".innerHTML to the rest cells
function tableAddRow(cell, rowContentId, divideColIds, cellContentIds, randomize) {
	var row = cell.parentNode;
	var tbodyOrThead = row.parentNode;
	var columnCnt = row.cells.length;
	var newRow = tbodyOrThead.insertRow(row.rowIndex);
	
	var divideCols = new Array();
	if (divideColIds) {
		for (var i=0; i<divideColIds.length; i++) {
			var tmp = document.getElementById(divideColIds[i]);
			if (tmp && tmp.parentNode && tmp.parentNode.cellIndex)
				divideCols.push(tmp.parentNode.cellIndex);
		}
	}

	var cell;
	var divideIdx = 0;
	var rowContent = document.getElementById(rowContentId);
	var cellContent = document.getElementById(cellContentIds[divideIdx]);
	for (var i=0; i<columnCnt; i++) {
		cell = newRow.insertCell(i);
		if (i == 0) {
			if (rowContent)
				cell.innerHTML = rowContent.innerHTML;
		} else if (divideIdx < divideCols.length && divideCols[divideIdx] == i) {
			divideIdx++;
			cellContent = divideIdx < cellContentIds.length ? document.getElementById(cellContentIds[divideIdx]) : null;
		} else {
			if (cellContent)
				cell.innerHTML = cellContent.innerHTML;
		}
	}
	
	if (randomize) {
		var rand = 'rand' + Math.floor(Math.random() * 10000000);
		newRow.innerHTML = newRow.innerHTML.replace(/_RANDOMIZE_/g, rand);
	}
}

// Delete row at the cell
function tableDeleteRow(cell) {
	var row = cell.parentNode;
	var tbodyOrThead = row.parentNode;
	tbodyOrThead.deleteRow(row.rowIndex);
}

// Move the row at the cell up or down
function tableMoveRow(cell, upOrDown, sameChildrenCnt) {
	moveHtmlItem(cell.parentNode, upOrDown, true, sameChildrenCnt);
}

// Add column to the left of the cell, and copy "colContentId".innerHTML to the first cell, copy "cellContentId".innerHTML to the rest cells //TODO no thead
function tableAddColumn(cell, colContentId, cellContentId, avoidRowIds) {
	var row = cell.parentNode;
	var tbodyOrThead = row.parentNode;
	var cellIdx = cell.cellIndex;
	var cell;
	var colContent = document.getElementById(colContentId);
	var cellContent = document.getElementById(cellContentId);
	var avoidRows = new Array();
	if (avoidRowIds) {
		for (var i=0; i<avoidRowIds.length; i++) {
			var tmp = document.getElementById(avoidRowIds[i]);
			if (tmp && tmp.parentNode && tmp.parentNode && tmp.parentNode.parentNode && tmp.parentNode.parentNode.rowIndex)
				avoidRows.push(tmp.parentNode.parentNode.rowIndex);
		}
	}
	
	for (var i=0; i<tbodyOrThead.rows.length; i++) {
		row = tbodyOrThead.rows[i];
		cell = row.insertCell(cellIdx);
		if (i == 0) {
			if (colContent)
				cell.innerHTML = colContent.innerHTML;
		} else if (avoidRows.indexOf(i) < 0) {
			if (cellContent)
				cell.innerHTML = cellContent.innerHTML;
		}
	}
}

// Delete column at the cell //TODO no thead
function tableDeleteColumn(cell) {
	var row = cell.parentNode;
	var tbodyOrThead = row.parentNode;
	var cellIdx = cell.cellIndex;
	for (var i=0; i<tbodyOrThead.rows.length; i++) {
		row = tbodyOrThead.rows[i];
		row.deleteCell(cellIdx);
	}
}

// Move the column at the cell left or right //TODO no thead
function tableMoveColumn(cell, leftOrRight, sameChildrenCnt) {
	var row = cell.parentNode;
	var tbodyOrThead = row.parentNode;
	var cellIdx = cell.cellIndex;
	for (var i=0; i<tbodyOrThead.rows.length; i++) {
		row = tbodyOrThead.rows[i];
		if (i == 0) {
			if (!moveHtmlItem(row.childNodes[cellIdx], leftOrRight, sameChildrenCnt, false))
				break;
		} else {
			moveHtmlItem(row.childNodes[cellIdx], leftOrRight, false, false);
		}
	}
}

// Get all table content as encoded string, ignoring the encColCnt columns //TODO no thead
function tableAllContent(tableId, endColCnt) {
	var table = document.getElementById(tableId);
	var tbodyOrThead = table.childNodes[0];
	var row;
	var cell;
	var tag;
	var rowStrs = new Array();
	if (!endColCnt)
		endColCnt = 0;

	for (var i=0; i<tbodyOrThead.rows.length; i++) {
		row = tbodyOrThead.rows[i];
		var cellStrs = new Array();
		
		for (var k=0; k<row.cells.length - endColCnt; k++) {
			cell = row.cells[k];
			var tagStrs = new Array();
			
			for (var p=0; p<cell.childNodes.length; p++) {
				tag = cell.childNodes[p];
				if (tag && tag.tagName) {
					if (tag.tagName.toLowerCase() == 'input' && tag.type == 'text' || tag.tagName.toLowerCase() == 'textarea')
						tagStrs.push(tag.value);
					else if (tag.tagName.toLowerCase() == 'input' && tag.type == 'radio')
						tagStrs.push(tag.checked);
				}
			}
			
			cellStrs.push(tagStrs.join('$$%$'));
		}
		
		rowStrs.push(cellStrs.join('##%#'));
	}
	
	return encodeURIComponent(rowStrs.join('@@%@'));
}

// Show tooltip when hover on a cell
function tableHoverCell(obj) {
	var cell = obj.parentNode;
	var row = cell.parentNode;
	var tbodyOrThead = row.parentNode;
	var cellIdx = cell.cellIndex;
	obj.setAttribute('title', '' + row.childNodes[0].childNodes[6].value + ' / ' + tbodyOrThead.rows[0].childNodes[cellIdx].childNodes[6].value);
}

//Keep 2 decimal places e.g. get 2.00 from 2
function roundDecimal(x) {
      var f = parseFloat(x); 
      if (isNaN(f)) { 
        return ""; 
      } 
      var f = Math.round(x*100)/100; 
      var s = f.toString(); 
      var rs = s.indexOf('.'); 
      if (rs < 0) { 
        rs = s.length; 
        s += '.'; 
      } 
      while (s.length <= rs + 2) { 
        s += '0'; 
      } 
      return s.replace(/\.00/g, ""); 
}
/**
 * It's called like this: StringFormat("&Type={0}&Ro={1}&lPlan={2}&Plan={3}&={4}&Id={5}&Id={6}", data1, data2, data3,data4, data5,data6,data7)
 * @returns
 */
function stringFormat() {
	 if (arguments.length == 0)
		 return null;
	 var str = arguments[0];
	 for (var i = 1; i < arguments.length; i++) {
		 var re = new RegExp('\\{' + (i - 1) + '\\}', 'gm');
		 str = str.replace(re, arguments[i]);
	 }
	 return str;
}

/**
 * format for IST, no display time zone
 * e.g. 2019-01-24
 * @param cellvalue
 * @returns
 */
function dateFormatByISTNoTimeZone(cellvalue){
	return dateFormat(cellvalue, 330, null);
}

/**
 * e.g. 2019-01-24 IST
 * @param cellvalue
 * @param timeZoneOffset default 330 = 5.5 * 60 = india timezone
 * @param timeZoneOffset default IST = india timezone
 * @returns
 */
function dateFormat(cellvalue, timeZoneOffset, timeZone) {
	if (cellvalue && "null" != cellvalue){
		try {
			if (!timeZoneOffset){
				timeZoneOffset = 330;
				timeZone = "IST";
			}
			var oDate = transferTimeByTimeZone(new Date(cellvalue), timeZoneOffset), oYear = oDate.getFullYear(), oMonth = oDate
					.getMonth() + 1, oDay = oDate.getDate(), oTime = oYear + '-' + padLefByZero(oMonth) + '-'
					+ padLefByZero(oDay);
			return oTime + (timeZone ? (" " + timeZone) : "");
		} catch (err) {
			console.log(err.message);
		}
	}
	return "";
}

/**
 * 
 * @param cellvalue
 * @param timeZoneOffset default 330 = 5.5 * 60 = india timezone
 * @param timeZoneOffset default IST = india timezone
 * @returns
 */
function timeFormat(cellvalue, timeZoneOffset, timeZone) {
	if (cellvalue && "null" != cellvalue){
		try {
			if (!timeZoneOffset){
				timeZoneOffset = 330;
				timeZone = "IST";
			}
			var oDate = transferTimeByTimeZone(new Date(cellvalue), timeZoneOffset), oYear = oDate.getFullYear(), oMonth = oDate
					.getMonth() + 1, oDay = oDate.getDate(), oHour = oDate
					.getHours(), oMin = oDate.getMinutes(), oSen = oDate
					.getSeconds(), oTime = oYear + '-' + padLefByZero(oMonth) + '-'
					+ padLefByZero(oDay) + ' ' + padLefByZero(oHour) + ':' + padLefByZero(oMin) + ':'
					+ padLefByZero(oSen);
			return oTime + (timeZone ? (" " + timeZone) : "");
		} catch (err) {
			console.log(err.message);
		}
	}
	return null;
}


/**
 * transfer time by timezone
 * @param time Date
 * @param timeZoneOffset unit minutes  e.g. GM+8 = 480
 * @returns
 */
function transferTimeByTimeZone(time, timeZoneOffset){
	if (timeZoneOffset == time.getTimezoneOffset())
		return time;
	var localTime = time.getTime();
    var localOffset= time.getTimezoneOffset() * 60000;
    var utcTime = localTime + localOffset;
	var wishTime= utcTime + (60000 * timeZoneOffset);
	return new Date(wishTime); 
}
/**
 * yyyy-M-d HH:mm
 */
function shortTimeFormat(cellvalue) {
	if (cellvalue && "null" != cellvalue){
		try {
			var oDate = new Date(cellvalue), oYear = oDate.getFullYear(), oMonth = oDate
					.getMonth() + 1, oDay = oDate.getDate(), oHour = oDate
					.getHours(), oMin = oDate.getMinutes(), oSen = oDate
					.getSeconds(), oTime = oYear + '-' + oMonth + '-'
					+ oDay + ' ' + padLefByZero(oHour) + ':' + padLefByZero(oMin);
			return oTime;
		} catch (err) {
			console.log(err.message);
		}
	}
	return null;
}

/**
 * set the select box to read-only
 * how to call it:
 * 1, set readonly attribute in select, e.g. <select id="type" name="type" class="form-control" readonly="readyonly">
 * 2, call readononlySelectHanddler like this readononlySelectHanddler($("select"))
 * 3, if you want remove readonly, you only need remove readonly attr from select
 */
function readononlySelectHanddler(source){
	if (source){
		source.focus(function(e){
			this.defaultValue = $(e.target).val();
		});
		source.change(function(e){
			if ("readonly" == $(e.target).attr("readonly"))
				$(e.target).val(this.defaultValue);
		});
		//onfocus="this.defaultIndex=this.selectedIndex;" onchange="this.selectedIndex=this.defaultIndex;"
	}
}

/**
 * 
 * @param action
 * @param data e.g. [{"name":"type","value":"Asin"},{"name":"keys","value":"abc,afdsd,"}]
 * @returns
 */
function downloadFileWithPost(action,datas,target) {
	$("#downloadform").remove();
	var form = $("<form>");
	form.attr("id", "downloadform");
	form.attr("style", "display:none");
	if (target)
		form.attr("target", target);
	form.attr("method", "post");
	form.attr("action", action);
	$.each(datas, function(index, data){
		var input1 = $("<input>");
		input1.attr("type", "hidden");
		input1.attr("name", data.name);
		input1.attr("value", data.value);
		form.append(input1);
	});
	
	$("body").append(form);

	form.submit();//表单提交 
}

function padLefByZero(num){  
    if(parseInt(num) < 10){  
        num = '0'+num;  
     }  
     return num;  
}
//Capitalize the first letter of each word
function titleUpperCase(text){
	var i, ss = text.toLowerCase().split(/\s+/);  
    for (i = 0; i < ss.length; i++) {  
        ss[i] = ss[i].slice(0, 1).toUpperCase() + ss[i].slice(1);  
    }  
    return ss.join(' ');
}
/**
 * find out the index of element in arr
 */
function arraySearch(arr, element) {
	if (arr) {
		for (var i = 0; i < arr.length; i++) {
			if (arr[i] == element) {
				return i;
			}
		}
	}
	return -1;
}
function validResult(result){
	if (result && "0" == result.code)
		return true;
	else
		return false;
}
/**
 * need layer.js
 * @param successCallback will call it if request success and reponse is success
 * @param async default true
 * @returns
 */
function ajaxPost(url,data,successCallback,async,failCallback) {
	var layerIndex;
	$.ajax({
		type: 'post',
        url: url,
        async: (null != async) ? async : true,
        data: data,
        beforeSend: function() {
        	 layerIndex = layer.load(0, { shade: [0.01, '#fff'] });
        },
        error : function(request) {
        	layer.close(layerIndex);
        	layerFail("Request Error" + (request.status ? ", status:" + request.status : ""));
		},
        success : function(result) {
        	layer.close(layerIndex);
        	if (validResult(result)) {
        		successCallback(result);
        	} else {
        		if (null != failCallback) {
        			failCallback(result);
        		} else{
        			if (result.msg)
            			layerFail(result.msg);
            		else
            			layerFail("Request Failure");
        		}
        	}
        }
	});
}
/**
 * need layer.js
 * @param successCallback will call it if request success and reponse is success
 * @param async default true
 * @returns
 */
function ajaxGet(url,data,successCallback,async,failCallback){
	var layerIndex;
	$.ajax({
		type: 'get',
        url: url,
        async: (null != async) ? async : true,
        data: data,
        beforeSend: function() {
       	 	layerIndex = layer.load(0, { shade: [0.01, '#fff'] });
        },
        error : function(request) {
        	layer.close(layerIndex);
        	layerFail("Request Error" + (request.status ? ", status:" + request.status : ""));
		},
        success : function(result) {
        	layer.close(layerIndex);
        	if (validResult(result)){
        		successCallback(result);
        	}else{
        		if (null != failCallback) {
        			failCallback(result);
        		} else {
	        		if (result.msg)
	        			layerFail(result.msg);
	        		else
	        			layerFail("Request Failure");
        		}
        	}
        }
	});
}

/**
 * need jquery.form.min.js
 * @param selector e.g $("#formUploadImage")
 * @param url e.g. /product/upload
 * @param action e.g post
 * @returns
 */
function ajaxForm(selector, url, successCallback, method, failCallback){
	var layerIndex;
	selector.ajaxSubmit({
	    type: method ? method : 'post',
	    url: url,
	    data:  $(this).serialize(),
	    beforeSend: function(){
       	 	layerIndex = layer.load(0, { shade: [0.01, '#fff'] });
        },
	    error : function(request) {
	    	layer.close(layerIndex);
	    	layerFail("Connection error",{title:"",btn:["OK"]});
		},
	    success : function(result) {
	    	layer.close(layerIndex);
	    	if (validResult(result)){
	    		successCallback(result);
	    	}else{
	    		if (null != failCallback) {
        			failCallback(result);
        		} else {
		    		if (result.msg)
		    			layerFail(result.msg);
		    		else
		    			layerFail("Request Failure");
        		}
	    	}
		}
	});
}

/**
 * depends on layer.js
 * @param info
 * @returns
 */
function layerAlert(info){
	layer.alert(info, {icon: 7, title:"", btn:["OK"]});
}
/**
 * depends on layer.js
 * @param info
 * @returns
 */
function layerFail(info, title){
	if (!info)
		info = "Fail";
	if (parent.layer)
		parent.layer.alert(info, {icon: 5, title:title ? title : "", btn:["OK"]});
	else
		layer.alert(info, {icon: 5, title:title ? title : "", btn:["OK"]});
}
/**
 * depends on layer.js
 * @param info
 * @returns
 */
function layerSuccess(info, title){
	if (!info)
		info = "Success";
	if (parent.layer)
		parent.layer.alert(info, {icon: 6, title:title ? title : "", btn:["OK"]});
	else
		layer.alert(info, {icon: 6, title:title ? title : "", btn:["OK"]});
}
function layerConfirm(info, btnOkText, okCallback, cancelCallback){
	
	layer.confirm(info, {
		  btn: [btnOkText, 'Cancel'],title:'',btnAlign: 'c'
	}, function(index){
		layer.close(index);
		if (okCallback)
			okCallback();
	}, function(index){
		layer.close(index);
		if (cancelCallback)
			cancelCallback();
	});
}

function layerCloseWindow(){
	if (parent.layer){
		var index = parent.layer.getFrameIndex(window.name);
		parent.layer.close(index);
	}else{
		var index =layer.getFrameIndex(window.name);
		layer.close(index);
	}
}

/**
 * call like layerZoomImg("www.123.com?id=123", "80%", "80%")
 * you can call like this 
 * 	var size = zoomSizeEqualScaling(500, 700, $(document).width() * 0.9, $(document).height() * 0.9);
	layerZoomImg(url, size[0] + "px", size[1] + "px");
 * @param url
 * @param title
 * @param width e.g 100px or 50%
 * @param height e.g 100px or 80%
 * @returns
 */
function layerZoomImg(url, width, height) {
	layer.open({
        type: 1,
        title: false,
        title:"Zoom Image",
        shadeClose: true,
        area: [null != width ? width : "70%", null != height ? height : "70%"],
        content: "<img style='width:100%;height:100%;' src=" + url + " />"
    });
}

/**
 * call like this 
 * var size = zoomSizeEqualScaling(560, 434, 1024, 768);
 * @param imageWidth
 * @param imageHeight
 * @param maxWidth
 * @param maxHeight
 * @returns
 */
function zoomSizeEqualScaling(imageWidth, imageHeight, maxWidth, maxHeight){
	var sx = maxWidth / imageWidth;
    var sy = maxHeight / imageHeight;
    // 等比缩放
    if (sx > sy) {
        sx = sy;
        maxWidth = (sx * imageWidth);
    } else {
        sy = sx;
            maxHeight = (sy * imageHeight);
        }

    var size = [2];
    size[0] = maxWidth;
    size[1] = maxHeight;
    return size;
}

function toDecimal2(x, ss) {
	var f = parseFloat(x);
	if (isNaN(f)) {
		return false;
	}
	var f = Math.round(x * 100) / 100;
	var s = f.toString();
	if (ss == 0)
		return s;
	else{
		var rs = s.indexOf('.');
		if (rs < 0) {
			rs = s.length;
			s += '.';
		}
		while (s.length <= rs + ss) {
			s += '0';
		}
		return s;
	}	
}

function bcFixed(num, s) {
	var times = Math.pow(10, s)
	var des = num * times + 0.5
	des = parseInt(des, 10) / times
	return toDecimal2(des, s) + '';
}

function processNull(text){
	if (text || 0 == text)
		return text;
	else
		return "";
}

function processNullAndZero(text){
	if (text && text != "0" && text != 0){
		return text;
	} else
		return "";
}
/**
 * result of addWithArith(1, 2, 2) is 3.00
 * 
 */
function addWithArith(num1, num2, s) {
	var baseNum, baseNum1, baseNum2, ret;
	try {
		baseNum1 = num1.toString().split(".")[1].length;
	} catch (e) {
		baseNum1 = 0;
	}
	try {
		baseNum2 = num2.toString().split(".")[1].length;
	} catch (e) {
		baseNum2 = 0;
	}
	baseNum = Math.pow(10, Math.max(baseNum1, baseNum2));
	ret = (num1 * baseNum + num2 * baseNum) / baseNum;
	return bcFixed(ret, s);
};
/**
 * result of subWithArith(2, 1, 1) is 1.0
 */
function subWithArith(num1, num2, s) {
	var baseNum, baseNum1, baseNum2, ret;
	var precision;// scale
	try {
		baseNum1 = num1.toString().split(".")[1].length;
	} catch (e) {
		baseNum1 = 0;
	}
	try {
		baseNum2 = num2.toString().split(".")[1].length;
	} catch (e) {
		baseNum2 = 0;
	}
	baseNum = Math.pow(10, Math.max(baseNum1, baseNum2));
	precision = (baseNum1 >= baseNum2) ? baseNum1 : baseNum2;
	ret = ((num1 * baseNum - num2 * baseNum) / baseNum).toFixed(precision);
	return bcFixed(ret, s);
};
/**
 * result of mulWithArith(2, 1, 1) is 2.0
 */
function mulWithArith(num1, num2, s) {
	var baseNum = 0, ret;
	try {
		baseNum += num1.toString().split(".")[1].length;
	} catch (e) {
	}
	try {
		baseNum += num2.toString().split(".")[1].length;
	} catch (e) {
	}
	ret = Number(num1.toString().replace(".", ""))
			* Number(num2.toString().replace(".", "")) / Math.pow(10, baseNum);
	return bcFixed(ret, s);
};
/**
 * result of divWithArith(2, 1, 3) is 1.000
 */
function divWithArith(num1, num2, s) {
	var baseNum1 = 0, baseNum2 = 0, ret;
	var baseNum3, baseNum4;
	try {
		baseNum1 = num1.toString().split(".")[1].length;
	} catch (e) {
		baseNum1 = 0;
	}
	try {
		baseNum2 = num2.toString().split(".")[1].length;
	} catch (e) {
		baseNum2 = 0;
	}
	with (Math) {
		baseNum3 = Number(num1.toString().replace(".", ""));
		baseNum4 = Number(num2.toString().replace(".", ""));
		ret = (baseNum3 / baseNum4) * pow(10, baseNum2 - baseNum1);
		return bcFixed(ret, s);
	}
};
(function($) {
    // backup jquery's ajax
    var _ajax = $.ajax;

    // overwrite jquery's ajax
    $.ajax = function(opt) {
        // backup opt's error function and success function
        var fn = {
            error : function(XMLHttpRequest, textStatus, errorThrown) {
            },
            success : function(data, textStatus) {
            }
        };
        if (opt.error) {
            fn.error = opt.error;
        }
        if (opt.success) {
            fn.success = opt.success;
        }

        // extend 
        var _opt = $.extend(opt, {
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                // extend error function
            	if ("parsererror" == textStatus){
            		showLoginWindow();
            	}else
            		fn.error(XMLHttpRequest, textStatus, errorThrown);
            },
            success : function(data, textStatus) {
                // extend success function
                if(data){
                	// intercept login page
                    if (!$.isPlainObject(data) && data.indexOf("<title>User Login</title>") > -1) {
                        showLoginWindow();
                    } else if (null != data.code && "10002" == data.code){
                    	showLoginWindow();
                    	fn.success(data, textStatus);
                    } else {
                        fn.success(data, textStatus);
                    }
                }


            }
        });
        _ajax(_opt);
    };
})(jQuery);

function showLoginWindow() {
	window.open("/user/login", "_blank");
}

// Make color by value (color: #RRGGBB)
function colorByValue(val, minVal, midVal, maxVal, minColor, midColor, maxColor, delta) {
	if (midVal == null)
		midVal = 0;
	if (midColor == null)
		midColor = '#FFFFFF';
	if (val == null)
		return midColor;

	if (maxVal != null && minVal != null) {
		if (val < midVal)
			return colorByValue2((val - midVal) / (minVal - midVal), midColor, minColor, delta);
		else
			return colorByValue2((val - midVal) / (maxVal - midVal), midColor, maxColor, delta);
		
	} else if (minVal != null) {
		return colorByValue2((val - midVal) / (minVal - midVal), midColor, minColor, delta);

	} else if (maxVal != null) {
		return colorByValue2((val - midVal) / (maxVal - midVal), midColor, maxColor, delta);
		
	} else {
		return midColor;
	}
}

// Make color by value (color: #RRGGBB)
function colorByValue2(val, minColor, maxColor, delta) {
	if (val < 0)
		val = 0;
	else if (val > 1)
		val = 1;
	
	if (delta)
		val += delta;
	
	return colorByValue3(val, val, val, minColor, maxColor, delta);
}

// Make color by value (color: #RRGGBB)
function colorByValue3(rVal, gVal, bVal, minColor, maxColor) {
	var minR = parseInt(minColor.substr(minColor.length - 6, 2), 16);
	var minG = parseInt(minColor.substr(minColor.length - 4, 2), 16);
	var minB = parseInt(minColor.substr(minColor.length - 2, 2), 16);

	var maxR = parseInt(maxColor.substr(maxColor.length - 6, 2), 16);
	var maxG = parseInt(maxColor.substr(maxColor.length - 4, 2), 16);
	var maxB = parseInt(maxColor.substr(maxColor.length - 2, 2), 16);
	
	var valR = Math.round(minR + (maxR - minR) * rVal);
	var valG = Math.round(minG + (maxG - minG) * gVal);
	var valB = Math.round(minB + (maxB - minB) * bVal);
	
	valR = Math.min(255, Math.max(0, valR));
	valG = Math.min(255, Math.max(0, valG));
	valB = Math.min(255, Math.max(0, valB));
	
	return '#' + (valR < 16 ? '0' : '') + valR.toString(16)
		+ (valG < 16 ? '0' : '') + valG.toString(16)
		+ (valB < 16 ? '0' : '') + valB.toString(16);
}

// Random color by text
function colorByText(text) {
	var val = 3.0 * (Math.abs(hashCodeByText(text)) % 120 + 1) / 120.0;
	val = Math.min(val, 3.0);
	if (val <= 1.0)
		return colorByValue3(val, 1.0 - val, 0, '#CCCCCC', '#FFFFFF');
	else if (val <= 2.0)
		return colorByValue3(0.0, val - 1.0, 2.0 - val, '#CCCCCC', '#FFFFFF');
	else
		return colorByValue3(3.0 - val, 0.0, val - 2.0, '#CCCCCC', '#FFFFFF');
}

// Hash code by text
function hashCodeByText(text) {
	var hash = 0;
	if (text && text.length > 0) {
	    for (var i = 0; i < text.length; i++) {
	        var character = text.charCodeAt(i);
	        hash = ((hash << 5) - hash) + character;
	        hash = hash & hash; // Convert to 32bit integer
	    }
	}
    return hash;
}
