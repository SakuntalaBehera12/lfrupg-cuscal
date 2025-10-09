jQuery.noConflict();

jQuery.ajaxSetup({
	cache : false
});

function scrollToAnchor(aid) {
	var aTag = jQuery("a[name='" + aid + "']");
	jQuery("html, body").animate(
		{scrollTop : aTag.offset().top},
		'slow'
	);
}

function gotoUrl(url) {
	window.location = url;
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
			
			if (Object.keys(data).length == 0) {
				items.push("<li>This request contains no masked fields to display.</li>");
			}
			
			jQuery("ul.masked-fields-output").html(items.join(""));
			openModalWindow("maskedFieldDiv");
		});
	});
});

jQuery(function() {
	jQuery("a.cancel-this-request").bind("click", function() {
			openModalWindow("cancelRequestDiv");
	});
});

jQuery(function() {
	jQuery("a.reverse-this-request").bind("click", function() {
			openModalWindow("reverseRequestDiv");
	});
});

jQuery(function() {
	jQuery("a.cancel-request").bind("click", function() {
		jQuery("#cancelRequestDiv").hide();		
		var url = jQuery(this).attr("url");
		var inputData = { cancelNote: jQuery("#cancelNote").val()};		
		jQuery.getJSON(url, inputData, function(data) {
			var items = [];
			jQuery.each(data, function(key, val) {
				if (key === "error" || key === "Error") {
					if(val === "NoError"){						
						jQuery(".cancelRequestNoteCSSClass").val(jQuery("#cancelNote").val());							
						jQuery("#cancelRequestSubmitForm")[0].submit();
					}
					else{
						items.push("<li>"+val+"</li>");
						jQuery("ul.cancel-request-output").html(items.join(""));
						jQuery("#cancelRequestDiv").show();
					}
				}
			});
		});
	});
});

jQuery(function() {
	jQuery("a.reverse-request").bind("click", function() {
		jQuery("#reverseRequestDiv").hide();		
		var url = jQuery(this).attr("url");
		var inputData = { cancelNote: jQuery("#reverseNote").val()};		
		jQuery.getJSON(url, inputData, function(data) {
			var items = [];
			jQuery.each(data, function(key, val) {
				if (key === "error" || key === "Error") {
					if(val === "NoError"){						
						jQuery(".reverseRequestNoteCSSClass").val(jQuery("#reverseNote").val());						
						jQuery("#reverseRequestSubmitForm")[0].submit();
					}
					else{
						items.push("<li>"+val+"</li>");
						jQuery("ul.reverse-request-output").html(items.join(""));
						jQuery("#reverseRequestDiv").show();
					}
				}
			});
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
function openModalWindow(divId) {
	var modalDiv = jQuery("#" + divId);
//	var modalDiv = jQuery("#" + divId);
	//var modalDiv = jQuery(".modal-wrapper");
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
			closeModal(divId);
		}
	});
}

/**
 * Close the modal div.
 */
function closeModal(divId) {	
	jQuery.modal.close();
	if(divId == "cancelRequestDiv"){
		jQuery("ul.cancel-request-output>li").remove();		
	}else{
		jQuery("ul.masked-fields-output>li").remove();
	}
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

/*function addNoPrintClass() {
	jQuery("ul.masked-fields-output").addClass("no-print");
}*/