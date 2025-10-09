Liferay.on('allPortletsReady', function() {
	if (typeof setupLoader !== "undefined") setupLoader();
	
	jQuery("table.saf-list a").live("click", function() {
		jQuery("#loading").show();
		
		jQuery.fileDownload(jQuery(this).attr('href'), {
			successCallback: function(url) {
				jQuery("#loading").hide();
			},
			failCallback: function(responseHtml, url) {
				jQuery("#loading").hide();
			}
		});
		return false;
	});
});

function showWindow() {
	jQuery("#loading").show();
}