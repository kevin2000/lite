 
$(function() {
	initForm("/dic/saveDic", "formEdit", getValidateOptions());
});


function getValidateOptions() {
	return {
		rules : {
			code : {
				required : true
			}
		},
		messages : {
			code : {
				required : getWarningText("Please enter Code")
			}
		}
	};
}
