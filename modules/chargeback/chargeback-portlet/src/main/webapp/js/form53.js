function onChangeForm53() {
	jQuery("#whatWasPurchased").change(function() {		
		//Hide all the sub fields.
		jQuery("#merchandiseSection, #servicesSection, #returnInfo").children().each(function() {
			jQuery(this).hide();
			//Reset the form;
			//This is causing some weirdness in the form where 
			jQuery(this).find("input[type=text]").val("");
			jQuery(this).find("textarea").val("");
			jQuery(this).find("input[type=checkbox]").removeAttr("checked");
			jQuery(this).find("select option[value=0]").attr("selected", "selected");
		});
		
		jQuery("#returnInfo").stop().fadeOut(animationTime);
		
		jQuery("#elabCancellationMerchantBilledMore").stop().fadeOut(animationTime, function() {
			jQuery(this).find("input[type=checkbox]").removeAttr("checked");
		});
		
		jQuery("#merchandiseSection, #servicesSection").stop().fadeOut(animationTime);
		
		switch(jQuery(this).val()) {
			case "0":
				break;
			case "334":
				break;
			case "335":
				break;
			case "336":
				break;
			case "294":
				
				if (jQuery("#reasonCode").val() === "68") { //Form 85
					jQuery("#elabCancellationMerchandiseReturn").show();
				} else {
					jQuery("#elabCancellationMerchandiseWrong," +
							"#elabCancellationMerchandiseOrdered," +
							"#elabCancellationMerchandiseReturn").each(function() {
						jQuery(this).show();
					});
				}
				
				jQuery("#merchandiseSection").stop().fadeIn(animationTime);
				break;
			case "295":
				jQuery("#elabCancellationServicesWrong," +
						"#elabCancellationServicesOrdered," +
						"#elabCancellationExpectedServicesDate").show();
				
				jQuery("#servicesSection").stop().fadeIn(animationTime);
				break;
				
			case "337":
				jQuery("#elabCancellationMerchantBilledMore").stop().fadeIn(animationTime);
				
				//jQuery("#merchandiseSection").stop().fadeIn(animationTime);
				break;
			default:;
		}
	});
	
	jQuery("#merchandiseWhatWasWrong").change(function() {
		
		jQuery("#elabCancellationMerchandiseDetailsNotAsDescribed," +
				"#elabCancellationDetailsDefective," +
				"#elabCancellationExpertIdentified," +
				"#elabCancellationDateInformed," +
				"#elabCancellationCounterfeitCertification," +
				"#elabCancellationCounterfeitDocumentation," +
				"#elabCancellationMerchandiseExplainTermsOfSale," +
				"#elabCancellationMerchandiseAttachingIncidentReporting," +
				"#elabCancellationMerchandiseQualityExplain," +
				"#elabCancellationWorkRedone").hide();
		jQuery("#merchandiseWhatWasWrongSection").stop().fadeOut(animationTime);
		
		jQuery("#showWorkRedoneDetails").stop().fadeOut(animationTime, function() {
			jQuery(this).find("div").each(function() {
				jQuery(this).hide();
			});
			
			jQuery(this).find("input").each(function() {
				jQuery(this).val("");
			});
			
			jQuery("#elabCancellationWorkRedone").find("select option[value=0]").attr("selected", "selected");
		});
		
		//Reset the form.
		/*jQuery("input[type=text]").val("");
		jQuery("textarea").val("");
		jQuery("input[type=checkbox]").removeAttr("checked");
		jQuery("input[type=radio]").removeAttr("checked");*/
		
		switch (jQuery(this).val()) {
			case "0":
				break;
			case "296":
				jQuery("#elabCancellationMerchandiseDetailsNotAsDescribed").show();
				jQuery("#merchandiseWhatWasWrongSection").stop().fadeIn(animationTime);
				break;
			case "297":
				jQuery("#elabCancellationDetailsDefective").show();
				jQuery("#merchandiseWhatWasWrongSection").stop().fadeIn(animationTime);
				break;
			case "298":
				jQuery("#elabCancellationExpertIdentified, " +
						"#elabCancellationDateInformed, " +
						"#elabCancellationCounterfeitCertification, " +
						"#elabCancellationCounterfeitDocumentation").show();
				jQuery("#merchandiseWhatWasWrongSection").stop().fadeIn(animationTime);
				
				break;
			case "299":
				jQuery("#elabCancellationMerchandiseExplainTermsOfSale," +
						"#elabCancellationMerchandiseAttachingIncidentReporting").show();
				jQuery("#merchandiseWhatWasWrongSection").stop().fadeIn(animationTime);
				break;
			case "378":
				jQuery("#elabCancellationMerchandiseQualityExplain").show();
				jQuery("#elabCancellationWorkRedone").show();
				jQuery("#merchandiseWhatWasWrongSection").stop().fadeIn(animationTime);
				break;
			default:
				;
		}
	});
	
	jQuery("#servicesWhatWasWrong").change(function() {
		
		jQuery("#elabCancellationServicesDetailsNotAsDescribed," +
				"#elabCancellationServicesExplainTermsOfSale," +
				"#elabCancellationServicesAttachingIncidentReporting").hide();
		jQuery("#servicesWhatWasWrongSection").stop().fadeOut();
		
		switch(jQuery(this).val()) {
			case "0": //Please select

				break;
			case "296":
				jQuery("#elabCancellationServicesDetailsNotAsDescribed").show();
				jQuery("#servicesWhatWasWrongSection").stop().fadeIn(animationTime);
				
				break;
			case "299":
				jQuery("#elabCancellationServicesExplainTermsOfSale," +
						"#elabCancellationServicesAttachingIncidentReporting").show();
				jQuery("#servicesWhatWasWrongSection").stop().fadeIn(animationTime);
				
				break;
			default:
				;
		}
	});
	
	jQuery("#merchandiseReturn").change(function() {
		jQuery("#returnInfo, " +
				"#elabCancellationWhyMerchandiseNotReturned," +
				"#elabCancellationMerchantRefuseReturn," +
				"#elabCancellationMerchantRefusedReason").stop().fadeOut(animationTime, clearReturnInformation());
		switch(jQuery(this).val()) {
			case "0":
				break;
			case "yes":
				jQuery("#returnInfo>legend," +
						"#returnInfoReturnDate," +
						"#returnInfoReturnMethod," +
						"#returnInfoMerchantReceivedOn," +
						"#returnInfoReturnAuthorisationNumber").show();
				
				jQuery("#returnInfo").stop().fadeIn(animationTime);
				break;
			case "no":
				jQuery("#elabCancellationWhyMerchandiseNotReturned," +
						"#elabCancellationMerchantRefuseReturn").stop().fadeIn(animationTime);
				break;
			default:
				;
		}
	});
	
	jQuery("#merchantRefuse").change(function() {
		jQuery("#elabCancellationMerchantRefusedReason").stop().fadeOut(animationTime, function() {
					jQuery("#merchantRefusedAuthorisation").removeAttr("selected");
				});
		switch(jQuery(this).val()) {
			case "0":
				break;
			case "315":
				jQuery("#elabCancellationMerchantRefusedReason").stop().fadeIn(animationTime);
				
				break;
			case "316":
				break;
			case "317":
				break;
			default:
				;
		}
	});
	
	//Did you cancel section.
	jQuery("#didCancel").change(function() {
		if (jQuery(this).val() === "Yes") {
			
			if (jQuery("#reasonCode").val() === "67") { //Form 53
				jQuery("#cancellationCancelledDate, " +
						"#cancellationSpokeWith, " +
						"#cancellationReasonCancelled").each(function() {
					jQuery(this).show();
				});
			} else if (jQuery("#reasonCode").val() === "68") { //Form 85
				if (jQuery("#whatWasPurchased").val() === "337") {
					jQuery("#cancellationCancelledDate," +
							"#cancellationWasCancellationCodeGiven," +
							"#cancellationWasCardholderGivenCancellationPolicy," +
							"#cancellationSpokeWith").each(function() {
						jQuery(this).show();
					});
				} else if (jQuery("#whatWasPurchased").val() === "334" || 
							jQuery("#whatWasPurchased").val() === "335" || 
							jQuery("#whatWasPurchased").val() === "336" ||
							jQuery("#whatWasPurchased").val() === "294") {
					jQuery("#cancellationCancelledDate," +
							"#cancellationCodeText," +
							"#cancellationWasCardholderGivenCancellationPolicy," +
							"#cancellationSpokeWith").each(function() {
						jQuery(this).show();
					});
				}
			}
			jQuery("#cancellationInfo").stop().fadeIn(animationTime);
		} else {
			jQuery("#cancellationInfo").stop().fadeOut(animationTime, function() {
				jQuery(this).find("div").each(function() {
					jQuery(this).hide();
				});
			});
		}
	});
	
	jQuery("#workRedone").change(function() {
		if (jQuery(this).val() === "Yes") {
			jQuery("#questionOne," +
					"#questionTwo," +
					"#questionThree").show();
			jQuery("#showWorkRedoneDetails").stop().fadeIn(animationTime);
		} else {
			jQuery("#showWorkRedoneDetails").stop().fadeOut(animationTime, function() {
				jQuery(this).find("div").each(function() {
					jQuery(this).hide();
				});
				
				jQuery(this).find("input").each(function() {
					jQuery(this).val("");
				});
			});
		}
	});
	
	jQuery("#returnMethod").change(function() {
		jQuery("#returnInfoOtherMethod," +
				"#returnInfoShippingNumber," +
				"#returnInfoWhoSignedPackage," +
				"#returnInfoDeliveryAddress").stop().fadeOut(animationTime);
		
		switch(jQuery(this).val()) {
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
	});
}

function loadForm53() {	
	if (jQuery("#whatWasPurchased").val() === "294") {
		jQuery("#elabCancellationMerchandiseWrong," +
				"#elabCancellationMerchandiseOrdered," +
				"#elabCancellationMerchandiseReturn").each(function() {
					jQuery(this).show();
				});
		
		if (jQuery("#servicesSection").is(":block")) {
			
			jQuery("#servicesSection").stop().fadeOut(animationTime, function() {
				jQuery("#merchandiseSection").stop().fadeIn(animationTime);
			});
		} else {
			jQuery("#merchandiseSection").stop().fadeIn(animationTime);
		}
	} else if (jQuery("#whatWasPurchased").val() === "295") {
		//Show these fields.
		jQuery("#elabCancellationServicesWrong," +
				"#elabCancellationServicesOrdered," +
				"#elabCancellationExpectedServicesDate").show();
		
		if (jQuery("#merchandiseSection").is(":block")) {
			jQuery("#merchandiseSection").stop().fadeOut(animationTime, function() {
				jQuery("#servicesSection").stop().fadeIn(animationTime);
			});
		} else {
			jQuery("#servicesSection").stop().fadeIn(animationTime);
		}
	} else {
		jQuery("#merchandiseSection, #servicesSection").stop().fadeOut(animationTime);
	}
	
	//Merchandise what was wrong section.
	switch (jQuery("#merchandiseWhatWasWrong").val()) {
		case "0": //Please select
			break;
		case "296":
			jQuery("#elabCancellationMerchandiseDetailsNotAsDescribed").show();
			jQuery("#merchandiseWhatWasWrongSection").stop().fadeIn(animationTime);
			
			jQuery("#merchandiseWhatWasWrong option[value=296]").attr("selected", true);
			break;
		case "297":
			jQuery("#elabCancellationDetailsDefective").show();
			jQuery("#merchandiseWhatWasWrongSection").stop().fadeIn(animationTime);
			
			jQuery("#merchandiseWhatWasWrong option[value=297]").attr("selected", true);
			break;
		case "298":
			jQuery("#elabCancellationExpertIdentified, " +
					"#elabCancellationDateInformed, " +
					"#elabCancellationCounterfeitCertification, " +
					"#elabCancellationCounterfeitDocumentation").show();
			jQuery("#merchandiseWhatWasWrongSection").stop().fadeIn(animationTime);
			
			jQuery("#merchandiseWhatWasWrong option[value=298]").attr("selected", true);
			break;
		case "299":
			jQuery("#elabCancellationMerchandiseExplainTermsOfSale," +
					"#elabCancellationMerchandiseAttachingIncidentReporting").show();
			jQuery("#merchandiseWhatWasWrongSection").stop().fadeIn(animationTime);
			
			jQuery("#merchandiseWhatWasWrong option[value=299]").attr("selected", true);
			break;
		case "378":
			jQuery("#elabCancellationMerchandiseQualityExplain").show();
			jQuery("#elabCancellationWorkRedone").show();
			jQuery("#merchandiseWhatWasWrongSection").stop().fadeIn(animationTime);
			
			jQuery("#merchandiseWhatWasWrong option[value=378]").attr("selected", true);
			break;
		default:
			;
	}
	
	//Services what was wrong section
	switch(jQuery("#servicesWhatWasWrong").val()) {
		case "0": //Please select
			break;
		case "296":
			jQuery("#elabCancellationServicesDetailsNotAsDescribed").show();
			jQuery("#servicesWhatWasWrongSection").stop().fadeIn(animationTime);
			
			break;
		case "299":
			jQuery("#elabCancellationServicesExplainTermsOfSale," +
					"#elabCancellationServicesAttachingIncidentReporting").show();
			jQuery("#servicesWhatWasWrongSection").stop().fadeIn(animationTime);
			
			break;
		default:
			;
	}
	
	//Merchandise return section
	switch(jQuery("#merchandiseReturn").val()) {
		case "0":
			break;
		case "yes":
			jQuery("#returnInfoReturnDate," +
					"#returnInfoReturnMethod," +
					"#returnInfoMerchantReceivedOn," +
					"#returnInfoReturnAuthorisationNumber").show();
			
			jQuery("#returnInfo").stop().fadeIn(animationTime);
			break;
		case "no":
			jQuery("#elabCancellationWhyMerchandiseNotReturned," +
					"#elabCancellationMerchantRefuseReturn").stop().fadeIn(animationTime);
			break;
		default:
			;
	}
	
	//Merchant Refuse section
	switch(jQuery("#merchantRefuse").val()) {
		case "0":
			break;
		case "315":
			jQuery("#elabCancellationMerchantRefusedReason").stop().fadeIn(animationTime);
			
			break;
		case "316":
			break;
		case "317":
			break;
		default:
			;
	}
	
	//Cancelled section.
	if (jQuery("#didCancel").val() === "Yes") {
		jQuery("#cancellationCancelledDate, " +
				"#cancellationSpokeWith, " +
				"#cancellationReasonCancelled").each(function() {
			jQuery(this).show();
		});
		
		jQuery("#cancellationInfo").stop().fadeIn(animationTime);
	} else {
		jQuery("#cancellationInfo").stop().fadeOut(animationTime, function() {
			jQuery(this).find("div").each(function() {
				jQuery(this).hide();
			});
		});
	}
	
	// Quality work re-done section
	if (jQuery("#workRedone").val() === "Yes") {
		jQuery("#questionOne," +
				"#questionTwo," +
				"#questionThree").show();
		jQuery("#showWorkRedoneDetails").stop().fadeIn(animationTime);
	} /*else {
		jQuery("#showWorkRedoneDetails").stop().fadeOut(animationTime, function() {
			jQuery(this).find("div").each(function() {
				jQuery(this).hide();
			});
			
			jQuery(this).find("input").each(function() {
				jQuery(this).val("");
			});
		});
	}*/
	
	//Return method section
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
					"#returnInfoShippingNumber" +
					"#returnInfoWhoSignedPackage," +
					"#returnInfoDeliveryAddress").stop().fadeIn(animationTime);
			
			break;
		default:
			;
	}
}

function clearReturnInformation() {
	jQuery("#returnDate").val("");
	jQuery("#shippingNumber").val("");
	jQuery("#whoSignedPackage").val("");
	jQuery("#otherMethod").val("");
	jQuery("#deliveryAddress").val("");
	jQuery("#merchantReceivedOn").val("");
	jQuery("#returnAuthorisationNumber").val("");
	jQuery("#returnMethod option[value=0]").attr("selected", "selected");
	jQuery("#whyMerchandiseNotReturned").val("");
	jQuery("#merchantRefuse option[value=0]").attr("selected", "selected");
	jQuery("#merchantRefusedAuthorisation," +
			"#merchantRefusedReturn," +
			"#merchantAdvisedNoReturn").each(function() {
				jQuery(this).removeAttr("checked");
			});
}