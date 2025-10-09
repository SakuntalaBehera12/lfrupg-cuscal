jQuery.noConflict();

Liferay.on('allPortletsReady', function() {
	jQuery("#pin-change-start-date, #pin-change-end-date").datePicker({
		clickInput : true,
		createButton : false,
		startDate : "01/01/2000",
		showYearNavigation : true
	});
	
	updateShowHideFormText();
	
	jQuery("#show-hide-search-form").bind("click", function() {
		jQuery("#search-form-wrapper").toggle("slow", function() {
			updateShowHideFormText();
		});
	});
});

function updateShowHideFormText() {
	if (jQuery("#search-form-wrapper").css("display") == "block") {
		jQuery("#show-hide-search-form").html("Hide search form");
	} else if (jQuery("#search-form-wrapper").css("display") == "none") {
		jQuery("#show-hide-search-form").html("Show search form");
	}
}

function replaceAll(strMain, strFind, strRep) {
	return strMain.replace(new RegExp(strFind, 'g'), strRep);
}

function openPinChangeDetailsJsonObj(url, pinChangeId, pinChangeBusinessDate, pinChangeSource, currRowNum) {
    var divId = "pin-change-details-wrapper"; 
    
	jQuery.ajax({
		type : "post",
		url : url,
		data : {
			pinChangeTransactionId : pinChangeId,
			pinChangeBusinessDate : pinChangeBusinessDate,
			pinChangeSource : pinChangeSource,
			currRowNum : currRowNum
		},
		success : function(data) {		    
			var panData= json_parse(data);
			if(panData.requestDetails != null){
				createDetailDivForPinChange(data, currRowNum);
				openPinChangeModalWindow(divId);
		    } else {
			  var failedMessage = "<tr><td>An error has occurred while trying to process your request. If this error continues, please contact "+
					"<a class='normal-link' href='mailto:calldirect@cuscal.com.au'>CallDirect</a> on <span class='nowrap'>1300 650 501</span>.</td></tr>";
			   divId="transDetailErrorDiv";
			   jQuery("#transDetailError").html(failedMessage);
			   openPinChangeModalWindow(divId);
		    }
		}
    });	
}

function openPinChangeDetailsForPrevOrNext(url) {
    var divId = "pin-change-details-wrapper";
    
	jQuery.ajax({
		type : "post",
		url : url,
		success : function(data) {
			createDetailDivForPinChange(data, -1);			
			openPinChangeModalWindow(divId);
		}
    });	
}

function createDetailDivForPinChange(sData, currRowNum) {
    var data = json_parse(sData);
    
	if (currRowNum == -1) {
		currRowNum = data.nextRowNum - 1;
	}
	
	jQuery("#pin-change-details-code").html(data.code);
	jQuery("#pin-change-details-description").html(data.description);
	jQuery("#pin-change-details-message-type").html(data.messageType);
    
	createPanHtml(data);
	
	jQuery("#pin-change-transaction-id").val(data.transactionId);
	jQuery("#pin-change-business-date").val(data.busDate);
	jQuery("#pin-change-source").val(data.dataSrc);
	
	jQuery("#pin-change-details-pan").html(data.requestDetails.pan);
	jQuery("#pin-change-details-masked-pan").html(data.requestDetails.maskedPan);
	jQuery("#pin-change-details-expiry-date").html(data.requestDetails.expiryDate);
	
	if( typeof  data.hsmUp != "undefined" && data.hsmUp == false){
	   var hsmfailMsg = "An error occurred while unmasking the PAN. If this error continues, please contact <a href=\"mailto:calldirect@cuscal.com.au\">CallDirect</a> on <span class=\"nowrap\">1300 650 501</span>.";
	   jQuery("#pin-change-hsm-msg").html(hsmfailMsg);
	}
	

	// Changed this because in the Transaction Search requirements the
	// transaction date is set as the switch date time.
	//jQuery("#pin-change-details-transaction-date").html(data.requestDetails.transactionDateTime);
	jQuery("#pin-change-details-transaction-date").html(data.requestDetails.switchDateTime);
	jQuery("#pin-change-details-local-date").html(data.requestDetails.transactionLocationDateTime);
	jQuery("#pin-change-details-switch-date").html(data.requestDetails.switchDateTime);

	jQuery("#pin-change-details-issuer-id").html(data.requestDetails.issuerId);
	jQuery("#pin-change-details-issuer-name").html(data.requestDetails.issuerName);
	jQuery("#pin-change-details-ret-reference").html(data.requestDetails.cardAcceptorRetrievalReferenceNumber);

	jQuery("#pin-change-details-acquiring-id").html(data.requestDetails.acquirerId);
	jQuery("#pin-change-details-acquiring-name").html(data.requestDetails.acquirerName);
	jQuery("#pin-change-details-stan").html(data.requestDetails.acquirerSystemTrace);
	
	jQuery("#pin-change-details-response-code").html(data.responseDetails.code);
	jQuery("#pin-change-details-response-desc").html(data.responseDetails.description);
	jQuery("#pin-change-details-auth-id").html(data.responseDetails.authorisationId);
	
	jQuery("#pan-change-details-supervisor").html(data.pinChangeSupervisorId);
	jQuery("#pan-change-details-operator").html(data.pinChangeOperatorId);
	
	var s = jQuery("#pin-change-size").val();
	var size = Number(s);
	
	if (currRowNum <= 1) {
		jQuery("#prev").addClass("disabled");
	} else {
		jQuery("#prev").removeClass("disabled");
	}
	
	if (size <= currRowNum) {
		jQuery("#next").addClass("disabled");
	} else {
		jQuery("#next").removeClass("disabled");
	}
}

function printPinChangeDetails(url){
	jQuery.ajax({
		type : "post",
		url : url,
		success : function() {
			jQuery("#pin-change-details-pan").hide();
			jQuery("#pin-change-show-pan").hide();
			jQuery(".spacer").hide();
			jQuery("#pin-change-details-masked-pan").show();
			jQuery(".pct-print").print();
			jQuery("#pin-change-details-masked-pan").hide();
			jQuery(".spacer").show();
			jQuery("#pin-change-show-pan").show();
			jQuery("#pin-change-details-pan").show();
		}
    });
}

/*
 * 
 */
function open2FAPagePinChangeDetails(url) {
	jQuery("#pinChangeSearchResultsForm").attr("action", url);
	jQuery("#pinChangeSearchResultsForm").submit();
}

/**
 * Open the modal div.
 * 
 * @param divId
 */
function openPinChangeModalWindow(divId) {
	
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
		opacity : 30,
		overlayCss : {
			backgroundColor : "#000"
		},
		closeClass : "modal-close",
		onShow: function(dialog) {
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
		onClose: function(dialog) {
			removeNoPrintClass();
			closePinChangeModal();
		}
	});
}

/**
 * Close the modal div.
 */
function closePinChangeModal() {
	jQuery.modal.close();
}

function addNoPrintClass() {
	jQuery("#pan,a,input,table.cuscalTable,#footer,#navigation,span.pagination-text,div#transaction-search-form,#transactionForm p,#dockbar").addClass("no-print");
}

function removeNoPrintClass() {
	jQuery("#pan,a,input,table.cuscalTable,#footer,#navigation,span.pagination-text,div#transaction-search-form,#transactionForm p,#dockbar").removeClass("no-print");
}

/**
 * Check if the users browser is Firefox.
 * 
 * @returns boolean
 */
function checkFF() {
	return jQuery.browser.mozilla;
}

/**
 * Check if the browser is IE or not.
 * 
 * @returns boolean 
 */
function checkIE() {
	return jQuery.browser.msie;
}