//TODO: Remove all console.log lines before promoting.
var allMemberFieds,
	whatWasPurchasedList,
	animationTime = 300, //Time in ms.
	initialMemberMessageOption = new Option("Please select", "0");

Liferay.on('allPortletsReady', function() {
	
	// setupLoader();
	jQuery("#chargeback-form").submit(function() {
		jQuery("#loading").show();
	});
	
	jQuery(document).ajaxStart(function() {
		jQuery("#loading").show();
	}).ajaxStop(function() {
		jQuery("#loading").hide();
	});
	
	//var hasRun = false;
	
	if (jQuery("html").hasClass("ie8") || jQuery("html").hasClass("ie7")) {
		runDocumentReadyStatements();
	} else {
		var renderUrl = jQuery(".cancellation-info-section").attr("data-url");
		if (renderUrl.indexOf("&amp;", 0) > -1) {
			renderUrl = renderUrl.replace(/&amp;/g, "&");
		}

		var ticketsId = jQuery(".cancellation-info-section").attr("data-id");
		
		jQuery.ajax({
			type		:	"get",
			url			:	renderUrl,
			data		:	{
				ticketId	:	ticketsId
			},
			success		:	function(resp) {			
				var tempDiv = jQuery("<div />").html(resp);
				var neededHtml = tempDiv.find("#cancellationInfo");
				jQuery("#cancellation-section").html(neededHtml);
				tempDiv.remove();
				
				runDocumentReadyStatements();
			},
			error		:	function() {
				return;
			}
		}); // End jQuery.ajax().fail
	}
}); // End document.load


/**
 * 
 */
function runDocumentReadyStatements() {
	//call the rest of the work here.	
	whatWasPurchasedList = jQuery("#whatWasPurchased option");
	//allMemberFields = jQuery("#memberMessage option");

	//reset member message options to Please select.
	//resetMemberMessageOptions();
	hideAllFields();
	jQuery("#chargebackType").change(function() {
		//Reset everything every time the chargeback type is changed. 
		//resetMemberMessageOptions();
		hideAllFields();
		hideReasonCodes();
		jQuery("#reasonCode").val("0");
		resetAllFormData();
		
		if (jQuery(this).val() === "0") {
			jQuery("#reasons").fadeOut(animationTime);
			jQuery.each(jQuery("select"), function(i, data){
				//jQuery("option[value=0]").attr("selected", "selected");
				data.options[0].selected = "selected";
			});
		} else {
			populateReasonCodes();
			jQuery("#reasons").fadeIn(animationTime);
		}
	});
	
	//Set the page when there is an error and the page is reloaded.
	if (jQuery("#chargebackType").val() !== "0") {
		var value = jQuery("#chargebackType").val();
		hideReasonCodes();
		populateReasonCodes();
		
		if (value === "44") {
			loadScreenFieldsForRFI();
		} else if (value === "45") {
			loadScreenFieldsForFraud();
		} else if (value === "46") {
			loadScreenFieldsForAuthorisation();
		} else if (value === "47") {
			loadScreenFieldsForProcessingError();
		} else if (value === "48") {
			loadScreenFieldsForCancelled();
		} else if (value === "49") {
			loadScreenFieldsForQuestionnaire();
		}
		
		jQuery("#reasons").fadeIn(animationTime);
	}
	
	//Chargeback Reason for information
	jQuery("#cbReasonRFI").change(function() {
		hideAllFields();
		resetAllFormData();
		
		jQuery("#reasonCode").val("0");
		loadScreenFieldsForRFI();
	});
	
	//Chargeback Fraud
	jQuery("#cbReasonFraud").change(function() {
		hideAllFields();
		resetAllFormData();
		
		jQuery("#reasonCode").val("0");
		loadScreenFieldsForFraud();		
	});
	
	//Chargeback Authorisation
	jQuery("#cbReasonAuth").change(function() {
		hideAllFields();
		resetAllFormData();
		
		jQuery("#reasonCode").val("0");
		loadScreenFieldsForAuthorisation();
	});
	
	jQuery("#cbReasonProcessingError").change(function() {
		hideAllFields();
		resetAllFormData();
		
		jQuery("#reasonCode").val("0");
		loadScreenFieldsForProcessingError();
	});
	
	jQuery("#cbReasonCancelled").change(function() {
		hideAllFields();
		resetAllFormData();
		
		jQuery("#reasonCode").val("0");
		loadScreenFieldsForCancelled();
	});
	
	jQuery("#cbReasonNRFS").change(function() {
		hideAllFields();
		resetAllFormData();
		
		jQuery("#reasonCode").val("0");
		loadScreenFieldsForQuestionnaire();
	});
	
	/*jQuery("#memberMessage").change(function() {
		if (jQuery("#memberMessage option:selected").val() === "0") {
			jQuery("#updateMessage").val("");
		} else {
			jQuery("#updateMessage").val(jQuery("#memberMessage option:selected").text());
		}
	});*/
	
	jQuery(".date-pick").datePicker({
		clickInput 			: true,
		createButton 		: false,
		startDate 			: "01/01/2000",
		endDate				: (new Date()).asString(),
		showYearNavigation 	: true,
		verticalOffset 		: 20
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
	
	/**
	 * javascript to handle the select all checkboxes.
	 */
	jQuery("#elabAuthCrbRegion input[type='checkbox']").live("click", function() {
		if (jQuery(this).val() === "all") {
			if (jQuery(this).is(":checked")) {
				jQuery(this).siblings("[type=checkbox]").attr("checked", "checked");
			} else {
				jQuery(this).siblings("[type=checkbox]").removeAttr("checked");
			}
		} else {
			if (jQuery(this).siblings("#all").is(":checked")) {
				jQuery(this).siblings("#all").removeAttr("checked");
			}
		}
	});
	
	jQuery("#transExceedsAuthAmount").click(function() {
		if (jQuery(this).is(":checked")) {
			jQuery("#elabAuthTransExceedsAuthAmountExplain").stop().fadeIn(animationTime);
		} else {
			jQuery("#elabAuthTransExceedsAuthAmountExplain").stop().fadeOut(animationTime);
			jQuery("#elabAuthTransExceedsAuthAmountExplain").children("input[type=text]").val("");
		}
	});
	
	jQuery("#authIncorrectData").click(function() {
		if (jQuery(this).is(":checked")) {
			jQuery("#elabAuthIncorrectDataExplain").stop().fadeIn(animationTime);
		} else {
			jQuery("#elabAuthIncorrectDataExplain").stop().fadeOut(animationTime);
			jQuery("#elabAuthIncorrectDataExplain").children("input[type=text]").val("");
		}
	});
	
	jQuery("#authNonMatchingMCC").click(function() {
		if (jQuery(this).is(":checked")) {
			jQuery("#elabAuthMCCInClearing, " +
					"#elabAuthMCCInSystemAuthorisation").each(function() {
				jQuery(this).stop().fadeIn(animationTime);
			});
		} else {
			jQuery("#elabAuthMCCInClearing, " +
					"#elabAuthMCCInSystemAuthorisation").each(function() {
				jQuery(this).stop().fadeOut(animationTime);
				jQuery(this).children("input[type=text]").val("");
			});
		}
	});
	
	jQuery("#processingErrorTransactionCurrencyDifferentTransmitted").click(function() {
		if (jQuery(this).is(":checked")) {
			jQuery("#elabProcessingErrorCurrencyTransaction, " +
					"#elabProcessingErrorCurrencyTransmitted").each(function() {
				jQuery(this).stop().fadeIn(animationTime);
			});
		} else {
			jQuery("#elabProcessingErrorCurrencyTransaction, " +
					"#elabProcessingErrorCurrencyTransmitted").each(function() {
				jQuery(this).stop().fadeOut(animationTime);
				jQuery(this).children("input[type=text]").val("");
			});
		}
	});
	
	jQuery("#processingErrorTransactionCountryDifferent").click(function() {
		if (jQuery(this).is(":checked")) {
			jQuery("#elabProcessingErrorTransactionCountry, " +
					"#elabProcessingErrorPortalCountry").each(function() {
				jQuery(this).stop().fadeIn(animationTime);
			});
		} else {
			jQuery("#elabProcessingErrorTransactionCountry, " +
					"#elabProcessingErrorPortalCountry").each(function() {
				jQuery(this).stop().fadeOut(animationTime);
				jQuery(this).children("input[type=text]").val("");
			});
		}
	});
	
	jQuery("#resolveWithMerchant").change(function() {
		
		jQuery("#attemptDateContact," +
				"#attemptNameContact," +
				"#attemptMethodContact," +
				"#attemptMerchantResponse," +
				"#attemptNotApplicableLocalLaw," +
				"#attemptExplainWhyNotResolve").each(function() {
			jQuery(this).stop().fadeOut(animationTime);
		});
		
		if (jQuery(this).val() === "yes") {
			jQuery("#attemptDateContact," +
					"#attemptNameContact," +
					"#attemptMethodContact," +
					"#attemptMerchantResponse").each(function() {
				jQuery(this).stop().fadeIn(animationTime);
			});
			
		} else if (jQuery(this).val() === "no") {
			jQuery("#attemptNotApplicableLocalLaw," +
					"#attemptExplainWhyNotResolve").each(function() {
				jQuery(this).stop().fadeIn(animationTime);
			});
		} else {
			jQuery("#attemptDateContact," +
					"#attemptNameContact," +
					"#attemptMethodContact," +
					"#attemptMerchantResponse," +
					"#attemptNotApplicableLocalLaw," +
					"#attemptExplainWhyNotResolve").each(function() {
				jQuery(this).stop().fadeOut(animationTime);
			});
		}
	});
	
	jQuery("#infoIncorrect").change(function() {
		
		jQuery("#elabProcessingErrorAmountIncorrect," +
				"#elabProcessingErrorIncorrectAmountWas," +
				"#elabProcessingErrorIncorrectAmountShouldBe," +
				"#elabProcessingErrorNameOnVoucher").each(function() {
			jQuery(this).stop().fadeOut(animationTime);
		});
		
		if (jQuery(this).val() === "286") {
			jQuery("#elabProcessingErrorAmountIncorrect," +
					"#elabProcessingErrorIncorrectAmountWas," +
					"#elabProcessingErrorIncorrectAmountShouldBe").each(function() {
				jQuery(this).stop().fadeIn(animationTime);
			});
			
		} else if (jQuery(this).val() === "287") {
			jQuery("#elabProcessingErrorNameOnVoucher").each(function() {
				jQuery(this).stop().fadeIn(animationTime);
			});
		} else {
			jQuery("#elabProcessingErrorAmountIncorrect," +
					"#elabProcessingErrorIncorrectAmountWas," +
					"#elabProcessingErrorIncorrectAmountShouldBe," +
					"#elabProcessingErrorNameOnVoucher").each(function() {
				jQuery(this).stop().fadeOut(animationTime);
			});
		}
	});
	
	jQuery("#visaEuropeOnlyData").click(function() {
		if (jQuery(this).is(":checked")) {
			jQuery("#visaEuropeOnlyDate").stop().fadeIn(animationTime);
		} else {
			jQuery("#visaEuropeOnlyDate").stop().fadeOut(animationTime);
			jQuery("#visaEuropeOnlyDate").children("input[type=text]").val("");
		}
	});
	
	//Form 53
	onChangeForm53();
	
	//Form 85
	onChangeForm85();
	
	//Form 30
	onChangeForm30();
	
	jQuery("#visaEuropeOnly>label").css("display", "inline");
	//return true;
}

/**
 * 
 */
/*function resetMemberMessageOptions() {
	jQuery("#memberMessage option").remove();
	initialMemberMessageOption.selected = true;
	jQuery("#memberMessage").append(initialMemberMessageOption);
	jQuery("#updateMessage").val("");
}
*/
/**
 * 
 */
/*function addAllMemberMessageOptions() {
	jQuery("#memberMessage option").remove();
	jQuery("#memberMessage").append(allMemberFields);
	jQuery("#memberMessage option[value=0]").attr("selected", "selected");
}*/

/**
 * 
 * @param reasonList
 * @param reasonCode
 */
function setupPageForReasonCodeScreen(reasonList, reasonCode){
	jQuery(reasonList).val(reasonCode);
	
	if(reasonList=="#cbReasonFraud"){
		loadScreenFieldsForFraud();
	} else if (reasonList=="#cbReasonRFI"){
		loadScreenFieldsForRFI();
	}
}

/**
 * Request for Information section.
 */
function loadScreenFieldsForRFI(){
//	addAllMemberMessageOptions();
//	rePopulateMemberMessages([71]);
	
	var reasonCode = jQuery("#reasonCode").val();
	
	if (reasonCode === "0") {
		reasonCode = jQuery("#cbReasonRFI").val();
	}
	
	switch(reasonCode) {
		case "50":
			jQuery("#retrievalRequest").stop().fadeIn(animationTime);
			jQuery("#cbReasonRFI option[value=50]").attr("selected", true);
			break;
		case "0":
			break;
		default:
			;
	}
	
	jQuery("#reasonCode").val(reasonCode);
}

/**
 * Fraud section.
 */
function loadScreenFieldsForFraud(){
	var reasonCode = jQuery("#reasonCode").val();
	
	if (reasonCode === "0"
			|| reasonCode === "undefined" 
			|| reasonCode === ""
			|| reasonCode === null) {
		reasonCode = jQuery("#cbReasonFraud").val();
	}
	
	if (reasonCode == 52){ //reason code 57
		// This is not used at the moment but will come in later. Use this
		// format to generate the Member Message Texts.
//		addAllMemberMessageOptions();
//		rePopulateMemberMessages([71]);
		
		jQuery("#certCb200," +
				"#certCb201," +
				"#certCb202," +
				"#certCb203").each(function() {
			jQuery(this).show();
		}).show();
		
		jQuery("#retrievalRequest").stop().fadeIn(animationTime);
		jQuery("#certifications").stop().fadeIn(animationTime);  
		jQuery("#cbReasonFraud option[value=52]").attr("selected", true);		
	} else if (reasonCode == 51) { //Reason code 81
		jQuery("#certCb200," +
				"#certCb204," +
				"#certCb205," +
				"#certCb206," +
				"#certCb207," +
				"#certCb209").each(function() {
			jQuery(this).show();
		});
		
		
		jQuery("#elabFictitious," +
				"#fraudDate," +
				"#fraudAdviceNo," +
				"#cardDate," +
				"#cardStatus," +
				"#visaEuropeOnly").each(function() {
			jQuery(this).show();
		});
		
		jQuery("#retrievalRequest").stop().fadeIn(animationTime);
		jQuery("#certifications").stop().fadeIn(animationTime);
		jQuery("#elabInfoFraud").stop().fadeIn(animationTime);
		jQuery("#cbReasonFraud option[value=51]").attr("selected", true);
	} else if (reasonCode == 53) {		 // reason code 83		
		jQuery("#certCb200").show();
		
		jQuery("#elabMsrtt").show();
		jQuery("#elabFictitious").show();
		jQuery("#fraudDate").show();
		jQuery("#fraudAdviceNo").show();
		jQuery("#cardDate").show();
		jQuery("#cardStatus").show();
		jQuery("#visaEuropeOnly").show();
		
		jQuery("#retrievalRequest").stop().fadeIn(animationTime);
		jQuery("#certifications").stop().fadeIn(animationTime);
		jQuery("#elabInfoFraud").stop().fadeIn(animationTime);
		jQuery("#cbReasonFraud option[value=53]").attr("selected", true);
	} else if (reasonCode == 54) {// reason code 93
		jQuery("#fraudReporting").stop().fadeIn(animationTime);
		jQuery("#cbReasonFraud option[value=54]").attr("selected", true);
	} else if (reasonCode == 117) { //reason code 62
		jQuery("#certCb208," +
				"#certCb210," +
				"#certCb204," +
				"#certCb207," +
				"#certCb211").show();
		
		jQuery("#fraudDate," +
				"#fraudAdviceNo," +
				"#cardDate," +
				"#cardStatus," +
				"#visaEuropeOnly").each(function() {
			jQuery(this).show();
		});
		
		if (jQuery("#visaEuropeOnlyData").is(":checked")) {
			jQuery("#visaEuropeOnlyDate").stop().fadeIn(animationTime);
		}
		
		jQuery("#certifications").stop().fadeIn(animationTime);
		jQuery("#elabInfoFraud").stop().fadeIn(animationTime);
		jQuery("#cbReasonFraud option[value=117]").attr("selected", true);
	}
	
	jQuery("#reasonCode").val(reasonCode);
}

/**
 * Authorisation section.
 */
function loadScreenFieldsForAuthorisation() {
	var reasonCode = jQuery("#reasonCode").val();
	
	if (reasonCode === "0" 
			|| reasonCode === "undefined" 
			|| reasonCode === ""
			|| reasonCode === null) {
		reasonCode = jQuery("#cbReasonAuth").val();
	}
	
	jQuery("#elabInfoAuth>div").hide();
	switch(reasonCode) {
		case "55": //Reason code 70
			jQuery("#elabException," +
					"#elabAuthCrbRegion").each(function() {
				jQuery(this).show();
			});
			
			jQuery("#elabInfoAuth").stop().fadeIn(animationTime);
			jQuery("#cbReasonAuth option[value=55]").attr("selected", true);
			break;
			
		case "56": //Reason code 71
			//jQuery("#certCb213").show();
			//jQuery("#certifications").stop().fadeIn(animationTime);
			
			jQuery("#elabAuthDeclinedDate, #elabCardStatusChanged").each(function() {
				jQuery(this).show();
			});
			
			jQuery("#elabInfoAuth").stop().fadeIn(animationTime);
			jQuery("#cbReasonAuth option[value=56]").attr("selected", true);
			break;
			
		case "57": //Reason code 72
			jQuery("#elabAuthNoTransDate, " +
					"#elabAuthIncorrectData, " +
					"#elabAuthTransExceedsAuthAmount, " +
					"#elabAuthNonMatchingMCC, " +
					"#elabCardStatusChanged").each(function() {
				jQuery(this).show();
			});
			
			if (jQuery("#authIncorrectData").is(":checked")) {
				jQuery("#elabAuthIncorrectDataExplain").stop().fadeIn(animationTime);
			}
			
			if (jQuery("#transExceedsAuthAmount").is(":checked")) {
				jQuery("#elabAuthTransExceedsAuthAmountExplain").stop().fadeIn(animationTime);
			}
			
			if (jQuery("#authNonMatchingMCC").is(":checked")) {
				jQuery("#elabAuthMCCInClearing, " +
						"#elabAuthMCCInSystemAuthorisation").each(function() {
					jQuery(this).stop().fadeIn(animationTime);
				});
			}
			
			jQuery("#elabInfoAuth").stop().fadeIn(animationTime);
			jQuery("#cbReasonAuth option[value=57]").attr("selected", true);
			break;
			
		case "58": //Reason code 73
			jQuery("#elabCardExpiredDate, #elabCardStatusChanged").show();
			jQuery("#elabInfoAuth").stop().fadeIn(animationTime);
			jQuery("#cbReasonAuth option[value=58]").attr("selected", true);
			break;
			
		case "59":
			jQuery("#member-messages").fadeOut(animationTime);
			
			jQuery("#cbReasonAuth option[value=59]").attr("selected", true);
			break;
			
		case "0":
			break;
			
		default:
			;
	}
	
	jQuery("#reasonCode").val(reasonCode);
}

/**
 * Processing Error section.
 */
function loadScreenFieldsForProcessingError() {
	var reasonCode = jQuery("#reasonCode").val();
	
	if (reasonCode === "0" 
			|| reasonCode === "null" 
			|| reasonCode === undefined 
			|| reasonCode === "undefined" 
			|| reasonCode === "") {
		reasonCode = jQuery("#cbReasonProcessingError").val();
	}
	
	jQuery("#elabInfoProcessingError>div").hide();
	switch(reasonCode) {
		case "60": //Reason Code 74
//			addAllMemberMessageOptions();
//			rePopulateMemberMessages([93,94]);
			
			jQuery("#elabProcessingErrorTransDate, " +
					"#elabProcessingErrorSettleDate, " +
					"#elabProcessingErrorCounterfeit, " +
					"#elabProcessingErrorAccountClosed, " +
					"#elabProcessingErrorOtherFraud, " +
					"#elabProcessingErrorPresentmentOlder180Days, " +
					"#elabProcessingErrorCardStatusChanged, " +
					"#elabProcessingErrorTransProcessedAfterTransactionDate").each(function(){
				jQuery(this).show();
			});
			
			jQuery("#elabInfoProcessingError").stop().fadeIn(animationTime);
			jQuery("#cbReasonProcessingError option[value=60]").attr("selected", true);
			break;
		case "61": //Reason code 76
//			addAllMemberMessageOptions();
//			rePopulateMemberMessages([95,96,97,98,99,100,101,102,103]);
			
			jQuery("#elabProcessingErrorTransactionIncorrect, " +
					"#elabProcessingErrorTransactionCurrencyDifferentTransmitted, " +
					"#elabProcessingErrorTransactionCountryDifferent").each(function() {
				jQuery(this).show();
			});

			if (jQuery("#processingErrorTransactionCurrencyDifferentTransmitted").is(":checked")) {
				jQuery("#elabProcessingErrorCurrencyTransaction, " +
						"#elabProcessingErrorCurrencyTransmitted").each(function() {
					jQuery(this).show();
				});
			}
			
			if (jQuery("#processingErrorTransactionCountryDifferent").is(":checked")) {
				jQuery("#elabProcessingErrorTransactionCountry, " +
						"#elabProcessingErrorPortalCountry").each(function() {
					jQuery(this).show();
				});
			}
			
			jQuery("#certCb241").show();
			jQuery("#certCb376").show();
			jQuery("#certifications").stop().fadeIn(animationTime);
			
			jQuery("#elabInfoProcessingError").stop().fadeIn(animationTime);
			jQuery("#cbReasonProcessingError option[value=61]").attr("selected", true);
			break;
		case "62": //Reason code 77
//			addAllMemberMessageOptions();
//			rePopulateMemberMessages([104,105,106]);
			
			jQuery("#elabProcessingErrorNonMatchingAccountNumber," +
					"#elabProcessingErrorNonExistingAccount," +
					"#elabProcessingErrorAccountNumberExceptionFile").each(function() {
				jQuery(this).show();
			});
			
			jQuery("#elabInfoProcessingError").stop().fadeIn(animationTime);
			jQuery("#cbReasonProcessingError option[value=62]").attr("selected", true);
			break;
		case "63": //Reason code 80
			jQuery("#elabProcessingErrorInfoIncorrect").each(function() {
				jQuery(this).show();
			});
			
			displayIncorrectAmountOrCardNumber();
			
			jQuery("#elabInfoProcessingError").stop().fadeIn(animationTime);
			jQuery("#cbReasonProcessingError option[value=63]").attr("selected", true);
			break;
			
			break;
		case "64": //Reason code 82
			jQuery("#elabProcessingErrorARN, " +
					"#elabProcessingErrorTransactionDate").each(function() {
				jQuery(this).show();
			});
			
			jQuery("#elabInfoProcessingError").stop().fadeIn(animationTime);
			jQuery("#cbReasonProcessingError option[value=64]").attr("selected", true);
			break;
		case "65": //Reason code 86
			jQuery("#elabProcessingErrorPaymentMethod," +
					"#elabProcessingErrorProofOtherMeans").each(function() {
				jQuery(this).show();
			});
			
			jQuery("#attemptResolveWithMerchant").show();
			
			displayAttemptToResolve();
			
			jQuery("#elabInfoProcessingError").stop().fadeIn(animationTime);
			jQuery("#attemptToResolve").stop().fadeIn(animationTime);
			jQuery("#cbReasonProcessingError option[value=65]").attr("selected", true);
			break;
		case "0":
			break;
		
		default:
			;
	}

	jQuery("#reasonCode").val(reasonCode);
}

function loadScreenFieldsForCancelled() {
	var reasonCode = jQuery("#reasonCode").val();
	
	if (reasonCode === "0" 
			|| reasonCode === "null" 
			|| reasonCode === undefined 
			|| reasonCode === "undefined" 
			|| reasonCode === "") {
		reasonCode = jQuery("#cbReasonCancelled").val();
	}
	
	switch(reasonCode) {
		case "66": // Reason code 41
			
			jQuery("#cancellationRequiredOneOrMore," +
					"#cancellationDateRecurringTransCancelled," +
					"#cancellationDateAcquirerNotified," +
					"#cancellationReasonCancelled," +
					"#attemptResolveWithMerchant").each(function() {
				jQuery(this).show();
			});
			
			displayAttemptToResolve();

			jQuery("#cancellationInfo").stop().fadeIn(animationTime);
			jQuery("#attemptToResolve").stop().fadeIn(animationTime);
			jQuery("#cbReasonCancelled option[value=66]").attr("selected", true);
			break;
		case "67": //Reason code 53
			var whatWasPurchased = jQuery("#whatWasPurchased option:selected").val();			
			if (whatWasPurchased === "0" 
				|| whatWasPurchased === "null" 
				|| whatWasPurchased === undefined 
				|| whatWasPurchased === "undefined" 
				|| whatWasPurchased === "") {
				addAllPurchasedOptions("0");
			} else {
				addAllPurchasedOptions(whatWasPurchased);
			}
			
			removeUnwantedPurchasedOptions([334,335,336,337]);
			
			jQuery("#elabCancellationWhatWasPurchased").show();
			jQuery("#attemptResolveWithMerchant").show();
			jQuery("#elabCancellationDidYouCancel").show();
			
			jQuery("#returnInfoReturnDate").show();
			jQuery("#returnInfoReturnMethod").show();
			jQuery("#returnInfoMerchantReceivedOn").show();
			jQuery("#returnInfoReturnAuthorisationNumber").show();
			displayAttemptToResolve();
			loadForm53();
			
			jQuery("#elaborationInfoCancellation").stop().fadeIn(animationTime);
			jQuery("#attemptToResolve").stop().fadeIn(animationTime);
			jQuery("#cbReasonCancelled option[value=67]").attr("selected", true);
			break;
		case "68": //Reason code 85
			var whatWasPurchased = jQuery("#whatWasPurchased option:selected").val();			
			if (whatWasPurchased === "0" 
				|| whatWasPurchased === "null" 
				|| whatWasPurchased === undefined 
				|| whatWasPurchased === "undefined" 
				|| whatWasPurchased === "") {
				addAllPurchasedOptions("0");
			} else {
				addAllPurchasedOptions(whatWasPurchased);
			}
			
			removeUnwantedPurchasedOptions([295]);
			
			jQuery("#elabCancellationWhatWasPurchased").show();
			jQuery("#elabCancellationDidYouCancel").show();
			jQuery("#elabCancellationOriginalCreditNotAccepted").show();
			jQuery("#attemptResolveWithMerchant").show();
			jQuery("#creditInfoCreditVoucherGiven").show();
			
			jQuery("#returnInfoReturnDate").show();
			jQuery("#returnInfoReturnMethod").show();
			jQuery("#returnInfoMerchantReceivedOn").show();
			jQuery("#returnInfoReturnAuthorisationNumber").show();
			jQuery("#returnInfoReturnInstruction").show();
			displayAttemptToResolve();
			
			loadForm85();
			
			jQuery("#elaborationInfoCancellation").stop().fadeIn(animationTime);
			jQuery("#attemptToResolve").stop().fadeIn(animationTime);
			jQuery("#creditInformationFieldset").stop().fadeIn(animationTime);
			jQuery("#cbReasonCancelled option[value=68]").attr("selected", true);
			break;
		case "0":
			break;
		default:
			;
	}
	
	jQuery("#reasonCode").val(reasonCode);
}

/**
 * 
 */
function loadScreenFieldsForQuestionnaire() {
	var reasonCode = jQuery("#reasonCode").val();
	
	if (reasonCode === "0" 
			|| reasonCode === "null" 
			|| reasonCode === undefined 
			|| reasonCode === "undefined" 
			|| reasonCode === "") {
		reasonCode = jQuery("#cbReasonNRFS").val();
	}
	
	switch(reasonCode) {
		case "69": //Reason code 30
			jQuery("#elabQuestionnaireWhatWasPurchased").show();
			jQuery("#attemptResolveWithMerchant").show();
			displayAttemptToResolve();
			loadForm30();
			
			jQuery("#elaborationInfoQuestionnaire").stop().fadeIn(animationTime);
			jQuery("#attemptToResolve").stop().fadeIn(animationTime);
			jQuery("#cbReasonNRFS option[value=69]").attr("selected", true);
			break;
			
		case "70": //Reason code 90
			
			jQuery("#questionATMCashLoadInformation," +
					"#questionAmountRequested," +
					"#questionAmountReceived").each(function() {
						jQuery(this).show();
					});
			
			jQuery("#questionnaireInformation").stop().fadeIn(animationTime);
			jQuery("#cbReasonNRFS option[value=70]").attr("selected", true);
			break;
		case "0":
			break;
		default:
			;
	}
	
	jQuery("#reasonCode").val(reasonCode);
}

/**
 * 
 * @returns {String}
 */
function populateReasonCodes(){
		if (jQuery("#chargebackType").val() === "44") {
		jQuery("#cbReasonRFI").show();
		return "#cbReasonRFI";
	} else if (jQuery("#chargebackType").val() === "45") {
		jQuery("#cbReasonFraud").show();
		return "#cbReasonFraud";
	} else if (jQuery("#chargebackType").val() === "46") {
		jQuery("#cbReasonAuth").show();
		return "#cbReasonAuth";
	} else if (jQuery("#chargebackType").val() === "47") {
		jQuery("#cbReasonProcessingError").show();
		return "#cbReasonProcessingError";
	} else if (jQuery("#chargebackType").val() === "48") {
		jQuery("#cbReasonCancelled").show();
		return "#cbReasonCancelled";
	} else if (jQuery("#chargebackType").val() === "49") {
		jQuery("#cbReasonNRFS").show();
		return "#cbReasonNRFS";
	}
}

function addAllPurchasedOptions(optionToSelect) {
	jQuery("#whatWasPurchased").find("option").remove();
	jQuery("#whatWasPurchased").append(whatWasPurchasedList);
	if (optionToSelect === "0") {
		jQuery("#whatWasPurchased option[value=0]").attr("selected", true);
	} else {
		jQuery("#whatWasPurchased option[value=" + optionToSelect + "]").attr("selected", true);
	}
}

function removeUnwantedPurchasedOptions(codes) {
	var optionsToRemove = [];
	
	jQuery.each(codes, function(i, data) {
		if (i !== 0) {
			optionsToRemove += ",";
		}
		optionsToRemove += "[value~=" + data + "]";
	});
	
	jQuery("#whatWasPurchased " + optionsToRemove).remove();
}

/**
 * 
 */
/*function rePopulateMemberMessages(codes) {
	var messagesToKeep = "[value~=0]";
	
	jQuery.each(codes, function(i, data) {
		messagesToKeep += ",";
		messagesToKeep += "[value~=" + data + "]";
	});
	
	jQuery("#memberMessage option")
		.not(messagesToKeep)
		.remove();
}*/

function hideReasonCodes(){
	jQuery("#cbReasonRFI").hide();
	jQuery("#cbReasonFraud").hide();
	jQuery("#cbReasonAuth").hide();
	jQuery("#cbReasonProcessingError").hide();
	jQuery("#cbReasonNRFS").hide();
	jQuery("#cbReasonCancelled").hide();
	
	resetReasonCodes();
}


function resetReasonCodes() {
	jQuery("#cbReasonRFI").val("0");
	jQuery("#cbReasonFraud").val("0");
	jQuery("#cbReasonAuth").val("0");
	jQuery("#cbReasonProcessingError").val("0");
	jQuery("#cbReasonNRFS").val("0");
	jQuery("#cbReasonCancelled").val("0");
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

/**
 * 
 */
function resetCheckboxesAndRadio() {
	jQuery("input[type=checkbox]").removeAttr("checked");
	jQuery("input[type=radio]").removeAttr("checked");
}

function hideAllFields(){
	hideAllCancellationInfoFields();
	hideAllElabInfoFraudFields();
	hideAllElabInfoAuthFields();
	hideAllElabInfoProcessingErrorFields();
	hideAllElabInfoCancellationFields();
	hideAllElabInfoQuestionnaireFields();
	hideAllAttemptToResolveFields();
	hideAllReturnInformationFields();
	hideAllFraudReportingFields();
	hideAllQuestionnaireInfoFields();
	hideAllCreditInfoFields();
	jQuery("#retrievalRequest").hide();
	jQuery("#certifications").hide();
	jQuery("#certifications div[id*='certCb']").hide();
}

function hideAllElabInfoFraudFields(){
	jQuery("#elabInfoFraud").hide();
	jQuery("#elabInfoFraud").stop().fadeOut(animationTime, function() {
		jQuery("#elabInfoFraud>div").hide();
	});
}

function hideAllElabInfoAuthFields() {
	jQuery("#elabInfoAuth").stop().fadeOut(animationTime, function() {
		jQuery("#elabInfoAuth>div").hide();
	});
}

function hideAllElabInfoProcessingErrorFields() {
	jQuery("#elabInfoProcessingError").stop().fadeOut(animationTime, function() {
		jQuery("#elabInfoProcessingError>div").hide();
	});
}

function hideAllElabInfoCancellationFields() {
	/*jQuery("#elaborationInfoCancellation").stop().fadeOut(animationTime, function() {
		jQuery("#elaborationInfoCancellation>div").hide();
		jQuery("#merchandiseSection>div").hide();
		jQuery("#servicesSection>div").hide();
		jQuery("#merchandiseWhatWasWrongSection>div").hide();
		jQuery("#servicesWhatWasWrongSection>div").hide();
	});*/
	jQuery("#elaborationInfoCancellation").hide();
	jQuery("#elaborationInfoCancellation>div").hide();
	jQuery("#merchandiseSection>div").hide();
	jQuery("#servicesSection>div").hide();
	jQuery("#merchandiseWhatWasWrongSection>div").hide();
	jQuery("#servicesWhatWasWrongSection>div").hide();
	
}

function hideAllElabInfoQuestionnaireFields() {
	jQuery("#elaborationInfoQuestionnaire").stop().fadeOut(animationTime, function() {
		jQuery("#questionnaireServicesSection").hide();
		jQuery("#questionnaireServicesSection>div").hide();
		jQuery("#questionnaireMerchandiseSection").hide();
		jQuery("#questionnaireMerchandiseSection>div").hide();
	});
}

function hideAllAttemptToResolveFields() {
	jQuery("#attemptToResolve").stop().fadeOut(animationTime, function() {
		jQuery("#attemptToResolve>div").hide();
	});
}

function hideAllCreditInfoFields() {
	jQuery("#creditInformationFieldset").stop().fadeOut(animationTime, function() {
		jQuery(this).children("div").each(function() {
			jQuery(this).hide();
		});
	});
}
function hideAllReturnInformationFields() {
	jQuery("#returnInfo").stop().fadeOut(animationTime, function() {
		jQuery(this).find("div").each(function() {
			jQuery(this).hide();
		});
	});
}

function hideAllCancellationInfoFields() {
	jQuery("#cancellationInfo").stop().fadeOut(animationTime, function() {
		jQuery("#cancellationInfo>div").hide();
	});
}

function hideAllFraudReportingFields() {
	jQuery("#fraudReporting").stop().fadeOut(animationTime, function() {
		jQuery("#fraudReporting>div").hide();
	});
}

function hideAllQuestionnaireInfoFields() {
	jQuery("#questionnaireInformation").stop().fadeOut(animationTime, function() {
		jQuery("#questionnaireInformation>div").hide();
	});
}

function addMoreFiles(){
    var fileIndex = jQuery("#fileTable tr").children().length;
    fileIndex = fileIndex - 1;
    jQuery("#fileTable").append(
            '<tr><td>'+
            '   <input type="file" name="requestAttachments['+ fileIndex +']" />'+
            '</td></tr>');
}

function displayAttemptToResolve(){
	
	jQuery("#attemptDateContact," +
			"#attemptNameContact," +
			"#attemptMethodContact," +
			"#attemptMerchantResponse," +
			"#attemptNotApplicableLocalLaw," +
			"#attemptExplainWhyNotResolve").each(function() {
		jQuery(this).stop().fadeOut(animationTime);
	});
	
	if (jQuery("#resolveWithMerchant").val() === "yes") {
		jQuery("#attemptDateContact," +
				"#attemptNameContact," +
				"#attemptMethodContact," +
				"#attemptMerchantResponse").each(function() {
			jQuery(this).stop().fadeIn(animationTime);
		});
		
	} else if (jQuery("#resolveWithMerchant").val() === "no") {
		jQuery("#attemptNotApplicableLocalLaw," +
				"#attemptExplainWhyNotResolve").each(function() {
			jQuery(this).stop().fadeIn(animationTime);
		});
	} else {
		jQuery("#attemptDateContact," +
				"#attemptNameContact," +
				"#attemptMethodContact," +
				"#attemptMerchantResponse," +
				"#attemptNotApplicableLocalLaw," +
				"#attemptExplainWhyNotResolve").each(function() {
			jQuery(this).stop().fadeOut(animationTime);
		});
	}
}

function displayIncorrectAmountOrCardNumber() {
	
	jQuery("#elabProcessingErrorAmountIncorrect," +
			"#elabProcessingErrorIncorrectAmountWas," +
			"#elabProcessingErrorIncorrectAmountShouldBe," +
			"#elabProcessingErrorNameOnVoucher").each(function() {
		jQuery(this).stop().fadeOut(animationTime);
	});
	
	if (jQuery("#infoIncorrect").val() === "286") {
		jQuery("#elabProcessingErrorAmountIncorrect," +
				"#elabProcessingErrorIncorrectAmountWas," +
				"#elabProcessingErrorIncorrectAmountShouldBe").each(function() {
			jQuery(this).stop().fadeIn(animationTime);
		});
		
	} else if (jQuery("#infoIncorrect").val() === "287") {
		jQuery("#elabProcessingErrorNameOnVoucher").stop().fadeIn(animationTime);
	} else {
		jQuery("#elabProcessingErrorAmountIncorrect," +
				"#elabProcessingErrorIncorrectAmountWas," +
				"#elabProcessingErrorIncorrectAmountShouldBe," +
				"#elabProcessingErrorNameOnVoucher").each(function() {
			jQuery(this).stop().fadeOut(animationTime);
		});
	}
}