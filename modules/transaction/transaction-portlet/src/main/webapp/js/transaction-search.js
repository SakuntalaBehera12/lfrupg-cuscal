/* use in no conflict mode */
jQuery.noConflict();

/* turn off ajax caching since some versions of IE have issues */
jQuery.ajaxSetup({
	cache : false
});

// When the page has completed loading.
Liferay.on('allPortletsReady', function()  {
	
//	jQuery(".req-print").hide();
//	setupLoader();
	
	jQuery("#info-message-txdetails").hide();
	jQuery("#error-message-txdetails").hide();
	jQuery("#success-message").hide();
	
	jQuery("#startDate, #endDate").datePicker({
		clickInput 			: true,
		createButton 		: false,
		startDate 			: "01/01/2000",
		endDate				: (new Date()).asString(),
		showYearNavigation 	: true,
		verticalOffset 		: 20
	});
	jQuery("#startDate, #endDate").dpSetOffset(22, 0);
	
	jQuery("#panBin").focus();
	
	jQuery("#endDate").bind(
		"dpClosed",
		function(e, selectedDate) {
			var d = selectedDate[0];
			if (d) {
				d = new Date(d);
				if (jQuery("#startDate").val() === "" 
					|| !isValidDate(jQuery("#startDate").val()) 
					|| (convertToDate(jQuery("#startDate").val()) > d) ){
						jQuery("#startDate").val(d.addDays(0).asString());
				}
			}
		}
	);
	
	jQuery("#startDate").bind(
		"dpClosed",
		function(e, selectedDate) {
			var d = selectedDate[0];
			if (d) {
				d = new Date(d);
				if (jQuery("#endDate").val() === "" 
					|| !isValidDate(jQuery("#endDate").val()) 
					|| (convertToDate(jQuery("#endDate").val()) < d)){
						jQuery("#endDate").val(d.addDays(0).asString());
				}
			}
		}
	);
	
	/* Code for Quick/Advanced Search. Uncomment this code when it is required

	//this is used as .change() does not work for IE
	if (jQuery.browser.msie) {
		jQuery("input:radio[name='searchView']").click(function() {
		    this.blur();
		    this.focus();
		  });
	}
	
	jQuery("input:radio[name='searchView']").change(function(){
		jQuery("#optionalFilters").toggle("slow", "swing");
		 jQuery("#visaIdField").toggle("slow", "swing");
		 jQuery("#cuscalIdField").toggle("slow", "swing");
		 jQuery("#terminalIdField").toggle("slow", "swing");
		 jQuery("#cardAcceptorIdField").toggle("slow", "swing");
		 jQuery("#stanField").toggle("slow", "swing");
	});
	
	if (jQuery("input:radio[name='searchView']:checked").val()==="quick"){
		jQuery("#optionalFilters").hide();
		 jQuery("#visaIdField").hide();
		 jQuery("#cuscalIdField").hide();
		 jQuery("#terminalIdField").hide();
		 jQuery("#cardAcceptorIdField").hide();
		 jQuery("#stanField").hide();
		
	} else {
		jQuery("#optionalFilters").show();
		 jQuery("#visaIdField").show();
		 jQuery("#cuscalIdField").show();
		 jQuery("#terminalIdField").show();
		 jQuery("#cardAcceptorIdField").show();
		 jQuery("#stanField").show();
	}

	*/
	
	jQuery(".tab").live("click", function() {
		var section = jQuery(this).attr("sec");
			 
		if (section == "detail") {
			if (jQuery("#transaction-details-wrapper").is(":hidden")) {
				jQuery("li.serv-req").removeClass("selected");
				jQuery("li.trans-det").addClass("selected");
//				jQuery(".req-print").hide();
				jQuery(".trans-print").show();
				
				jQuery("#service-requests").fadeOut(300, function() {
					jQuery("#transaction-details-wrapper").fadeIn(300);
				});
			}
		} else if (section == "request"){
			var url = jQuery(this).attr("url");
			
			if (jQuery("#service-requests").is(":hidden")) {
					jQuery("li.trans-det").removeClass("selected");
					jQuery("li.serv-req").addClass("selected");
					jQuery(".trans-print").hide();
//					jQuery(".req-print").show();
					
					jQuery("#service-requests").attr("trans", jQuery(".cuscal-transaction-id").val());
					jQuery("#service-requests").attr("dat", jQuery(".business-date").val());
					getTransactionTickets(url);
				}
		}
	});
	
	
	jQuery("#service-request-form-visa").submit(function(event) {
		event.preventDefault();
		jQuery("#loading").show();
//		jQuery("#submit-visa").hide();
//		jQuery("#disclaimer-wrap").hide();		
		var url = jQuery(this).attr("action");
		var dat = jQuery("#service-requests").attr("dat");
		jQuery(".business-date").val(dat);		
		var theForm = jQuery(this).serialize();
		jQuery.ajax({
			type	: "post",
			url		: url,
			dataType: "application/JSON",
			data	: theForm + "&dat=" + dat,
			async	: false,
			success	: function(data) {
				jQuery(".trans-print").hide();
				var resp = json_parse(data);
				if (resp.status == "FAIL") {
					createErrorDiv(resp);
				} else {
					createTicketDetailsScreen(resp);
					jQuery("#transaction-details-wrapper").hide();
					jQuery("#success-message").show();
					jQuery("#request-form").hide();
				}
			}
		});		
	});
	
	jQuery("#service-request-form-atmpos").submit(function() {
		jQuery("#createTicket").hide();
	});
	
	jQuery("#visa-request-type").change(function() {
		var requestType = jQuery(this).val();
		jQuery("#error-message-txdetails").hide();
		if (requestType == 1) {
			
			if (jQuery("#tc40-form").css("display") === "none") {
				jQuery("#tc52-form, #vc-form").each(function() {
					if (jQuery(this).css("display") === "block") {
						jQuery(this).stop().fadeOut(300);
					}
				});
			}
			
			setTimeout(function() {
				jQuery("#tc40-form").hide().fadeIn(300, function() {
					jQuery("#submit-visa").hide().fadeIn(150);
					jQuery("#disclaimer-wrap").hide().fadeIn(150);
				});
			}, 650);
		} else if (requestType == 2) {
			
			if (jQuery("#tc52-form").css("display") === "none") {
				jQuery("#tc40-form, #vc-form").each(function() {
					if (jQuery(this).css("display") === "block") {
						jQuery(this).stop().fadeOut(300);
					}
				});
			}
			
			setTimeout(function() {
				jQuery("#tc52-form").hide().fadeIn(300, function() {
					jQuery("#submit-visa").hide().fadeIn(150);
					jQuery("#disclaimer-wrap").hide().fadeIn(150);
				});
			}, 650);
		} else if (requestType == 3) {
			
			if (jQuery("#vc-form").css("display") === "none") {
				jQuery("#tc40-form, #tc52-form").each(function() {
					if (jQuery(this).css("display") === "block") {
						jQuery(this).stop().fadeOut(300, function() {
							jQuery("#submit-visa").stop().fadeOut(300);
							jQuery("#disclaimer-wrap").stop().fadeOut(300);
						});
					}
				});
			}
			
			setTimeout(function() {
				jQuery("#vc-form").hide().fadeIn(300);
			}, 350);
		} else {
			jQuery("#tc40-form").fadeOut(300);
			jQuery("#tc52-form").fadeOut(300);
			jQuery("#vc-form").fadeOut(300);
			jQuery("#submit-visa").fadeOut(300);
			jQuery("#disclaimer-wrap").fadeOut(300);
		}
	});

	jQuery("#mastercard-request-type").change(function() {
		var requestType = jQuery(this).val();
		if (requestType == 4) {					
			setTimeout(function() {
				jQuery("#mastercard-dispute-form").stop().fadeIn(300);
			}, 350);			
		} else {
			jQuery("#mastercard-dispute-form").fadeOut(300);
		}
	});
	
	jQuery(".amount-received").blur(function(){
		var transactionAmount = jQuery(".transaction-amount-value").val();
        var transactionCode = jQuery(".atm-or-pos").data("transactioncode");
        var atmOrPos = jQuery(".atm-or-pos").html();
		var atmFee = "0.00";		
		if(jQuery(".atm-fee").val()!= ""){			
			atmFee = jQuery(".atm-fee").val();
		}

		var claimAmount = "0.00";
		//check transactiontype is pos and deposit (21)
        if (atmOrPos === "POS" && transactionCode === "21") {
            claimAmount = formatNumber(jQuery(this).val(),'N') - formatNumber(transactionAmount,'N');
        } else {
            claimAmount = formatNumber(transactionAmount,'N') + formatNumber(atmFee,'N') - formatNumber(jQuery(this).val(),'N');
        }

		if(isNaN(claimAmount)){
			claimAmount = "0.00";
		} else {			
			claimAmount = claimAmount.toFixed(2);
		}
		
		setHtmlOrInputValue(jQuery(".claim-amount"), claimAmount);
	});
	
	jQuery(".amount-deposited").blur(function(){		
		var cardholderAmount = jQuery(".cardholder-amount-value").val();		
		var atmFee = "0.00";			
		var depositedAmount = jQuery(this).val();
		
		if(jQuery(".atm-fee").val()!= ""){			
			atmFee = jQuery(".atm-fee").val();
		}	
		var claimAmount = formatNumber(depositedAmount,'Y') - (formatNumber(cardholderAmount, 'Y') + formatNumber(atmFee,'Y'));		
		if(isNaN(claimAmount)){
			claimAmount = "0.00";
		} else {			
			claimAmount = claimAmount.toFixed(2);
		}		
		setHtmlOrInputValue(jQuery(".claim-amount"), claimAmount);
		
	});
	
	jQuery(document).ajaxStart(function() {
		jQuery("#loading").show();
	}).ajaxStop(function() {
		jQuery("#loading").hide();
	});
	
	jQuery("#transactionForm").bind("submit", function() {
		jQuery("#loading").show();
	});	
	
	jQuery("#mastercard-dispute-form>.next-button").live("click", function(ev){				
		jQuery("#loading").show();	
	 });
	
	jQuery("#search").keypress(function(event) {
		if (event.which == 13) {
			event.preventDefault();
			jQuery("#transactionForm").submit();
		}
	});
	
	jQuery(".scheme-id-help").bind("click", function() {		
		openSchemeIDModalWindow();
	});
	
});

function openSchemeIDModalWindow() {
	jQuery("#scheme-id-desc").empty();
	jQuery("<p/>")
		.html("For example MasterCard ID, Visa ID etc.")
		.appendTo(jQuery("#scheme-id-desc"));
	openModalWindow("scheme-id-div");
}

function addMoreFiles(){
    var fileIndex = jQuery("#fileTable tr").children().length;
    fileIndex = fileIndex - 1;
    jQuery("#fileTable").append(
            '<tr><td>'+
            '   <input type="file" name="atmPosClaimInformation.requestAttachments['+ fileIndex +']" />'+
            '</td></tr>');
}

function replaceAll(strMain, strFind, strRep) {
	return strMain.replace(new RegExp(strFind, 'g'), strRep);
}

function openTransactionDetailsJsonObj(url, txId, busDate, txSrc, currRowNum, pageName) {
    var divId = "transactionDetailsDiv"; 
	jQuery(".business-date").val(busDate);	
	jQuery("#service-requests").attr("dat", busDate);	
	jQuery.ajax({
		type 	: "post",
		url  	: url,
		data 	: {
			txId 		: txId,
			busDate 	: busDate,
			txSrc 		: txSrc,
			currRowNum 	: currRowNum
		},
		success : function(data) {			
			var panData= json_parse(data);
			if(panData.requestDetails != null) {
				createDetailDivForTX(data, currRowNum);			
				openModalWindow(divId, 826);				
				if (typeof pageName != undefined && pageName == "searchResult") {					
					jQuery("li.trans-det").addClass("selected");
					jQuery("li.serv-req").removeClass("selected");
					jQuery("#transaction-details-wrapper").show();
					jQuery("#service-requests").hide();
					clearAllPageErrors();
					clearATMPageErrors();
					jQuery(".portlet-msg-error").hide();
					jQuery("#success-message").hide();
					setHtmlOrInputValue(jQuery(".outstandingTC40"), false);
					setHtmlOrInputValue(jQuery(".outstandingTC52"), false);
					setHtmlOrInputValue(jQuery(".outstandingDispute"), false);
					setHtmlOrInputValue(jQuery(".outstandingClosedDispute"), false);
					setHtmlOrInputValue(jQuery(".outstandingReinvestigation"), false);
				}
		    } else {
			  var failedMessage = "<tr><td>An error has occurred while trying to process your request. If this error continues, please contact "+
					"<a class='normal-link' href='mailto:calldirect@cuscal.com.au'>CallDirect</a> on <span class='nowrap'>1300 650 501</span>.</td></tr>";
			   divId="transDetailErrorDiv";
			   jQuery("#transDetailError").html(failedMessage);
		       openModalWindow(divId, 826);
		    }		
		}
    });
}

function openTransactionDetailsJsonObjForNextPrev(url) {
    var divId = "transactionDetailsDiv";    
    jQuery("#success-message").hide();
    jQuery("#error-message-txdetails").hide();
    jQuery(".portlet-msg-error").hide();    
	setHtmlOrInputValue(jQuery(".outstandingTC40"), false);
	setHtmlOrInputValue(jQuery(".outstandingTC52"), false);
	setHtmlOrInputValue(jQuery(".outstandingDispute"), false);
	setHtmlOrInputValue(jQuery(".outstandingClosedDispute"), false);
	setHtmlOrInputValue(jQuery(".outstandingReinvestigation"), false);	
    clearAllPageErrors();
    clearATMPageErrors();    
	jQuery.ajax({
		type 	: "post",
		url 	: url,
		success : function(data) {
			createDetailDivForTX(data, -1);			
			openModalWindow(divId, 826);
			jQuery("#service-requests").fadeOut(300, function() {
				jQuery("#transaction-details-wrapper").fadeIn(300);
			});
			
		}
    });	
}

function isEmpty(obj) {
	var name;
	for (name in obj) {
		return false;
	}
	return true;
}

/* Format the Java date to dd/MM/yyyy for javascript display */
function formatDateForDisplay(milliseconds, withTime) {
	var date = new Date(milliseconds);
	var formattedDate;
	
	function pad(n) {return n < 10 ? '0'+n : n;}
	
	formattedDate = pad(date.getDate())
				+ "/"
				+ pad((date.getMonth() + 1))
				+ "/"
				+ pad(date.getFullYear());
	if (withTime === true) {
		formattedDate += " "
					+ pad(date.getHours())
					+ ":"
					+ pad(date.getMinutes());
	} 
	
	return formattedDate;
}

function createDetailDivForTX(sData, currRowNum) {
    var data= json_parse(sData); 
	if (currRowNum == -1) {
		currRowNum = data.nextRowNum - 1;
	}
	
	if (typeof data.accessibleIssuer != undefined 
			&& false == data.accessibleIssuer) {
		jQuery('#servReqTab').hide();
	} else {
		jQuery('#servReqTab').show();
	}
	
	jQuery('#code').html(data.code);
	jQuery('#txDesc').html(data.description);
	jQuery('#msgType').html(data.messageType);
	
	setHtmlOrInputValue(jQuery(".cuscal-transaction-id"), data.transactionId);
	setHtmlOrInputValue(jQuery(".business-date"), data.busDate);
	jQuery("#service-requests").attr("trans", data.transactionId);
	jQuery("#service-requests").attr("dat", data.busDate);
	
	jQuery('#busDate').val(data.busDate);
	jQuery('#txSrc').val(data.dataSrc);
    
	setHtmlOrInputValue(jQuery(".masked-pan"), data.requestDetails.maskedPan);
	createPanHtml(data);
	
	if (typeof data.validServiceRequest != undefined) {
		if (data.validServiceRequest === false) {
			jQuery(".mastercard-additional-transaction-info-fieldset").hide();
			jQuery("#ticket-table").hide();
			jQuery("#request-form").hide();
			if (typeof data.visaOrAtmPos != undefined) {
				if(data.visaOrAtmPos == "MASTERCARD_IPM") {
					setHtmlOrInputValue(jQuery("#info-message-txdetails"), 
					"<span>This transaction is ineligible for submission of the selected service request.</span>");
				}else {
					setHtmlOrInputValue(jQuery("#info-message-txdetails"), 
					"<span>Currently online Service Requests can only be submitted" +
					" for ATM disputes, eftpos disputes, Visa voucher requests (TC52) " +
					"and Visa fraud reports (TC40)." +
					"<br><br>" +
					"If you require assistance for this type of transaction please contact " +
					"<a href=\"mailto:calldirect@cuscal.com.au\">CallDirect</a>" +
					" on <span class=\"nowrap\">1300 650 501</span>");	
				}
			}else {
				setHtmlOrInputValue(jQuery("#info-message-txdetails"), 
					"<span>Currently online Service Requests can only be submitted" +
					" for ATM disputes, eftpos disputes, Visa voucher requests (TC52) " +
					"and Visa fraud reports (TC40)." +
					"<br><br>" +
					"If you require assistance for this type of transaction please contact " +
					"<a href=\"mailto:calldirect@cuscal.com.au\">CallDirect</a>" +
					" on <span class=\"nowrap\">1300 650 501</span>");
			}
			jQuery("#info-message-txdetails").show();
		} else if (data.validServiceRequest === true) {
			setHtmlOrInputValue(jQuery("#info-message-txdetails"), "");
			jQuery("#info-message-txdetails").hide();
			showHideProperForm(data);
			jQuery("#ticket-table").show();
			jQuery("#request-form").show();
			
		}
	}
		
	if (typeof data.memberInformation != undefined
			&& null !== data.memberInformation) {
		setHtmlOrInputValue(jQuery(".member-name"), data.memberInformation.memberName);
		setHtmlOrInputValue(jQuery(".member-number"), data.memberInformation.memberNumber);
	}
	
	if (typeof data.contactInformation != undefined
			&& null !== data.contactInformation) {
		setHtmlOrInputValue(jQuery(".given-name"), data.contactInformation.givenName);
		setHtmlOrInputValue(jQuery(".contact-surname"), data.contactInformation.surname);
		setHtmlOrInputValue(jQuery(".contact-phone"), data.contactInformation.phoneNo);
		setHtmlOrInputValue(jQuery(".contact-email"), data.contactInformation.email);
		setHtmlOrInputValue(jQuery(".contact-organisation"), data.contactInformation.organisation);
	}
	
	if (typeof data.visaArn != undefined 
			&& null !== data.visaArn) {
		setHtmlOrInputValue(jQuery(".arn"), data.visaArn);
	}
	
	setHtmlOrInputValue(jQuery(".reversal"), data.reversal);
	setHtmlOrInputValue(jQuery(".visaAtm"), data.visaAtm);
		
	jQuery("#expDate").html(data.requestDetails.expiryDate);
	
	if (typeof  data.hsmUp != "undefined" && data.hsmUp == false){
	   var hsmfailMsg = "An error occurred while unmasking the PAN. If this error continues, please contact <a href=\"mailto:calldirect@cuscal.com.au\">CallDirect</a> on <span class=\"nowrap\">1300 650 501</span>.";
	   jQuery('#hsmMsg').html(hsmfailMsg);
	}
	
	if (typeof data.requestDetails.transactionAmt != "undefined" &&
			data.requestDetails.transactionAmt != null) {
		var transactionAmount = data.requestDetails.transactionAmt + "&nbsp;" + (data.requestDetails.currencyCodeAcq);
		setHtmlOrInputValue(jQuery(".transaction-amount"), transactionAmount);
		jQuery(".transaction-amount-value").val(data.requestDetails.transactionAmt);
		jQuery(".transaction-amount-currency").val(data.requestDetails.currencyCodeAcq);
	}
	
	
	jQuery('#acqCurrencyCode').html(data.requestDetails.currencyCodeAcq);
	
	//jQuery('#txDtTme').html(data.requestDetails.transactionDateTime);
	jQuery("#txDtTme").html(data.requestDetails.switchDateTime);
	
	if (typeof data.requestDetails.cardholderAmt != "undefined" &&
			data.requestDetails.cardholderAmt != null) {
		var cardholderAmount = data.requestDetails.cardholderAmt + "&nbsp;" + (data.requestDetails.currencyCodeIss);
		jQuery(".cardholder-amount").html(cardholderAmount);
		jQuery(".cardholder-amount-value").val(data.requestDetails.cardholderAmt);
		jQuery(".cardholder-amount-currency").val(data.requestDetails.currencyCodeIss);
	}
	
	setHtmlOrInputValue(jQuery(".location-date"), data.requestDetails.transactionLocationDateTime);

	if (typeof data.requestDetails.cashAmt != "undefined" &&
			data.requestDetails.cashAmt != null) {
		jQuery('#cash').html(data.requestDetails.cashAmt + "&nbsp;" + (data.requestDetails.currencyCodeAcq));
	}
	
	jQuery('#switch').html(data.requestDetails.switchDateTime);
	
	if (typeof data.requestDetails.feeAmt != "undefined" &&
			data.requestDetails.feeAmt != null) {
		jQuery('#fee').html(data.requestDetails.feeAmt + "&nbsp;" + (data.requestDetails.currencyCodeAcq));
	}
	
	jQuery('#claim-amt').val(0);
	jQuery("#settlement").html(data.requestDetails.settlementDateTime);
	
	setHtmlOrInputValue(jQuery(".acquirer-id"), data.requestDetails.acquirerId);
	setHtmlOrInputValue(jQuery(".issuer-id"), data.requestDetails.issuerId);
	setHtmlOrInputValue(jQuery(".acquirer-name"), data.requestDetails.acquirerName);
	setHtmlOrInputValue(jQuery(".issuer-name"), data.requestDetails.issuerName);
	
	var issuerIdAndTitle = data.requestDetails.issuerId;
	
	if (data.requestDetails.issuerId !== null &&
			data.requestDetails.issuerId !== "" &&
			data.requestDetails.issuerId !== undefined) {
		issuerIdAndTitle += " - "
						 + data.requestDetails.issuerName;
	}
	setHtmlOrInputValue(jQuery(".ticket-issuer"), issuerIdAndTitle);
	
	var acquirerIdAndTitle = data.requestDetails.acquirerId;
	
	if (data.requestDetails.acquirerName !== null &&
			data.requestDetails.acquirerName !== "" && 
			data.requestDetails.acquirerName !== undefined ) {
		acquirerIdAndTitle += " - "
						   + data.requestDetails.acquirerName;
	}
	
	setHtmlOrInputValue(jQuery(".ticket-acquirer"), acquirerIdAndTitle);

	setHtmlOrInputValue(jQuery(".stan"), data.requestDetails.acquirerSystemTrace);
	
	setHtmlOrInputValue(jQuery(".visa-transaction-id"), data.externalTransactionId);
	
	jQuery('#retRefNo').html(data.requestDetails.issuerRetrievalReferenceNumber);

	jQuery('#orgAcqId').html(data.requestDetails.orignalAcquirerId);
	jQuery('#cardAcceptId').html(data.requestDetails.cardAcceptorId);
	
	if(jQuery('#posForm option').length <= 1){
		populateDropDowns(data.reasonData.posMap, '#posForm');
	}	
	if(jQuery('#atmForm option').length <= 1){
		populateDropDowns(data.reasonData.atmMap, '#atmForm');
	}		
	if(jQuery('#atmDepositDisputeReasonsForm option').length <= 1){
		populateDropDowns(data.reasonData.atmDepositDisputeReasonsMap, '#atmDepositDisputeReasonsForm');
	}
	//if the options have not yet been loaded onto the page yet for Visa
	if(jQuery('#fraudType option').length <= 1){
		populateDropDowns(data.reasonData.tc40FraudTypeMap, '#fraudType');
		populateDropDowns(data.reasonData.tc40NotificationTypeMap, '#fraudNotifCode');
		populateDropDowns(data.reasonData.tc40DetectionMap, '#detection');
		populateDropDowns(data.reasonData.tc52Map, '#voucherReason');		
		populateServiceTypeDropDowns(data.reasonData.visaType, '#visa-request-type');
	}
	
	//if the options have not yet been loaded onto the page yet for mastercard-request-type
	if(jQuery('#mastercard-request-type option').length <= 1){		
		populateServiceTypeDropDowns(data.reasonData.visaType, '#mastercard-request-type');
	}
	
	populateButtonsForPortlets(data.reasonData.visaDestination, ".open-sr-portlet");
	jQuery("#vc-form").hide();
	jQuery("#mastercard-dispute-form").hide();
	
	setHtmlOrInputValue(jQuery(".dest"), jQuery(data.reasonData.visaDestination).get("2"));
	
	jQuery('#orgAcqName').html(data.requestDetails.orignalAcquirerName);
	jQuery('#cardAcptName').html(data.requestDetails.cardAcceptorName);
	
	setHtmlOrInputValue(jQuery(".terminal-id"), data.requestDetails.orignalAcquirerTerminalId);
	jQuery("#cardRetRefNo").html(data.requestDetails.cardAcceptorRetrievalReferenceNumber);

	setHtmlOrInputValue(jQuery(".atm-or-pos"), data.requestDetails.orignalAcquirerAtmOrPos);
	//set transactioncode(transactiontype) data for later use in claim amount calculation
    jQuery(".atm-or-pos").data("transactioncode", data.transactionCode);
	jQuery('#pinPrst').html(data.requestDetails.pinPresent);

	jQuery('#entryMode').html(data.requestDetails.posEntryMode);
	jQuery('#entryDscrip').html(data.requestDetails.posEntryDescription);
	jQuery('#auth3ds').html(data.requestDetails.pos3dsAuthentication);
	jQuery('#auth3ds_desc').html(data.requestDetails.pos3dsAuthDescription);

	jQuery('#condCode').html(data.requestDetails.posConditionCode);
	jQuery('#condDescp').html(data.requestDetails.posConditionDescription);	

	jQuery('#merchCode').html(data.requestDetails.posMerchantCode);
	jQuery('#merchDscrp').html(data.requestDetails.posMerchantDescription);	
	
	//ProjectP00337
	jQuery('#termCapability').html(data.requestDetails.terminalCapability);
	
	jQuery('#faultyCardReader').html(data.requestDetails.faultyCardReader);
	if(data.requestDetails.terminalCapability != null && data.requestDetails.faultyCardReader == 'Yes'){
		jQuery("#atmForm option[value=962]").remove();//disable counterfiet transaction when both these values are present
	}
	//ends P00337
	
	
	jQuery('#respCode').html(data.responseDetails.code);
	jQuery('#respDescp').html(data.responseDetails.description);
	jQuery('#authId').html(data.responseDetails.authorisationId);
	jQuery('#authBy').html(data.responseDetails.authorisedBy);
	jQuery('#cardControlRejectCode').html(data.cardControlRejectCode);
	
	jQuery('#isMobile').html(data.requestDetails.isMobile);
	
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

function setAdditionalTransactionInformation(data){
	
	if (typeof data.requestDetails.posEntryMode != undefined && data.requestDetails.posEntryMode == null ) {		
		jQuery('.entryMode').hide();
		jQuery('.entryDscrip').hide();
	}else{	
		jQuery('.entryMode').show();
		jQuery('.entryDscrip').show();
		jQuery('.entryMode').html(data.requestDetails.posEntryMode);
		jQuery('.entryDscrip').html(data.requestDetails.posEntryDescription);		
	}
	
	if (typeof data.requestDetails.posConditionCode != undefined && data.requestDetails.posConditionCode == null) {		
		jQuery('.condCode').hide();
		jQuery('.condDescp').hide();
	}else{	
		jQuery('.condCode').show();
		jQuery('.condDescp').show();
		jQuery('.condCode').html(data.requestDetails.posConditionCode);
		jQuery('.condDescp').html(data.requestDetails.posConditionDescription);		
	}
	
	if (typeof data.functionCode != undefined && 0 == data.functionCode) {			
		jQuery('.functionCode').hide();
		jQuery('.functionCodeDescription').hide();
	}else{	
		jQuery('.functionCode').show();
		jQuery('.functionCodeDescription').show();
		jQuery('.functionCode').html(data.functionCode);
		jQuery('.functionCodeDescription').html(data.functionCodeDescription);
	}
	
	if (typeof data.ucafcollectionIndicator != undefined && data.ucafcollectionIndicator == null) {			
		jQuery('.ucaf-collection-indicator').hide();	
		jQuery('.ucaf-collection-indicator-description').hide();
	}else{
		jQuery('.ucaf-collection-indicator').show();
		jQuery('.ucaf-collection-indicator-description').show();
		jQuery('.ucaf-collection-indicator').html(data.ucafcollectionIndicator);
		jQuery('.ucaf-collection-indicator-description').html(data.ucafcollectionIndicatorDescription);
	}
	
	if (typeof data.electronicCommerceIndciator != undefined && data.electronicCommerceIndciator == null) {			
		jQuery('.electronic-commerce-indciator').hide();		
	}else{		
		jQuery('.electronic-commerce-indciator').show;
		jQuery('.electronic-commerce-indciator').html(data.electronicCommerceIndciator);
	}	
	if (typeof data.centralSiteBusinessDate != undefined && data.centralSiteBusinessDate == null) {		
		jQuery('.csbd-value').hide();		
	}else{		
		jQuery('.csbd-value').show;
		jQuery('.csbd-value').html(data.centralSiteBusinessDate);
	}
	
	if (typeof data.track2DataServiceCode != undefined && data.track2DataServiceCode == null) {			
		jQuery('.track2-data-present-value').hide();		
	}else{		
		jQuery('.track2-data-present-value').show;
		jQuery('.track2-data-present-value').html(data.track2DataServiceCode);
	}
	if (typeof data.chipCardPresent != undefined && data.chipCardPresent == null) {			
		jQuery('.chip-card-present-value').hide();		
	}else{		
		jQuery('.chip-card-present-value').show;
		jQuery('.chip-card-present-value').html(data.chipCardPresent);
	}
	
}
function showHideProperForm(data) {

    //set value for transactionCode hidden element to be used later in validation
    setHtmlOrInputValue(jQuery(".transactionCode"), data.transactionCode);
	jQuery("#visa-service-request").hide();
	jQuery("#mastercard-service-request").hide();
	jQuery("#atm-pos-service-request").hide();
	jQuery("#atm-deposit-service-request").hide();	
	jQuery(".visa-transaction-details-class").hide();
	jQuery(".mastercard-additional-transaction-info-fieldset").hide();
	if (typeof data.visaOrAtmPos != undefined && null !== data.visaOrAtmPos) {		
		if (data.visaOrAtmPos == "VISA") {		
			jQuery(".visa-transaction-details-class").show();
			jQuery("#visa-service-request").show();
			jQuery(".mastercard-additional-transaction-info-fieldset").hide();
		} else if(data.visaOrAtmPos == "MASTERCARD_IPM") {
			jQuery("#mastercard-service-request").show();
			setAdditionalTransactionInformation(data);
			jQuery(".mastercard-additional-transaction-info-fieldset").show();		
			jQuery(".visa-transaction-details-class").hide();
			jQuery("#atm-pos-service-request").hide();
		} else {
			jQuery(".visa-transaction-details-class").hide();
			jQuery(".mastercard-additional-transaction-info-fieldset").hide();
			jQuery("#atm-pos-service-request").show();

			if (data.visaOrAtmPos == "ATM" || data.visaOrAtmPos == "BILATERAL ATM") {
				setHtmlOrInputValue(jQuery(".atm-fee"), data.requestDetails.feeAmt);
				jQuery(".atm-fee").val(data.requestDetails.feeAmt);
			
				jQuery("#posForm").hide();
				jQuery("#atmForm").show();
				jQuery("#file-mandatory").hide();
				setHtmlOrInputValue(jQuery(".amount-received-label span"), "Amount Received<em>*</em>");

				//cash or cheque deposit
				if(data.visaOrAtmPos == "ATM" && (data.transactionCode == 21 || data.transactionCode == 24)){
					jQuery("#atm-pos-service-request").hide();	
					jQuery("#atm-deposit-service-request").show();
				}
				
			} else {
				jQuery("#atm-fee-div").hide();
				jQuery(".mastercard-additional-transaction-info-fieldset").hide();
				jQuery("#atmForm").hide();
				jQuery("#posForm").show();
			}
		}
	} else {
		jQuery("#visa-service-request").hide();
		jQuery("#mastercard-service-request").hide();
		jQuery("#atm-pos-service-request").hide();
		jQuery("#atm-deposit-service-request").hide();
		jQuery(".mastercard-additional-transaction-info-fieldset").hide();
	}	
}

function populateDropDowns(data, selectForm){	
	jQuery.each(data, function(key, value){		
		jQuery(selectForm).append(jQuery("<option/>").val(key).text(value));		
	});
}

function populateServiceTypeDropDowns(data, selectForm){
	jQuery.each(data, function(key, value){		
		if(selectForm === "#visa-request-type"){			
			if (key === 1 || key === "1") {				
				jQuery(selectForm).append(jQuery("<option/>").val(key).text(value));
			}else if (key === 2 || key === "2") {				
				jQuery(selectForm).append(jQuery("<option/>").val(key).text(value));
			}else if (key === 3 || key === "3") {				
				jQuery(selectForm).append(jQuery("<option/>").val(key).text(value));
			}		
		}else if (selectForm === "#mastercard-request-type"){				
			if (key === 4 || key === "4") {		
				jQuery(selectForm).append(jQuery("<option/>").val(key).text(value));
			}		
		}
	});	
}

function populateButtonsForPortlets(data, selectForm){
	jQuery.each(data, function(key, value){
		//Do this only for the Visa Chargeback option which is option 3.		
		if (key === 3 || key === "3") {
			if (value.indexOf("&amp;") > -1) {				
				value = value.replace(/&amp;/g, "&");				
			}
			
			if (jQuery("#vc-form>.next-button").length === 0) {
				jQuery("<a/>")
					.addClass("next-button no-print")
					.attr("href", value)
					.attr("data-senna-off", true)
					.html("Continue")
					.appendTo("#vc-form");
			}
		}
		//Do this only for the MasterCard Dispute option which is option 4.
		if (key === 4 || key === "4") {				
			if (value.indexOf("&amp;") > -1) {				
				value = value.replace(/&amp;/g, "&");				
			}			
			if (jQuery("#mastercard-dispute-form>.next-button").length === 0) {				
				jQuery("<a/>")
					.addClass("next-button no-print")
					.attr("href", value)
					.attr("data-senna-off", true)
					.html("Continue")
					.appendTo("#mastercard-dispute-form");
			}else {
				jQuery("#mastercard-dispute-form>.next-button").attr("href",value);
			}			
		}		
	});
}

/* Service Requests functions */
/* Display the appropriate error messages for the service request form */
function createErrorDiv(data) {

	jQuery(".trans-print").hide();
	clearAllPageErrors();
	for (var i = 0; i < data.result.length; i++) {
	    
		switch(data.result[i].field) {
			case "contactInformation.givenName" :
				jQuery("#given-name-error").html(data.result[i].code);
				break;
			case "contactInformation.surname" :
				jQuery("#contact-surname-error").html(data.result[i].code);
				break;
			case "contactInformation.email" :
				jQuery("#contact-email-error").html(data.result[i].code);
				break;
			case "contactInformation.email.invalid" :
				jQuery("#contact-email-error").html(data.result[i].code);
				break;
			case "contactInformation.phoneNumber" :
				jQuery("#contact-phone-error").html(data.result[i].code);
				break;
			case "contactInformation.phoneNumber.invalid" :
				jQuery("#contact-phone-error").html(data.result[i].code);
				break;
			case "visaTransactionInformation.fraudType" :
				jQuery("#fraudtype-error").html(data.result[i].code);
				break;
			case "visaTransactionInformation.fraudNotificationCode" :
				jQuery("#notificationcode-error").html(data.result[i].code);
				break;
			case "visaTransactionInformation.detection" :
				jQuery("#detection-error").html(data.result[i].code);
				break;
			case "visaTransactionInformation.requestType" :
				jQuery("#requesttype-error").html(data.result[i].code);
				break;
			case "visaTransactionInformation.voucherReason" :
				jQuery("#voucherreason-error").html(data.result[i].code);
				break;
			case "visaTransactionInformation.comments" :
				jQuery("#visa-comments").html(data.result[i].code);
				break;
			case "requestType" :
				jQuery("#error-message-txdetails").empty();
				jQuery(data.result[i].code).appendTo("#error-message-txdetails");
				jQuery("<span>The service request could not be logged for the following reason(s):" +
						"</span><br />" + data.result[i].code).appendTo("#error-message-txdetails");
				jQuery("#error-message-txdetails").show();
				break;
			case "visaTransactionInformation.errorMsg" :
				jQuery("#error-message-txdetails").empty();
				var error = "<span>The service request could not be logged for the following reason(s):<br />  " +
						"&#8226; " + data.result[i].code + "</span>";
				jQuery(error).appendTo("#error-message-txdetails");
				jQuery("#error-message-txdetails").show();
				break;
			case "disclaimer" :
				jQuery("#disclaimer-error").html(data.result[i].code);
				break;
				
			default:
				break;
		}
	}
}

function getTransactionTickets(url) {
		
	jQuery.ajax({
		type	: "post",
		url		: url,
		data	: {
			trans	: jQuery("#service-requests").attr("trans"),
			dat		: jQuery("#service-requests").attr("dat")
		},
		success	: function(data) {
			jQuery("table.transaction-service-requests tbody").empty();
			var ticketDetails = json_parse(data);
			var failedMessage = "An error has occurred while trying to process your request. If this error continues, please contact "+
				"<a class='normal-link' href='mailto:calldirect@cuscal.com.au'>CallDirect</a> on <span class='nowrap'>1300 650 501</span>.";
			if (ticketDetails != null) {
				//Service Request addition failed.
				if (ticketDetails.status == "FAIL") {
					jQuery("table.transaction-service-requests thead").empty();
					jQuery("table.transaction-service-requests tbody").empty();
					var row = jQuery("<tr></tr>")
								.appendTo("table.transaction-service-requests tbody");
					jQuery("<td></td>")
						.attr("colSpan", "6")
						.css({
							"text-align" : "center",
							"font-weight": "bold"
						})
						.html(failedMessage)
						.appendTo(row);
					jQuery("#atm-pos-service-request").hide();
					jQuery("#visa-service-request").hide();
					jQuery("#mastercard-service-request").hide();
					jQuery("#transaction-details-wrapper").fadeOut(300, function() {
						jQuery("#service-requests").fadeIn(300);
					});
				} else {
					//Request addition was a success.
					createTicketDetailsScreen(ticketDetails);
					jQuery("#transaction-details-wrapper").fadeOut(300, function() {
						jQuery("#service-requests").fadeIn(300);
					});
				}
			} else {
				//Ticket Details object returned is null
				jQuery("table.transaction-service-requests thead").empty();
				jQuery("table.transaction-service-requests tbody").empty();
				var row = jQuery("<tr></tr>")
					.appendTo("table.transaction-service-requests tbody");
				jQuery("<td></td>")
					.attr("colSpan", "6")
					.html(failedMessage)
					.appendTo(row);
				jQuery("#atm-pos-service-request").hide();
				jQuery("#visa-service-request").hide();
				jQuery("#mastercard-service-request").hide();
				jQuery("#transaction-details-wrapper").fadeOut(300, function() {
					jQuery("#service-requests").fadeIn(300);
				});
			}
		},
		error	: function() {
			//Could not call the WS at all.
			jQuery("table.transaction-service-requests thead").empty();
			jQuery("table.transaction-service-requests tbody").empty();
			var row = jQuery("<tr></tr>")
				.appendTo("table.transaction-tickets tbody");
			jQuery("<td></td>")
				.attr("colSpan", "6")
				.html(failedMessage)
				.appendTo(row);
			jQuery("#atm-pos-service-request").hide();
			jQuery("#visa-service-request").hide();
			jQuery("#mastercard-service-request").hide();
			jQuery("#transaction-details-wrapper").fadeOut(300, function() {
				jQuery("#service-requests").fadeIn(300);
			});
		}
	});
}

function createTicketDetailsScreen(data) {
	jQuery("table.transaction-service-requests tbody").empty();
	jQuery("#vc-form>.portlet-msg-info").remove();	
	if (isEmpty(data.tickets)) {
		var row = jQuery("<tr></tr>").appendTo("table.transaction-service-requests tbody");
		
		jQuery("<td></td>")
			.css({
				"text-align"	:	"center"
			})
			.attr("colSpan", "6")
			.appendTo(row)
			.html("There are currently no Service Requests associated with this transaction");
		jQuery(".portlet-msg-error").hide();
	} else {
		var index = 0;
		var chargebackRaised = false;
		
		var ticketStatus="";
		jQuery.each(data.tickets, function(count, entry) {
			index = index + 1;
			var row = jQuery("<tr></tr>").appendTo("table.transaction-service-requests tbody");
			if (index % 2 == 0) {
				row.addClass("even");
			} else {
				row.addClass("odd");
			}			
			var linkToTicketDetails = "<a data-senna-off=\"true\" href='" + entry.ticketDetailsLink +"'>" + entry.ticketNumber +"</a>";
			
			jQuery("<td></td>")
				.addClass(".ticket-call-number")
				.appendTo(row)
				.html(linkToTicketDetails);

			jQuery("<td></td>")
				.addClass("ticket-request-type")
				.appendTo(row)
				.html(entry.ticketCategory);

			jQuery("<td></td>")
				.addClass("ticket-submitted-by")
				.appendTo(row)
				.html(entry.ticketFirstName + " " + entry.ticketLastName);

			jQuery("<td></td>")
				.addClass("ticet-submitted-date")
				.appendTo(row)
				.html(entry.ticketSubmittedDate);
	
			jQuery("<td></td>")
				.addClass("ticket-update-date")
				.appendTo(row)
				.html(entry.ticketUpdateDate);
		
			jQuery("<td></td>")
				.addClass("ticket-status")
				.appendTo(row)
				.html(entry.ticketStatus);	
			//EFTPOS change for Project 8854
			if(entry.ticketStatus == "Closed" || entry.ticketStatus == "Open" || entry.ticketStatus == "Pending" || entry.ticketStatus == "On Hold"){
				ticketStatus = "hide";
			}//end changes
			
			if (entry.ticketCategory === "Visa Chargeback Request" || entry.ticketCategory === "Visa Chargeback") {
				chargebackRaised = true;
			}
		});
		
		//EFTPOS change for Project 8854
		if(ticketStatus == "hide"){
			jQuery("#atm-pos-service-request").hide();
			jQuery("#serviceReqText").hide();
			jQuery("#serviceReqTextSpan").hide();
		}//end changes
		if (typeof data.existingTC40 != "undefined"){
			setHtmlOrInputValue(jQuery(".outstandingTC40"), data.existingTC40);
		}
		if (typeof data.existingTC52 != "undefined"){
			setHtmlOrInputValue(jQuery(".outstandingTC52"), data.existingTC52);
		}
		if (typeof data.existingDispute != "undefined"){
			setHtmlOrInputValue(jQuery(".outstandingDispute"), data.existingDispute);
		}
		if (typeof data.existingClosedDispute != "undefined"){
			setHtmlOrInputValue(jQuery(".outstandingClosedDispute"), data.existingClosedDispute);
		}		
		if (typeof data.existingClosedDispute != "existingReInvestigation"){
			setHtmlOrInputValue(jQuery(".outstandingReinvestigation"), data.existingReinvestigation);
		}
		
		var existingMasterCardDisputeRetrievalRequest = data.existingMasterCardDisputeRetrievalRequest;
		var existingMasterCardDisputeFirstChargeback = data.existingMasterCardDisputeFirstChargeback;
		var existingMasterCardDisputeArbitrationChargeback = data.existingMasterCardDisputeArbitrationChargeback;	
		var existingMasterCardDisputeReportFraud = data.existingMasterCardDisputeReportFraud;	
		var displayMasterCardDispute = data.displayMasterCardDispute;
		var isMasterCardDispute  = data.isMasterCardDispute;
		if (typeof existingMasterCardDisputeRetrievalRequest != "undefined"){
			setHtmlOrInputValue(jQuery(".outstandingMasterCardDisputeRetrievalRequest"), existingMasterCardDisputeRetrievalRequest);
		}
		if (typeof existingMasterCardDisputeFirstChargeback != "undefined"){
			setHtmlOrInputValue(jQuery(".outstandingMasterCardDisputeFirstChargeback"), existingMasterCardDisputeFirstChargeback);
		}
		if (typeof existingMasterCardDisputeArbitrationChargeback != "undefined"){
			setHtmlOrInputValue(jQuery(".outstandingMasterCardDisputeArbitrationChargeback"), existingMasterCardDisputeArbitrationChargeback);
		}		
		if (typeof data.existingMasterCardDisputeArbitrationChargeback != "undefined"){
			setHtmlOrInputValue(jQuery(".existingMasterCardDisputeArbitrationChargeback"), data.existingMasterCardDisputeArbitrationChargeback);
		}
		if (typeof existingMasterCardDisputeReportFraud != "undefined"){
			setHtmlOrInputValue(jQuery(".outstandingMasterCardDisputeReportFraud"), existingMasterCardDisputeReportFraud);
		}
		
		var disputeButtonValue = jQuery("#mastercard-dispute-form>.next-button").attr('href');			
		disputeButtonValue = disputeButtonValue + "&outstandingMasterCardDisputeRetrievalRequest="+jQuery(".outstandingMasterCardDisputeRetrievalRequest").val();
		disputeButtonValue = disputeButtonValue + "&outstandingMasterCardDisputeFirstChargeback="+jQuery(".outstandingMasterCardDisputeFirstChargeback").val();
		disputeButtonValue = disputeButtonValue + "&outstandingMasterCardDisputeArbitrationChargeback="+jQuery(".outstandingMasterCardDisputeArbitrationChargeback").val();	
		disputeButtonValue = disputeButtonValue + "&outstandingMasterCardDisputeReportFraud="+jQuery(".outstandingMasterCardDisputeReportFraud").val();	
		jQuery("#mastercard-dispute-form>.next-button").attr("href",disputeButtonValue);	
		
	    if(isMasterCardDispute){
	    	//restrict multiple mastercard dipsute submission
	    	if(false === displayMasterCardDispute){
				jQuery("#info-message-txdetails").empty();
				var text = "<span>A servcie request has already been submitted.</span>";			
				jQuery(text).appendTo("#info-message-txdetails");
				jQuery("#info-message-txdetails").show();
				jQuery("#request-form").hide();
				return false;
	    	}
	    	
	    }
	    else { 			
	    	if (index >= 3) {
				jQuery("#info-message-txdetails").empty();
				var text = "<span>This transaction is ineligible for further online lodgement of service requests." +
							"<br />To lodge a service request regarding this transaction, please contact " +
							"<a class='normal-link' href='mailto:calldirect@cuscal.com.au'>CallDirect</a> on " +
							"<span class='nowrap'>1300 650 501.</span></span>";
				
				jQuery(text)
						.appendTo("#info-message-txdetails");
				jQuery("#info-message-txdetails").show();
				jQuery("#request-form").hide();
				return false;
	    	} else {
	    		jQuery("#vc-form>.portlet-msg-info").remove();
				if (chargebackRaised) {
					jQuery("#vc-form>.next-button").remove();					
					jQuery("#vc-form").css("width", "auto");
					jQuery("<p/>")
						.addClass("portlet-msg-info")
						.html("A chargeback has already been lodged for this transaction, so another cannot be raised.")
						.appendTo(jQuery("#vc-form"));
				}
	    	}
	    }
	}
}
/* End Service Request functions */

// Default search function.
function filterTxByDates(url) {
	jQuery("#transactionForm").attr("action", url);
	jQuery("#transactionForm").submit();
}

function clearATMPageErrors() {	
	jQuery('.atm-request-dispute-type-error').empty("");	
	jQuery('.atm-request-amount-received-error').empty("");	
	jQuery('.atm-request-atm-reason-error').empty("");	
	jQuery('.atm-request-comments-error').empty("");	
	jQuery('.atm-request-attachments-error').empty("");		
	jQuery('.atm-disclaimer-error').empty("");
}

function clearAllPageErrors() {
    jQuery('#given-name-error').empty("");
	jQuery('#contact-surname-error').empty("");	
	jQuery('#contact-email-error').empty("");	
	jQuery('#contact-phone-error').empty("");	
	jQuery('#fraudtype-error').empty("");	
	jQuery('#notificationcode-error').empty("");	
	jQuery('#detection-error').empty("");	
	jQuery('#requesttype-error').empty("");
	jQuery('#voucherreason-error').empty("");
	jQuery('#visa-comments').empty("");
	jQuery('#disclaimer-error').empty("");
}

function clearAll(url) {
	jQuery('#panBin').val("");
	jQuery('#terminalId').val("");
	jQuery('#merchantId').val("");
	jQuery('#startDate').val("");
	jQuery('#endDate').val("");
	jQuery('#amount').val("");
	jQuery('#responseCode').val("");
	jQuery('#messageCode').val("");
	jQuery('#startTimeHr').val("");
	jQuery('#startTimeMin').val("");
	
	jQuery('#startTimeHr').val("00");
	jQuery('#startTimeMin').val("00");		
	jQuery('#endTimeHr').val("24");
	jQuery('#endTimeMin').val("00");
	jQuery('#conditions').val("=");
	
	jQuery("#transactionForm").attr("action", url);
	jQuery("#transactionForm").submit();
	
}

function showHideSearchForm(){
	jQuery("#searchSelect").toggle();
	jQuery("#transactionSearch").toggle(
			"slow",
			function() {
				if (jQuery("#transactionSearch").css("display") == "block") {
					jQuery("#show-hide-search").html("Hide search form");

					//ensure the quick/advanced search radio is checked.
					if (jQuery("input:radio[name='searchView']:checked").val()=="" ||
							jQuery("input:radio[name='searchView']:checked").val()==undefined){
						jQuery("input:radio[name='searchView']").attr('checked', 'checked');
					}
				} else {
					jQuery("#show-hide-search").html("Show search form");
				}
			});
}

function open2FAPageTxDet(url) {
	jQuery("#transactionForm").attr("action", url);
	jQuery("#transactionForm").submit();
}

function followPan(pan, url) {
	//jQuery("#transactionForm").attr("action", "followPan");
	setHtmlOrInputValue(jQuery('#terminalId'), "");
	setHtmlOrInputValue(jQuery('#merchantId'), "");
	setHtmlOrInputValue(jQuery('#cuscalId'), "");
	setHtmlOrInputValue(jQuery('#amount'), "");
	setHtmlOrInputValue(jQuery('#responseCode'), "");
	setHtmlOrInputValue(jQuery('#messageCode'), "");
	setHtmlOrInputValue(jQuery("#panBin"), pan);
	jQuery("#transactionForm").attr("action", url);
	jQuery("#transactionForm").submit();
}

function openMessageResponseCodePage(divId,tableId,url) {
	jQuery.ajax({
		type 	: "post",
		url 	: url,
		success : function(data) {
			var failedMessage = "";
			if (divId == "responseCodeDiv")  {
				failedMessage = "<tr><td>An error has occurred while trying to process your request. If this error continues, please contact "+
					"<a class='normal-link' href='mailto:calldirect@cuscal.com.au'>CallDirect</a> on <span class='nowrap'>1300 650 501</span>.</td></tr>";
			} else {
				failedMessage = "<p class='error-message'>An error has occurred while trying to process your request. If this error continues, please contact "+
					"<a class='normal-link' href='mailto:calldirect@cuscal.com.au'>CallDirect</a> on <span class='nowrap'>1300 650 501</span>.</p>";
			}
			
			if(data != "") {
				jQuery("#" + tableId).html(data);
			} else {
				jQuery("#" + tableId).html(failedMessage);
			}
						
			openModalWindow(divId, 826);
		}
	});

}

function selectMsgOrRespCodeId(codeId,codeType){
	if(codeType == "response"){
		jQuery("#responseCode").val(codeId);
	}else{
		jQuery("#messageCode").val(codeId);
	}
	jQuery.modal.close();
}

function printDetails(url){
	jQuery.ajax({
		type 	: "post",
		url 	: url,
		success : function() {
			jQuery("#pan").hide();
			jQuery("#showPan").hide();
			jQuery(".masked-pan").show();
			jQuery("#loading").hide();
			jQuery(".printable").print();
			jQuery(".masked-pan").hide();
			jQuery("#showPan").show();
			jQuery("#pan").show();
		}
    });
	
}

function printRequest(url) {
	jQuery.ajax({
		url 	: 	url,
		type	:	"post",
		success	:	function() {
			jQuery(".printable").hide();
			jQuery(".print-request a").each(function() {
				jQuery(this).hide();
			});
			jQuery("#loading").hide();
			//jQuery(".print-request").print();
			window.print();
			jQuery(".print-request a").each(function() {
				jQuery(this).show();
			});	
		}
	});
	
	/*jQuery(".printable").hide();
	jQuery(".print-request a").each(function() {
		jQuery(this).hide();
	});
	jQuery(".print-request").print();
	jQuery(".print-request a").each(function() {
		jQuery(this).show();
	});	*/
}

/**
 * Open the modal div.
 * 
 * @param divId
 */
function openModalWindow(divId, minimumWidth) {
	var modalDiv = jQuery("#" + divId);
	var windowHeight = window.innerHeight;
	var modalHeight = modalDiv.height();
	
	if (minimumWidth == 0 ||
			minimumWidth == "" ||
			minimumWidth == undefined) {
		minimumWidth = 200;
	}
	
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
		minWidth	: minimumWidth, //826
		closeClass 	: "modal-close",
		onShow		: function(dialog) {
			// Update the width of the children div for IE and update the
			// max-height of the modal div in the case of other browsers.
			if (checkIE()) {
				if (modalDiv.height() > windowHeight) {
					modalDiv.children().each(function() {
						jQuery(this).width(jQuery(this).width() - 16);
					});
				}
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
	if (jQuery("#atm-pos-service-request").is(":visible") 
			|| jQuery("#success-message").is(":visible")){
		
		jQuery("<input>").attr({
		    type: "hidden",
		    name: "close"
		}).appendTo("#service-request-form-atmpos");
		
		jQuery("#service-request-form-atmpos").submit();  
	} else {
		jQuery.modal.close();
	}
}

function addNoPrintClass() {
	jQuery("#pan,a,input,table.cuscalTable,#footer,#navigation,span.pagination-text,div#transaction-search-form,#transactionForm p").addClass("no-print");
}

function removeNoPrintClass() {
	jQuery("#pan,a,input,table.cuscalTable,#footer,#navigation,span.pagination-text,div#transaction-search-form,#transactionForm p").removeClass("no-print");
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

/* Set the HTML object value or the input object Value */
function setHtmlOrInputValue(object, value) {
	// Reset the value to be empty instead of null. IE was setting the text box
	// values to be null and this was passed back with the service request.
	value = (null === value ? "" : value);
	object.each(function() {
		if (jQuery(this).is("input")) {
			jQuery(this).val(value);
		} else {
			jQuery(this).html(value);
		}
	});
}
function formatNumber(number, allowNegative){
	if (number !== "" && number !== "undefined"){
		if (allowNegative !== "undefined" && allowNegative === "Y"){
			number = number.replace(/[A-Za-z$,]/g, "");	
		}else{
			number = number.replace(/[A-Za-z$,-]/g, "");
		}
		number = parseFloat(number, 10);
	} else {
		number = parseFloat(0, 10);
	}
	return number;
}

/* Format the amount value. So 10 will become 10.00 */
function format(n) {
	return isNaN(n)? "0.00" : n.toFixed(2).replace(/./g, function(c, i, a) {
		return i && c !== "." && !((a.length - i) % 3) ? "," + c : c;
	});
}

function isValidDate(date){
	if(/^(0[1-9]|1\d|2\d|3[01])\/(0[1-9]|1[0-2])\/(19|20)\d{2}$/.test(date))
        return true;
	return false;
	}

//convert date from dd/mm/yyyy format to Date object
function convertToDate(date){
	date = date.split('/');
	return new Date(date[2],date[1]-1,date[0]);
}