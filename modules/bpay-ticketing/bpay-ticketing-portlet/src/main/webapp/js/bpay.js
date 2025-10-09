jQuery.noConflict();

jQuery.ajaxSetup({
	cache : false
});

Liferay.on('allPortletsReady', function() {
		
	jQuery("#paymentInvestigationReasonDiv").hide();
	jQuery("#errorCorrectionReasonDiv").hide();
		
	jQuery("#investigationType").change(function(){
		
		jQuery("#paymentInvestigationReasonDiv").hide("fast");
		jQuery("#errorCorrectionReasonDiv").hide("fast");
		
		if(jQuery(this).attr("value")=="1"){
         	        	
			jQuery("#paymentInvestigationReasonDiv").show("fast");
			jQuery("#bpayCorrectCRN").hide("fast");  
			jQuery("#bpayCorrectBillerCode").hide("fast");
			
			//hide fraudTypeDropdown and scamTypeDropdown for paymentInvestigationReasonDiv
			jQuery("#bpayFraudType").hide("fast"); 
			jQuery("#bpayScamType").hide("fast");
			jQuery("#fraudInfoBlock").hide("fast");
			jQuery("#scamInfoBlock").hide("fast");
			jQuery("#scamTypeDescription").hide("fast");
			jQuery("#fraudTypeDescription").hide("fast");

        } else if(jQuery(this).attr("value")=="2"){
         	
        	jQuery("#errorCorrectionReasonDiv").show("fast");
        	 
        }
		
	}).change();
	
	jQuery("#reasonForRequestErrorCorrection").change(function(){
		
		jQuery("#bpayCorrectCRN").hide("fast");  
		jQuery("#bpayCorrectCRN").val("");
		jQuery("#bpayCorrectBillerCode").hide("fast"); 
		jQuery("#bpayCorrectBillerCode").val("");
		
		// Initially hide fraudTypeDropdown, scamTypeDropdown, fraudInfoBlock and scamInfoBlock
		jQuery("#bpayFraudType").hide("fast");
		
		// Additional check to retain the value if reasonForRequestErrorCorrection is 5 and investigationType is 2
		if (jQuery(this).val() == "5" && jQuery("#investigationType").val() == "2") {
			// Do nothing, retain the current value
		} else {
			// Clear the value for other cases
			jQuery("#fraudType").val("");
			$('.hidden-message-fraud').hide();
			
		}
		jQuery("#bpayScamType").hide("fast");
		
		if (jQuery(this).val() == "5" && jQuery("#investigationType").val() == "2" && jQuery("#fraudType").val() == "3") {
			// Do nothing, retain the current value
		} else {
			// Clear the value for other cases
			jQuery("#scamType").val("");
			$('.description').removeClass('active');
			
		}
		jQuery("#fraudInfoBlock").hide("fast");
		
		if (jQuery(this).val() == "5" && jQuery("#investigationType").val() == "2" && jQuery("#fraudType").val() == "4") {
			// Do nothing, retain the current value
		} else {
			// Clear the value for other cases
			jQuery("#fraudInfo").val("");
			
		}
		
		jQuery("#scamInfoBlock").hide("fast");
		
		if (jQuery(this).val() == "5" && jQuery("#investigationType").val() == "2" && jQuery("#fraudType").val() == "3" && jQuery("#scamType").val() == "8") {
			// Do nothing, retain the current value
		} else {
			// Clear the value for other cases
			jQuery("#scamInfo").val("");
			
		}
		
		
		
		if(jQuery(this).attr("value")=="2"){
			
			jQuery("#bpayCorrectCRN").show("fast");
			
		}else if(jQuery(this).attr("value")=="3"){
			
			jQuery("#bpayCorrectBillerCode").show("fast");
		}
		else if(jQuery(this).attr("value")=="5"){
			
			// Show fraudTypeDropdown if the selected value of reason is "Payer did not authorize payment"
        	jQuery("#bpayFraudType").show("fast");

        	// Show scamTypeDropdown if fraudType value is "Victim of Scam"
        	if (jQuery("#fraudType").val() == "3") {
            	jQuery("#bpayScamType").show("fast");
            	//show scamInfo block if scamType is Other
            	if (jQuery("#scamType").val() == "8")
            	{
					jQuery("#scamInfoBlock").show("fast");
				}
        	}
        	// Show scamTypeDropdown if fraudType value is "Other"
        	else if(jQuery("#fraudType").val() == "4"){
				jQuery("#fraudInfoBlock").show("fast");
			}
		}

	}).change();
	
	
	// Show/hide scamTypeDropdown based on the selected value of fraudType i.e if fraudType value is "Victim of Scam" or "Other"
	jQuery("#fraudType").change(function(){
		// Hide scamTypeDropdown and text box initially
		jQuery("#bpayScamType").hide("fast");
		if (jQuery("#reasonForRequestErrorCorrection").val() == "5" && jQuery("#investigationType").val() == "2" && jQuery("#fraudType").val() == "3") {
			// Do nothing, retain the current value
		} else {
			// Clear the value for other cases
			jQuery("#scamType").val("");
			$('.description').removeClass('active');
			
		}
		jQuery("#fraudInfoBlock").hide("fast");
		if (jQuery("#reasonForRequestErrorCorrection").val() == "5" && jQuery("#investigationType").val() == "2" && jQuery("#fraudType").val() == "4") {
			// Do nothing, retain the current value
		} else {
			// Clear the value for other cases
			jQuery("#fraudInfo").val("");
			
		}
		jQuery("#scamInfoBlock").hide("fast");
		
		if (jQuery("#reasonForRequestErrorCorrection").val() == "5" && jQuery("#investigationType").val() == "2" && jQuery("#fraudType").val() == "3" && jQuery("#scamType").val() == "8") {
			// Do nothing, retain the current value
		} else {
			// Clear the value for other cases
			jQuery("#scamInfo").val("");
			
		}
		
		// Get the selected option's title and index
		var selectedOption = $('#fraudType option:selected');
		
		var selectedIndex = selectedOption.index();
		
		//Check description json is initialized before accessing it
		if (typeof fraudTypeDescriptionMapJson !== 'undefined' && fraudTypeDescriptionMapJson !== null) {
			var selectedOptionTitle = fraudTypeDescriptionMapJson[selectedIndex] || 'Description not found';
			var dropdownPosition = $('#fraudType').position();
			var rightPosition = dropdownPosition.left + $('#fraudType').outerWidth() + 10; // Adjust the distance as needed
			$('.hidden-message-fraud').css({left: rightPosition})
									.text(selectedOptionTitle)
									.fadeIn();
			}

		// Show the title in a hidden div for the 3rd option
		if (selectedIndex === 3) {
    	// Display the hidden div with the title to the right of the dropdown
		$('.hidden-message-fraud').show();
    	
		} else {
    			// If not the 3rd option, hide the message
    			$('.hidden-message-fraud').hide();
		}
		
		// Show scamTypeDropdown if fraudType value is "Victim of Scam"
		if(jQuery(this).attr("value")=="3"){
			jQuery("#bpayScamType").show("fast");
			// Show text box if scamType value is "Other"
			if(jQuery("#scamType").val() == "8"){
				jQuery("#scamInfoBlock").show("fast");
			}
		}
		// Show text box if fraudType value is "Other"
		else if(jQuery(this).attr("value") == "4"){
				jQuery("#fraudInfoBlock").show("fast");
			}
		else{
			// Hide scamTypeDropdown and text box if fraudType value is not "Victim of Scam"
			jQuery("#bpayScamType").hide("fast");
			jQuery("#scamInfoBlock").hide("fast");
		}

	}).change();
	
	// Show/hide scamInfo based on the selected value of scamType i.e if scamType value is "Other Scam"
	jQuery("#scamType").change(function(){
		
		// Hide other text box initially
		jQuery("#scamInfoBlock").hide("fast");
		if (jQuery("#reasonForRequestErrorCorrection").val() == "5" && jQuery("#investigationType").val() == "2" && jQuery("#fraudType").val() == "3" && jQuery("#scamType").val() == "8") {
			// Do nothing, retain the current value
		} else {
			// Clear the value for other cases
			jQuery("#scamInfo").val("");
			
		}
		// Get the selected option's title and index
		var selectedOption = $('#scamType option:selected');
		
		// Get the selected option's index
        var selectedIndex = $(this).find(':selected').index();

        // Hide all description divs
        $('.description').removeClass('active');

        // Show the description div for the selected option
        $('.description[data-option="' + selectedIndex + '"]').addClass('active');

		// Adjust margin-bottom of #bpayScamType based on the height of displayed description
        var scamMessageHeight = $('.description.active').outerHeight();
        $("#bpayScamType").css('margin-bottom', scamMessageHeight);
		if(selectedIndex === 0){
			// Hide all description divs
			$('.description').removeClass('active');
			jQuery("#bpayScamType").css('margin-bottom', '6px');
			
		}
		else if(selectedIndex === 1){
			jQuery("#bpayScamType").css('margin-bottom', '148px');
		}
		else if(selectedIndex === 2){
			jQuery("#bpayScamType").css('margin-bottom', '94px');
		}
		else if(selectedIndex === 3){
			jQuery("#bpayScamType").css('margin-bottom', '130px');
		}
		else if(selectedIndex === 4){
			jQuery("#bpayScamType").css('margin-bottom', '202px');
		}
		else if(selectedIndex === 5){
			jQuery("#bpayScamType").css('margin-bottom', '130px');
		}
		else if(selectedIndex === 6){
			jQuery("#bpayScamType").css('margin-bottom', '130px');
		}
		else if(selectedIndex === 7){
			jQuery("#bpayScamType").css('margin-bottom', '94px');
		}
		else if(selectedIndex === 8){
			jQuery("#bpayScamType").css('margin-bottom', '77px');
		}
		else{
			jQuery("#bpayScamType").css('margin-bottom', '6px');
		}
	
		// Show text box if fraudType value is "Other"
		if(jQuery(this).attr("value") == "8" && jQuery("#fraudType").val() == "3"){
				jQuery("#scamInfoBlock").show("fast");
			}

	}).change();
	
	jQuery("#transactionDate").datePicker({
		clickInput 			: true,
		createButton 		: false,
		startDate 			: "01/01/2000",
		endDate				: (new Date()).asString(),
		showYearNavigation 	: true,
		verticalOffset 		: 20
	});

	jQuery("#payerReportedDate").datePicker({
    	clickInput 			: true,
    	createButton 		: false,
    	startDate 			: "01/01/2000",
    	endDate				: (new Date()).asString(),
    	showYearNavigation 	: true,
    	verticalOffset 		: 20
    });
	
/*	setupLoader();*/
	jQuery("#bpayForm").submit(function() {
		jQuery("#loading").show();
	});
});

function addMoreFiles(){
    var fileIndex = jQuery("#fileTable tr").children().length;
    fileIndex = fileIndex - 1;
    jQuery("#fileTable").append(
            '<tr><td>'+
            '   <input type="file" name="requestAttachments['+ fileIndex +']" />'+
            '</td></tr>');
}


jQuery(function() {
	jQuery("a.masked-details").bind("click", function() {
		var url = jQuery(this).attr("url");
		jQuery.getJSON(url, function(data) {
			var items = [];
			jQuery.each(data, function(key, val) {
				if (key === "") {
					items.push("<li>This request contains no masked fields to display.</li>");
				} else if (key === "error" || key === "Error") {
					items.push("<li>" + val + "</li>");
				} else {
					items.push("<li>" + key + ": " + val + "</li>");
				}
			});
			
//			console.log("size", Object.keys(data).length);
			
			if (Object.keys(data).length == 0) {
				items.push("<li>This request contains no masked fields to display.</li>");
			}
			
//			console.log("data", data);
			
			jQuery("ul.masked-fields-output").html(items.join(""));
			openModalWindow();
		});
	});
});


jQuery(document).ajaxStart(function() {
	jQuery("#loading").show();
}).ajaxStop(function() {
	jQuery("#loading").hide();
});


/**
 * Open the modal div.
 */
function openModalWindow() {
	
//	var modalDiv = jQuery("#" + divId);
	var modalDiv = jQuery(".modal-wrapper");
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
		minWidth	: 400,
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
			//addNoPrintClass();
		},
		onClose		: function(dialog) {
			//removeNoPrintClass();
			closeModal();
		}
	});
}

/**
 * Close the modal div.
 */
function closeModal() {	
	jQuery.modal.close();
	jQuery("ul.masked-fields-output>li").remove();
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

	
	
