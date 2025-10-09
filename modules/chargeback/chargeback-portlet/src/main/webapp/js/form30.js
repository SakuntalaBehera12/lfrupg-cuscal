function onChangeForm30() {
	jQuery("#questionnaireWhatWasPurchased").change(function() {
		jQuery("#questionnaireMerchandiseSection," +
				"#questionnaireServicesSection").children().each(function() {
					jQuery(this).hide();
					jQuery(this).find("input[type=text]").val("");
					jQuery(this).find("textarea").val("");
					jQuery(this).find("input[type=checkbox]").removeAttr("checked");
					jQuery(this).find("select").each(function() {
						if (jQuery(this).find("option").is(":selected")) {
							jQuery(this).removeAttr("selected");
						}
					});
				});
		
		jQuery("#questionnaireMerchandiseSection, " +
				"#questionnaireServicesSection," +
				"#returnInfo," +
				"#cancellationInfo").stop().fadeOut(animationTime);
		
		switch(jQuery(this).val()) {
			case "0":
				break;
			case "362":
				jQuery("#elabQuestionnaireMerchandiseOrdered," +
						"#elabQuestionnaireMerchandiseDateExpected," +
						"#elabQuestionnaireMerchandiseNotReceived," +
						"#elabQuestionnaireMerchandiseAgreedLocation," +
						"#elabQuestionnaireWasMerchandiseCancelledNonReceipt").show();
				
				jQuery("#questionnaireMerchandiseSection").stop().fadeIn();
				break;
			case "295":
				jQuery("#elabQuestionnaireServicesOrdered," +
						"#elabQuestionnaireMerchantUnwilling," +
						"#elabQuestionnaireServicesDateExpected," +
						"#elabQuestionnaireServicesNotReceived," +
						"#elabQuestionnaireWasServiceCancelledNonReceipt").show();
				
				jQuery("#questionnaireServicesSection").stop().fadeIn(animationTime);
				break;
			default:;
		}
	});
	
	//Services not received checkbox.
	jQuery("#questionnaireServicesNotReceived").live("click", function() {
		if (jQuery(this).is(":checked")) {
			jQuery("#elabQuestionnaireServicesReceivedOn").stop().fadeIn(animationTime);
		} else {
			jQuery("#elabQuestionnaireServicesReceivedOn").stop().fadeOut(animationTime, function() {
				jQuery("#questionnaireServicesReceivedOn").val("");
				jQuery("#questionnaireMerchandiseReturn option[value=0]").attr("selected", "selected");
			});
		}
	});
	
	//Merchandise not received checkbox.
	jQuery("#questionnaireMerchandiseNotReceived").live("click", function() {
		if (jQuery(this).is(":checked")) {
			jQuery("#elabQuestionnaireMerchandiseReceivedOn," +
					"#elabQuestionnaireMerchandiseReturn").stop().fadeIn(animationTime);
		} else {
			jQuery("#elabQuestionnaireMerchandiseReceivedOn," +
					"#elabQuestionnaireMerchandiseReturn").stop().fadeOut(animationTime, function() {
				jQuery("#questionnaireMerchandiseReceivedOn").val("");
			});
		}
	});
	
	//Merchandise and Services cancellation section.
	jQuery("#questionnaireWasServicesCancelledNonReceipt," +
			"#questionnaireWasMerchandiseCancelledNonReceipt").change(function() {
		jQuery("#cancellationInfo").fadeOut(animationTime, function() {
			jQuery("#cancellationCancelledDate," +
					"#cancellationCodeText," +
					"#cancellationSpokeWith," +
					"#cancellationReasonCancelled").hide();
			
			jQuery(this).children().find("input[type=text]").val("");
			jQuery(this).children().find("textarea").val("");
			jQuery(this).children().find("input[type=checkbox]").removeAttr("checked");
			jQuery(this).children().find("select option[value=0]").attr("selected", "selected");
		});
		switch(jQuery(this).val()) {
			case "0":
				break;
			case "yes":
				jQuery("#cancellationCancelledDate," +
						"#cancellationCodeText," +
						"#cancellationSpokeWith," +
						"#cancellationReasonCancelled").show();
				
				jQuery("#cancellationInfo").stop().fadeIn(animationTime);
				break;
			case "no":
				break;
			case "not applicable":
				break;
			default:;
		}
	});
	
	jQuery("#questionnaireMerchandiseReturn").change(function() {
		jQuery("#elabQuestionnaireWhereIsMerchandise").stop().fadeOut(animationTime, function() {
			jQuery("#questionnaireWhereIsMerchandise").val("");
		});
		
		jQuery("#returnInfo").stop().fadeOut(animationTime, clearReturnInformation());
		
		switch(jQuery(this).val()) {
			case "0":
				break;
			case "yes":
				jQuery("#returnInfoReturnDate," +
						"#returnInfoReturnMethod," +
						"#returnInfoMerchantReceivedOn," +
						"#returnInfoReturnAuthorisationNumber," +
						"#returnInfoReturnInstruction").show();
				
				jQuery("#returnInfo").stop().fadeIn(animationTime);
				break;
			case "no":
				jQuery("#elabQuestionnaireWhereIsMerchandise").stop().fadeIn(animationTime);
				break;
			default:;
		}
	});
}


function loadForm30() {
	
	switch(jQuery("#questionnaireWhatWasPurchased").val()) {
		case "0":
			break;
		case "362":
			jQuery("#elabQuestionnaireMerchandiseOrdered," +
					"#elabQuestionnaireMerchandiseDateExpected," +
					"#elabQuestionnaireMerchandiseNotReceived," +
					"#elabQuestionnaireMerchandiseAgreedLocation," +
					"#elabQuestionnaireWasMerchandiseCancelledNonReceipt").show();
			
			jQuery("#questionnaireMerchandiseSection").stop().fadeIn();
			break;
		case "295":
			jQuery("#elabQuestionnaireServicesOrdered," +
					"#elabQuestionnaireMerchantUnwilling," +
					"#elabQuestionnaireServicesDateExpected," +
					"#elabQuestionnaireServicesNotReceived," +
					"#elabQuestionnaireWasServiceCancelledNonReceipt").show();
			
			jQuery("#questionnaireServicesSection").stop().fadeIn(animationTime);
			break;
		default:;
	}
	
	//Services not received checkbox.
	if (jQuery("#questionnaireServicesNotReceived").is(":checked")) {
		jQuery("#elabQuestionnaireServicesReceivedOn").stop().fadeIn(animationTime);
	}
	
	//Merchandise not received checkbox.
	if (jQuery("#questionnaireMerchandiseNotReceived").is(":checked")) {
		jQuery("#elabQuestionnaireMerchandiseReceivedOn," +
				"#elabQuestionnaireMerchandiseReturn").stop().fadeIn(animationTime);
	}
	
	//Merchandise cancellation section.
	switch(jQuery("#questionnaireWasMerchandiseCancelledNonReceipt").val()) {
		case "0":
			break;
		case "yes":
			jQuery("#cancellationCancelledDate," +
					"#cancellationCodeText," +
					"#cancellationSpokeWith," +
					"#cancellationReasonCancelled").show();
			
			jQuery("#cancellationInfo").stop().fadeIn(animationTime);
			break;
		case "no":
			break;
		case "not applicable":
			break;
		default:;
	}
	
	//Services cancellation section.
	switch(jQuery("#questionnaireWasServicesCancelledNonReceipt").val()) {
		case "0":
			break;
		case "yes":
			jQuery("#cancellationCancelledDate," +
				"#cancellationCodeText," +
				"#cancellationSpokeWith," +
				"#cancellationReasonCancelled").show();
			
			jQuery("#cancellationInfo").stop().fadeIn(animationTime);
			break;
		case "no":
			break;
		case "not applicable":
			break;
			default:;
	}
	
		
	switch(jQuery("#questionnaireMerchandiseReturn").val()) {
		case "0":
			break;
		case "yes":
			jQuery("#returnInfoReturnDate," +
					"#returnInfoReturnMethod").show();
			
			jQuery("#returnInfo").stop().fadeIn(animationTime);
			break;
		case "no":
			jQuery("#elabQuestionnaireWhereIsMerchandise").stop().fadeIn(animationTime);
			break;
		default:;
	}
		
	switch(jQuery("#returnMethod").val()) {
		case "0":
			break;
		case "322":
			break;
		case "323":
		case "324":
		case "325":
		case "326":
			jQuery("#returnInfoShippingNumber," +
					"#returnInfoWhoSignedPackage," +
					"#returnInfoDeliveryAddress").stop().fadeIn(animationTime);
			
			break;
		case "327":
			jQuery("#returnInfoOtherMethod," +
					"#returnInfoShippingNumber," +
					"#returnInfoWhoSignedPackage," +
					"#returnInfoDeliveryAddress").stop().fadeIn(animationTime);
			
			break;
		default:
			;
	}
}
