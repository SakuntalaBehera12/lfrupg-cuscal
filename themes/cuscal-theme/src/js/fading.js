jQuery(document).ready(function() {
	jQuery(".fader").cycle({
		fx: 'fade',
		speed: 'slow',
		timeout: 5000,
		pager: '.fader-nav',
		pause: 1
	});
	
	jQuery("div#fader a").click(function() {
		if (jQuery(this).attr("rel") != "play") {
			jQuery(".fader").cycle("pause");
			jQuery("a#faderPlayPause").attr("rel", "play");
			jQuery("a#faderPlayPause").removeClass("pause");
			jQuery("a#faderPlayPause").addClass("play");
		} else if(jQuery(this).attr("rel") == "play") {
			jQuery(".fader").cycle("resume");
			jQuery("a#faderPlayPause").attr("rel", "pause");
			jQuery("a#faderPlayPause").removeClass("play");
			jQuery("a#faderPlayPause").addClass("pause");
		}
		return false;
	});
});