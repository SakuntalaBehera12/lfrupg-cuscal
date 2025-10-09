function onChangeForm85() {

	jQuery("#creditVoucherGiven").change(function() {
		jQuery("#creditInfoCreditVoucherDated," +
				"#creditInfoCreditAmount," +
				"#creditInfoInvoiceNumber," +
				"#creditInfoDateCreditVoucher").stop().fadeOut(animationTime, function() {
					jQuery("#creditAmount").val("");
					jQuery("#invoiceNumber").val("");
					jQuery("#creditVoucherDated option[value=0]").attr("selected","selected");
					jQuery("#dateCreditVoucher").val("");
				});
		
		switch(jQuery(this).val()) {
			case "0":
				break;
			case "354":
				jQuery("#creditInfoCreditVoucherDated," +
						"#creditInfoCreditAmount," +
						"#creditInfoInvoiceNumber").stop().fadeIn(animationTime);
				break;
			case "355":
				break;
			case "356":
				break;
			default:;				
		}
	});
	
	jQuery("#creditVoucherDated").change(function() {
		jQuery("#creditInfoDateCreditVoucher").stop().fadeOut(animationTime, function() {
			jQuery("#dateCreditVoucher").val("");
		});
		
		switch(jQuery(this).val()) {
			case "0":
				break;
			case "357":
				jQuery("#creditInfoDateCreditVoucher").stop().fadeIn(animationTime);
				break;
			case "358":
				break;
			default:;
		}
	});
	
	jQuery("#wasCancellationCodeGiven").change(function() {
		jQuery("#cancellationCodeText," +
				"#cancellationCancellationCodeGivenButNotRetained," +
				"#cancellationReservationWithin72Hours," +
				"#cancellationMerchantNotAcceptCancellation," +
				"#cancellationExplainWhyCancellationCodeNotGiven").stop().fadeOut(animationTime, function() {
					jQuery("#cancellationCode").val("");
					jQuery("#cancellationCodeGivenButNotRetained").removeAttr("checked");
					jQuery("#reservationWithin72Hours").removeAttr("checked");
					jQuery("#merchantNotAcceptCancellation").removeAttr("checked");
					jQuery("#explainWhyCancellationCodeNotGiven").val("");
				});
		
		switch(jQuery(this).val()) {
			case "0":
				break;
			case "342":
				jQuery("#cancellationCodeText," +
						"#cancellationCancellationCodeGivenButNotRetained").stop().fadeIn(animationTime);
				break;
			case "343":
				jQuery("#cancellationReservationWithin72Hours," +
						"#cancellationMerchantNotAcceptCancellation," +
						"#cancellationExplainWhyCancellationCodeNotGiven").stop().fadeIn(animationTime);
				break;
			case "344":
				break;
			default:;
		}
	});
	
	jQuery("#wasCardholderGivenCancellationPolicy").change(function() {
		jQuery("#cancellationWhatWasCancellationPolicy").stop().fadeOut(animationTime, function() {
			jQuery("#whatWasCancellationPolicy").val("");
		});
		
		switch(jQuery(this).val()) {
			case "0":
				break;
			case "350":
				jQuery("#cancellationWhatWasCancellationPolicy").stop().fadeIn(animationTime);
				break;
			case "351":
				break;
			case "352":
				break;
			default:;
		}
	});
}

function loadForm85() {
	//What was purchased
	if (jQuery("#whatWasPurchased").val() === "294") { //Merchandise
		jQuery("#elabCancellationMerchandiseReturn").show();
		
		jQuery("#merchandiseSection").stop().fadeIn(animationTime);
	} else if(jQuery("#whatWasPurchased").val() === "337") {
		jQuery("#elabCancellationMerchantBilledMore").show();
	} else {
		jQuery("#merchandiseSection").stop().fadeOut(animationTime);
	}
	
	//Credit Voucher section.
	switch(jQuery("#creditVoucherGiven").val()) {
	
		case "0":
			break;
		case "354":
			jQuery("#creditInfoCreditVoucherDated," +
					"#creditInfoCreditAmount," +
					"#creditInfoInvoiceNumber").stop().fadeIn(animationTime);
			break;
		case "355":
			break;
		case "356":
			break;
		default:;				
	}
	
	//Credit voucher dated section
	switch(jQuery("#creditVoucherDated").val()) {
		case "0":
			break;
		case "357":
			jQuery("#creditInfoDateCreditVoucher").stop().fadeIn(animationTime);
			break;
		case "358":
			break;
		default:;
	}
	
	//Cancellation code section
	switch(jQuery("#wasCancellationCodeGiven").val()) {
		case "0":
			break;
		case "342":
			jQuery("#cancellationCodeText," +
					"#cancellationCancellationCodeGivenButNotRetained").stop().fadeIn(animationTime);
			break;
		case "343":
			jQuery("#cancellationReservationWithin72Hours," +
					"#cancellationMerchantNotAcceptCancellation," +
					"#cancellationExplainWhyCancellationCodeNotGiven").stop().fadeIn(animationTime);
			break;
		case "344":
			break;
		default:;
	}
	
	//Return information section
	switch(jQuery("#merchandiseReturn").val()) {
		case "yes":
			jQuery("#returnInfoReturnDate," +
					"#returnInfoReturnMethod," +
					"#returnInfoMerchantReceivedOn," +
					"#returnInfoReturnAuthorisationNumber").show();
			
			jQuery("#returnInfo").stop().fadeIn(animationTime);
			break;
		default:;
	}
	
	//Return method section.
	switch(jQuery("#returnMethod").val()) {
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
	
	//Cancellation policy section
	switch(jQuery("#wasCardholderGivenCancellationPolicy").val()) {
		case "350":
			jQuery("#cancellationWhatWasCancellationPolicy").stop().fadeIn(animationTime);
			break;
		default:;
	}

	//Did you cancel section
	if (jQuery("#didCancel").val() === "Yes") {
		
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
//		}
		jQuery("#cancellationInfo").stop().fadeIn(animationTime);
	} else {
		jQuery("#cancellationInfo").stop().fadeOut(animationTime, function() {
			jQuery(this).find("div").each(function() {
				jQuery(this).hide();
			});
		});
	}
}