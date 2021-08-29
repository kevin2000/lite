/***
 * version 1.0.1
 */
var saveUrl, formId;
var successCallback;
/*$(function() {
	validateRule();
});*/

function initForm(saveUrl,formId,validateOptions){
	this.saveUrl = saveUrl;
	this.formId = formId;
	validateRule(validateOptions);
	
}
//will call when submit is success and return success
function setSuccessCallback(successCallback){
	this.successCallback = successCallback;
}
function validateRule(validateOptions) {	
	$("#" + formId).validate(validateOptions);
}
$.validator.setDefaults({
	submitHandler: function() {
			var layerIndex;
		//$('#' + formId).on('submit', function() {
	        $("#" + formId).ajaxSubmit({
	            type: 'post',
	            url: saveUrl,
	            data:  $(this).serialize(),
	            beforeSend: function(){
		       	 	layerIndex = layer.load(0, { shade: [0.01, '#fff'] });
		        },
	            error : function(request) {
	            	layer.close(layerIndex);
	            	layerFail("Connection error",{title:"",btn:["OK"]});
	    		},
	            success : function(data) {
	            	layer.close(layerIndex);
	    			if (data.code == 0) {
	    				if (successCallback)
    						successCallback(data);
    					else {
    						layerSuccess(data.msg);
    						
    						if (parent.layer){			
    							parent.itemChangeHandler();
    		    				var index = parent.layer.getFrameIndex(window.name);
    		    				parent.layer.close(index);
    	    				}
    					}
	    			} else {
	    				layerFail(data.msg);
	    			}

	    		}
	            
	        });
	        return false;
	    //});
	}
});

function getWarningText(text){
	return "<i class='text-danger'>" + text + "</i> ";
}

function cancel(){
	if (parent.layer){
		var index = parent.layer.getFrameIndex(window.name);
		parent.layer.close(index);
	}else{
		var index =layer.getFrameIndex(window.name);
		layer.close(index);
	}
}

/*
 * expand Validtor For Multi element with the same Name
 * 1, set diffrent id for each element
 * 2, call expandValidtorForMultiName like this: $(function(){expandValidtorForMultiName();});
 */
function expandValidtorForMultiElemtnSameName(){
	if ($.validator) {
        $.validator.prototype.elements = function () {
            var validator = this,
              rulesCache = {};

            // select all valid inputs inside the form (no submit or reset buttons)
            return $(this.currentForm)
            .find("input, select, textarea")
            .not(":submit, :reset, :image, [disabled]")
            .not(this.settings.ignore)
            .filter(function () {
                if (!this.name && validator.settings.debug && window.console) {
                    console.error("%o has no name assigned", this);
                }
                //注释这行代码
                // select only the first element for each name, and only those with rules specified
                //if ( this.name in rulesCache || !validator.objectLength($(this).rules()) ) {
                //    return false;
                //}
                rulesCache[this.name] = true;
                return true;
            });
        }
    }
}
/**
 * call this method like  $(function{iframeAuto();})
 * @returns
 */
function iframeAuto(){
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.iframeAuto(index);
}