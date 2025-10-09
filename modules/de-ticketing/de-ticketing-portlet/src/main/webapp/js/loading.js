/**
 * Call the setupLoader function in the document load function to setup the
 * loader div as soon as the portlet is loaded.
 * 
 * To display the loading div the following code will need to be added:
 * For Ajax form submits
 * ---------------------
 * 				jQuery(document).ajaxStart(function(){
 * 					jQuery("#loading-wrapper").show(); 
 * 				});
 * 
 * For normal form submits
 * -----------------------
 * 				jQuery(<formID>).bind("submit", function() {
 * 					jQuery("#loading-wrapper").show();
 * 				});
 * 
 */
function setupLoader() {

	//Default values
	var loadingModalWidth = 500,
		loadingModalHeight = 400,
		imageHeight = 32,
		imageWidth = 32,
		textOffset = 20;
	
	//Calculated values. The loader image can be found in the images folder of the theme.
	var imageUrl = themeDisplay.getPathThemeImages() + "/ajax-loader.gif",
		imageTop = (loadingModalHeight/2) - (imageHeight/2),
		modalWindowLeft = (jQuery(window).width() / 2) - (loadingModalWidth/2),
		modalWindowTop = (jQuery(window).height() / 2) - (loadingModalHeight/2);
	
	if (jQuery("#loading").length <= 0) {
		var loadingDivWrapper = jQuery("<div>")
			.attr("id", "loading")
			.css({
				"display" 			: "none",
				"z-index" 			: "10000"			
			});
		
		var loadingDivOverlay = jQuery("<div>")
			.css({
				"position"			: "fixed",
				"top"				: "0",
				"left"				: "0",
				"width"				: "100%",
				"height" 			: "100%",
				"background-color" 	: "#000",
				"opacity" 			: "0.3",
				"-ms-filter" 		: "progid:DXImageTransform.Microsoft.Alpha(Opacity=30)",
				"filter" 			: "alpha(opacity=30)",
				"z-index" 			: "10000"
			})
			.html("&nbsp;");
		
		loadingDivWrapper.append(loadingDivOverlay);
		
		var loadingDiv = jQuery("<div>")
			.css({
				"position"			: "fixed",
				"height" 			: loadingModalHeight + "px",
				"width" 			: loadingModalWidth + "px",
				"top"				: modalWindowTop + "px",
				"left"				: modalWindowLeft + "px",
				"background-color" 	: "#fff",
				"text-align"		: "center",
				"border"			: "solid 1px #000",
				"border-radius" 	: "4px",
				"z-index"			: "10001"
			});
		
		var loadingImage = jQuery("<img>");
		loadingImage.attr("src", imageUrl);
		loadingImage.css({
			"position" 		: "relative",
			"height" 		: imageHeight + "px",
			"width" 		: imageWidth + "px",
			"top" 			: imageTop + "px",
			"z-index" 		: "10001"
		});
		
		loadingDiv.append(loadingImage);
		
		var loadingTextDiv = jQuery("<div>");
		loadingTextDiv.css({
			"position" 		: "relative",
			"top"			: (imageTop + textOffset) + "px",
			"font-weight"	: "bold",
			"text-align" 	: "center"
		});
		loadingTextDiv.html("Please wait...");
		
		loadingDiv.append(loadingTextDiv);
				
		loadingDivWrapper.append(loadingDiv);
		
		jQuery("body").append(loadingDivWrapper);
	}
}

/**
 * Call this function to destroy the loading div left behind. Can be used by the
 * ajaxStop function.
 */
function destroyLoader() {
	if (jQuery("#loader-wrapper").length > 0) {
		jQuery("#loader-wrapper").remove();
	}
}