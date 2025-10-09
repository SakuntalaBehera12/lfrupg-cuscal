var allMemberFieds,	
	animationTime = 300, //Time in ms.
	initialMemberMessageOption = new Option("Please select", "0");

Liferay.on('allPortletsReady', function() {
	
	// setupLoader();
	jQuery("#chargeback-form").submit(function() {
		jQuery("#loading").show();
	});
	
	jQuery(".document-indicator-help").bind("click", function() {
		openDocumentIndicatorModalWindow();
	});
	
	jQuery(".dispute-amount-help").bind("click", function() {
		openDisputeAmountModalWindow();
	});
	
	jQuery("#chargebackType").change(function() {
		//Reset everything every time the charge back type is changed.
		hideReasonCodes();
		jQuery("#reasonCode").val("0");
		resetAllFormData();
		
		if (jQuery(this).val() === "0") {
			jQuery("#reasons").fadeOut(animationTime);
			jQuery.each(jQuery("select"), function(i, data){				
				data.options[0].selected = "selected";
			});
		} else {
			populateReasonCodes();
			//jQuery("#reasons").fadeIn(animationTime);
		}
	});
	
	//Set the page when there is an error and the page is reloaded.
	if (jQuery("#chargebackType").val() !== "0") {
		populateValuesOnError();
	}
	
	
	jQuery("#firstCBReasonCode").change(function() {		
		resetAllFormData();		
		jQuery("#reasonCode").val("0");
		loadScreenFieldsForFirstChargeback();
	});
	
	
	jQuery("#arbitrationCBReasonCode").change(function() {		
		resetAllFormData();		
		jQuery("#reasonCode").val("0");
		loadScreenFieldsForArbitrationChargeback();		
	});
	
	
	jQuery("#retrievalRequestReasonCode").change(function() {		
		resetAllFormData();		
		jQuery("#reasonCode").val("0");
		loadScreenFieldsForRetrievalRequest();
	});
	
	
	jQuery("textarea.limit").live("keypress", function(e) {
		var limit = jQuery(this).attr("length");		
		if (limit === "undefined" 
			|| limit === "" 
			|| limit < 0) {
			limit = 100;
		}		
		if (jQuery(this).val().length >= limit) {
			jQuery(this).val(jQuery(this).val().substring(0, limit));
			if (e.which != 8) {
				return false;
			}
		}
	});
	
	
	jQuery('input[type=file]').change(function () {	    
	    jQuery("#document-Indicator-checkbox").attr('checked',true);	    
	});
});


function setupPageForReasonCodeScreen(reasonList, reasonCode){
	jQuery(reasonList).val(reasonCode);	
	if(reasonList=="#arbitrationCBReasonCode"){
		loadScreenFieldsForArbitrationChargeback();
	} else if (reasonList=="#firstCBReasonCode"){
		loadScreenFieldsForFirstChargeback();
	}else if (reasonList=="#retrievalRequestReasonCode"){
		loadScreenFieldsForRetrievalRequest();
	}
}


function loadScreenFieldsForFirstChargeback(){	
	var reasonCode = jQuery("#reasonCode").val();	
	if (reasonCode === "0") {
		reasonCode = jQuery("#firstCBReasonCode").val();
	}
	//select document indicator for the  supporting documents required reason codes
	if(reasonCode === "905" || reasonCode === "906" || reasonCode === "912" || reasonCode === "913" ||  reasonCode === "914" ||  reasonCode === "917" ||  reasonCode === "918"){		
		jQuery("#document-Indicator-checkbox").attr('checked',true);
	}
	jQuery("#reasonCode").val(reasonCode);
}


function loadScreenFieldsForArbitrationChargeback(){	
	var reasonCode = jQuery("#reasonCode").val();
	if (reasonCode === "0"
			|| reasonCode === "undefined" 
			|| reasonCode === ""
			|| reasonCode === null) {
		reasonCode = jQuery("#arbitrationCBReasonCode").val();
	}	
	jQuery("#reasonCode").val(reasonCode);
}

function loadScreenFieldsForRetrievalRequest() {
	var reasonCode = jQuery("#reasonCode").val();	
	if (reasonCode === "0" 
			|| reasonCode === "undefined" 
			|| reasonCode === ""
			|| reasonCode === null) {
		reasonCode = jQuery("#retrievalRequestReasonCode").val();
	}	
	jQuery("#reasonCode").val(reasonCode);
}

function populateReasonCodes(){	
	var chargebackType = jQuery("#chargebackType").val();
	jQuery("#disputeAmountDiv").show();
	jQuery("#reasons").show();
	if (chargebackType === "889") {
		jQuery("#retrievalRequestReasonCode").show();
		jQuery("#disputeAmountDiv").hide();
		jQuery("#disputeAmount").val("");
		return "#retrievalRequestReasonCode";
	}else if (chargebackType === "890") {
		jQuery("#firstCBReasonCode").show();
		return "#firstCBReasonCode";
	} else if (chargebackType === "891") {
		jQuery("#arbitrationCBReasonCode").show();
		return "#arbitrationCBReasonCode";
	} else if(chargebackType === "946"){
		jQuery("#disputeAmountDiv").hide();
		jQuery("#disputeAmount").val("");		
		jQuery("#reasons").hide();		
		return "";
	}
}


function hideReasonCodes(){
	jQuery("#firstCBReasonCode").hide();
	jQuery("#arbitrationCBReasonCode").hide();
	jQuery("#retrievalRequestReasonCode").hide();	
	resetReasonCodes();
}


function resetReasonCodes() {
	jQuery("#reasonCode").val("");
	jQuery("#firstCBReasonCode").val("0");
	jQuery("#arbitrationCBReasonCode").val("0");
	jQuery("#retrievalRequestReasonCode").val("0");		
}

function populateValuesOnError(){
	
	if(!jQuery("#updateTicket").val()){
		jQuery("#disputeAmount").val("");
	}
	jQuery("#firstCBReasonCode").hide();
	jQuery("#arbitrationCBReasonCode").hide();
	jQuery("#retrievalRequestReasonCode").hide();
	jQuery("#reasons").show();	
	var chargebackType = jQuery("#chargebackType").val();	
	var reasonCode = jQuery("#reasonCode").val();	
	jQuery("#disputeAmountDiv").show();
	if (chargebackType === "889") {		
		jQuery("#retrievalRequestReasonCode").show();		
		jQuery('#retrievalRequestReasonCode option[value="'+reasonCode+'"]').attr('selected',true);			
		jQuery("#reasons").fadeIn(animationTime);
		jQuery("#disputeAmountDiv").hide();
		jQuery("#disputeAmount").val("");
		return "#retrievalRequestReasonCode";
	}else if (chargebackType === "890") {		
		jQuery("#firstCBReasonCode").show();
		jQuery('#firstCBReasonCode option[value="'+reasonCode+'"]').attr('selected',true);	
		jQuery("#reasons").fadeIn(animationTime);
		return "#firstCBReasonCode";
	} else if (chargebackType === "891") {		
		jQuery("#arbitrationCBReasonCode").show();		
		jQuery('#arbitrationCBReasonCode option[value="'+reasonCode+'"]').attr('selected',true);		
		jQuery("#reasons").fadeIn(animationTime);
		return "#arbitrationCBReasonCode";
	} else if (chargebackType === "946") {	
		jQuery("#disputeAmountDiv").hide();
		jQuery("#disputeAmount").val("");
		jQuery("#reasons").hide();
		return "";
	}
	
}
function resetAllFormData() {
	jQuery("#chargeback-form input[type='text'], " +
			"#chargeback-form textarea").each(function() {
		jQuery(this).val("");
	});
	
	jQuery("select.reset").each(function(i, elm) {
		elm.options[0].selected = 'selected';
	});
	
	resetCheckboxesAndRadio();
}

function resetCheckboxesAndRadio() {
	jQuery("input[type=checkbox]").removeAttr("checked");
}

function addMoreFiles(){
    var fileIndex = jQuery("#fileTable tr").children().length;
    fileIndex = fileIndex - 1;
    jQuery("#fileTable").append(
            '<tr><td>'+
            '   <input type="file" name="requestAttachments['+ fileIndex +']" />'+
            '</td></tr>');
}

function openDisputeAmountModalWindow() {
	var data = "<tr><td>The dispute amount must always be in Australian dollars, even if the transaction amount is in another currency</td></tr>";	
	jQuery("#disputeAmountHelpTable").html(data);	
	openModalWindow("disputeAmountHelpDiv");
}

function openDocumentIndicatorModalWindow() {
	var data = "<tr><td>The documentation indicator must be checked when you are supplying documentation to support the service request. " +
	"Please note that for reason codes with '(Supply Supporting Docs)' supporting documentation must be supplied before the service " +
	"request can be submitted.</td></tr>";	
	jQuery("#documentIndicatorHelpTable").html(data);	
	openModalWindow("documentIndicatorHelpDiv");
}

/**
 * Open the modal div.
 * 
 * @param divId
 */
function openModalWindow(divId) {	
	var modalDiv = jQuery("#" + divId);
	var windowHeight = window.innerHeight;
	var modalHeight = modalDiv.height();
	
	// If windowHeight is unknown in this case then get the height in a
	// different way. Generally this is used for IE versions older than 7.
	// But the windowHeight was undefined in our case when using IE7.
	if (typeof windowHeight == "undefined") {
		windowHeight = document.documentElement.clientHeight;
	}	
	
	// If the height of the modal div is bigger than the users viewport then
	// reduce it by 20px.
	if (modalHeight > windowHeight) {
		modalHeight = windowHeight - 20;
	}
	
	// Set the height of the modal div only for IE. This also disables the
	// double scrollbars we were getting in IE.
	if (checkIE()) {
		modalDiv.height(modalHeight);
	}
	//Opens the modal div and updates the settings.
	modalDiv.modal({		
		opacity 	: 30,
		overlayCss 	: {
			backgroundColor : "#000"
		},
		minWidth	: 300,
		closeClass 	: "modal-close",
		onShow		: function(dialog) {
			
			// Update the width of the children div for IE and update the
			// max-height of the modal div in the case of other browsers.
			if (checkIE()) {
				modalDiv.children().each(function() {
					jQuery(this).width(jQuery(this).width() - 16);
				});
			} else {
				// Check the height because we don't want to do this when there
				// is a error getting the data.
				if (modalDiv.height() > windowHeight) {
					dialog.container.css("max-height", modalHeight);
					// Only set move the modal div down by 10 pixels for FF.
					// Chrome seems to have some issue setting the max-height
					// properly.
					if (checkFF()) {
						dialog.container.css("top", "10px");
					}
				}				
			}
			addNoPrintClass();
		},
		onClose		: function(dialog) {			
			removeNoPrintClass();
			closeModal();
		}
	});
}

/**
 * Close the modal div.
 */
function closeModal() {		
	jQuery.modal.close();
}

/**
 * Check if the browser is IE or not.
 * 
 * @returns boolean 
 */
function checkIE() {
	return jQuery.browser.msie;
}

function addNoPrintClass() {
	jQuery("#pan,a,input,table.cuscalTable,#footer,#navigation,span.pagination-text,div#chargeback-form,#chargeback-form p").addClass("no-print");
}

function removeNoPrintClass() {
	jQuery("#pan,a,input,table.cuscalTable,#footer,#navigation,span.pagination-text,div#chargeback-form,#chargeback-form p").removeClass("no-print");
}

/**
 * Check if the users browser is Firefox.
 * 
 * @returns boolean
 */
function checkFF() {
	return jQuery.browser.mozilla;
}