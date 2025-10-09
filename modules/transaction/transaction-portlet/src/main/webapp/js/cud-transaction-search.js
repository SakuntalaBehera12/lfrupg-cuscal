/* use in no conflict mode */
jQuery.noConflict();

Liferay.on('allPortletsReady', function() {
	jQuery('#startDate, #endDate').datePicker({
		clickInput : true,
		createButton : false,
		startDate : "01/01/2000",
		showYearNavigation : true
	});
});

/* turn off ajax caching since some versions of IE have issues */
jQuery.ajaxSetup({
	cache : false
});

function replaceAll(strMain, strFind, strRep) {
	return strMain.replace(new RegExp(strFind, 'g'), strRep);
}

function openCudTransactionDetailsJsonObj(url,txId,etlDate,adPank,currRowNum,adTransactionDetailK) {
    var divId = "transactionCudDetailsDiv"; 

	jQuery.ajax({
		type : "post",
		url : url,
		data : {
			cudTxId : txId,
			cudCurrRowNum : currRowNum,
			cudAdPank : adPank,
			cudEtlDate : etlDate,
			adTransactionDetailK : adTransactionDetailK
		},
		success : function(data) {			
			var panData= json_parse(data);
			if(panData.requestDetails != null){
				createDetailDivForCud(data, currRowNum);			
				openModalWindowCUD(divId);
		    }else{
			  var failedMessage = "<tr><td>An error has occurred while trying to process your request. If this error continues, please contact "+
					"<a class='normal-link' href='mailto:calldirect@cuscal.com.au'>CallDirect</a> on <span class='nowrap'>1300 650 501</span>.</td></tr>";
			   divId="transCudDetailErrorDiv";
			   jQuery("#transDetailError").html(failedMessage);
		       openModalWindowCUD(divId);
		    }
		}
    });	
}

function openCudTransactionDetailsJsonObjForNextPrev(url) {
    var divId = "transactionCudDetailsDiv";
    
	jQuery.ajax({
		type : "post",
		url : url,
		success : function(data) {
			createDetailDivForCud(data, -1);			
			openModalWindowCUD(divId);
		}
    });	
}

function createDetailDivForCud(sData, currRowNum) {
	var data= json_parse(sData);
	if (currRowNum == -1) {
		currRowNum = data.nextRowNum - 1;
	}
	jQuery('#cudDesc').html(data.description);
	
	jQuery('#txId').val(data.transactionId);
	jQuery('#cudAdPank').val(data.adPank);
	jQuery('#cudEtlDate').val(data.etlProcessEndDate);
	jQuery('#adTransactionDetailK').val(data.adTransactionDetailK);
	
	createPanHtml(data);
	jQuery("#masked-pan").html(data.requestDetails.maskedPan);
	
	
		
	if (typeof data.requestDetails.transactionAmt != "undefined" &&
			data.requestDetails.transactionAmt != null) {
		jQuery('#txAmt').html(data.requestDetails.transactionAmt + "&nbsp;" + (data.requestDetails.currencyCodeAcq));
	}
	
	jQuery('#acqCurrencyCode').html(data.requestDetails.currencyCodeAcq);
	
	//jQuery('#txDtTme').html(data.requestDetails.transactionDateTime);
	jQuery('#txDtTme').html(data.requestDetails.switchDateTime);
	
		
	jQuery('#lctDtTme').html(data.requestDetails.transactionLocationDateTime);

	if (typeof data.requestDetails.cashAmt != "undefined" &&
			data.requestDetails.cashAmt != null) {
		jQuery('#cash').html(data.requestDetails.cashAmt + "&nbsp;" + (data.requestDetails.currencyCodeAcq));
	}
	
	jQuery('#switch').html(data.requestDetails.switchDateTime);
	
	if (typeof data.requestDetails.feeAmt != "undefined" &&
			data.requestDetails.feeAmt != null) {
		jQuery('#fee').html(data.requestDetails.feeAmt + "&nbsp;" + (data.requestDetails.currencyCodeAcq));
	}
	
	

	jQuery('#acqId').html(data.requestDetails.acquirerId);
	jQuery('#issueId').html(data.requestDetails.issuerId);

	jQuery('#acqName').html(data.requestDetails.acquirerName);
	jQuery('#issuerName').html(data.requestDetails.issuerName);

	jQuery('#sysTrace').html(data.requestDetails.acquirerSystemTrace);
	
	jQuery('#cardAcceptId').html(data.requestDetails.cardAcceptorId);

	
	jQuery('#cardAcptName').html(data.requestDetails.cardAcceptorName);
	//need to change...
	jQuery('#cardAcptTerminalId').html(data.requestDetails.cardAcceptorTerminalId);
	
	
	jQuery('#atmPos').html(data.requestDetails.orignalAcquirerAtmOrPos);


	jQuery('#entryMode').html(data.requestDetails.posEntryMode);
	jQuery('#entryDscrip').html(data.requestDetails.posEntryDescription);

	jQuery('#condCode').html(data.requestDetails.posConditionCode);
	jQuery('#condDescp').html(data.requestDetails.posConditionDescription);	

	jQuery('#merchCode').html(data.requestDetails.posMerchantCode);
	jQuery('#merchDscrp').html(data.requestDetails.posMerchantDescription);	
	
	jQuery('#respCode').html(data.responseDetails.code);
	jQuery('#respDescp').html(data.responseDetails.description);
	jQuery('#authId').html(data.responseDetails.authorisationId);
	jQuery('#authBy').html(data.responseDetails.authorisedBy);
	//need to change
	jQuery('#switchBy').html(data.responseDetails.switchedBy);
	
	var s = jQuery('#size').val();
	var size = Number(s);
		
	if (currRowNum <= 1) {
		jQuery('#prev').addClass("disabled");
	} else {
		jQuery('#prev').removeClass("disabled");
	}
	
	if (size <= currRowNum) {
		jQuery('#next').addClass("disabled");
	} else {
		jQuery('#next').removeClass("disabled");
	}
}

function filterCudByDates(url) {
	jQuery("#transactionCudForm").attr("action", url);
	jQuery("#transactionCudForm").submit();
}

function clearCudAll(url) {
	jQuery('#panBin').val("");
	jQuery('#startDate').val("");
	jQuery('#endDate').val("");
	jQuery('#startTimeHr').val("00");
	jQuery('#startTimeMin').val("00");		
	jQuery('#endTimeHr').val("24");
	jQuery('#endTimeMin').val("00");
	
	
	jQuery("#transactionCudForm").attr("action", url);
	jQuery("#transactionCudForm").submit();
	
}

function showHideCudSearchForm(){
	jQuery("#transactionCudSearch").toggle(
			"slow",
			function() {
				if (jQuery("#transactionCudSearch").css("display") == "block") {
					jQuery("#show-hide-cudSearch").html("Hide search form");
				} else {
					jQuery("#show-hide-cudSearch").html("Show search form");
				}
			});
}

function open2FAPageCudDet(url) {
	jQuery("#transactionCudForm").attr("action", url);
	jQuery("#transactionCudForm").submit();
}

function printCudReport(url){
	jQuery.ajax({
		type : "post",
		url : url,
		success : function() {
			jQuery("#pan").hide();
			jQuery("#showPan").hide();
			jQuery("#masked-pan").show();
			jQuery(".printable").print();
			jQuery("#masked-pan").hide();
			jQuery("#showPan").show();
			jQuery("#pan").show();
		}
    });
}

/**
 * Open the modal div.
 * 
 * @param divId
 */
function openModalWindowCUD(divId) {
	
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

function addNoPrintClass() {
	jQuery("#pan,a,input,table.cuscalTable,#footer,#navigation,span.pagination-text,div#transaction-cud-form,#transactionCudForm p").addClass("no-print");
}

function removeNoPrintClass() {
	jQuery("#pan,a,input,table.cuscalTable,#footer,#navigation,span.pagination-text,div#transaction-cud-form,#transactionCudForm p").removeClass("no-print");
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
