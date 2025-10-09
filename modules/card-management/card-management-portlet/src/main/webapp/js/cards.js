function clearAll(url) {
	jQuery("#panOrBin").val("");
	jQuery("#autocomplete").val("");
	jQuery("#oneOrg").val("");
	jQuery("#cardholderName").val("");
	jQuery("#postCode").val("");
	jQuery("#expiryMonth").val("MM");
	jQuery("#expiryYear").val("YYYY");
	jQuery("#nonExpired").removeAttr('checked');
	jQuery("#cardStatus").val("");
	jQuery("#schemeType").val("");
	jQuery("#cardSearchForm").attr("action", url);
	jQuery("#cardSearchForm").submit();
}

function showHideSearchForm() {
	jQuery("#cardSearch").toggle(
		"slow",
		function() {
			if (jQuery("#cardSearch").css("display") == "block") {
				jQuery("#pan").focus();
				jQuery("#show-hide-search").html("Hide search form");
			} else {
				jQuery("#show-hide-search").html("Show search form");
			}
		});
}

function replaceAll(strMain, strFind, strRep) {
	return strMain.replace(new RegExp(strFind, 'g'), strRep);
}

function filterCardByFields(url) {
	jQuery("#cardSearchForm").attr("action", url + "&tflag=" + new Date().getTime());
	jQuery('#cardSearchForm').submit();
}

function openCardsDetailsPage(cardId, cuscalToken, url) {
	/*jQuery('#cardId').val(cardId);
	jQuery('#cuscalToken').val(cuscalToken);
	jQuery("#cardSearchForm").attr("action", url);
	jQuery('#cardSearchForm').submit();*/

	location.href = url + "&cardId=" + cardId + "&cuscalToken=" + cuscalToken + "&tflag=" + new Date().getTime();
}

function openCardDevices(cardId, cuscalToken, url) {
	/*jQuery('#cardId').val(cardId);
	jQuery('#cuscalToken').val(cuscalToken);
	jQuery('#cardDetailsForm').attr('action', url);
	jQuery('#cardDetailsForm').submit();*/

	location.href = url + "&cardId=" + cardId + "&cuscalToken=" + cuscalToken + "&tflag=" + new Date().getTime();
}

function deregisterDevice(cardId, cuscalToken, deviceId, url, deviceName) {
	if(confirm("Are you sure you want to deregister '" + deviceName + "'?")){
		jQuery('#cardId').val(cardId);
		jQuery('#cuscalToken').val(cuscalToken);
		jQuery('#deviceId').val(deviceId);
		jQuery('#cardDetailsForm').attr('action', url);
		jQuery('#cardDetailsForm').submit();
	}
}

function openTokenWallets(cardId, cuscalToken, url) {
	/*jQuery('#cardId').val(cardId);
	jQuery('#cuscalToken').val(cuscalToken);
	jQuery('#cardDetailsForm').attr('action', url);
	jQuery('#cardDetailsForm').submit();*/

	location.href = url + "&cardId=" + cardId + "&cuscalToken=" + cuscalToken + "&tflag=" + new Date().getTime();
}

function showTokenStatus(cardId, cuscalToken, tokenId, url) {
	jQuery('#cardId').val(cardId);
	jQuery('#cuscalToken').val(cuscalToken);
	jQuery('#tokenId').val(tokenId);
	jQuery('#cardDetailsForm').attr('action', url);
	jQuery('#cardDetailsForm').submit();
}

function warnNoTokenStatusEdit() {
	alert('Sorry, you are unable to change status of secondary tokens.');
}

function showCardsTab(cardId, cuscalToken, url) {
	/*jQuery('#cardId').val(cardId);
	jQuery('#cuscalToken').val(cuscalToken);
	jQuery('#cardDetailsForm').attr('action', url);
	jQuery('#cardDetailsForm').submit();*/

	location.href = url + "&cardId=" + cardId + "&cuscalToken=" + cuscalToken + "&tflag=" + new Date().getTime();
}

function activateToken(cardId, cuscalToken, tokenId, token, url) {
	if(confirm("Are you sure you want to activate '" + token + "'?")){
		jQuery('#cardId').val(cardId);
		jQuery('#cuscalToken').val(cuscalToken);
		jQuery('#tokenId').val(tokenId);
		jQuery("#cardDetailsForm").attr("action", url);
		jQuery('#cardDetailsForm').submit();
	}
}

function updateToken(cardId, cuscalToken, tokenId, action, token, url) {
	if(confirm("Are you sure you want to " + action +  " '" + token + "'?")){
		jQuery('#cardId').val(cardId);
		jQuery('#cuscalToken').val(cuscalToken);
		jQuery('#tokenId').val(tokenId);
		jQuery('#tokenAction').val(action);
		jQuery("#cardDetailsForm").attr("action", url);
		jQuery('#cardDetailsForm').submit();
	}
}

function updateCardControls(cardId, cuscalToken, url) {
	if(confirm("Are you sure you want to update card controls?")){
		jQuery('#cardId').val(cardId);
		jQuery('#cuscalToken').val(cuscalToken);
		jQuery("#cardDetailsForm").attr("action", url);
		jQuery('#cardDetailsForm').submit();
	}
}

function showHideCardControlHistory(cardId, cuscalToken, ccHistoryPageIndex, url) {
	jQuery("#cardControlHistory").toggle(
		"slow",
		function() {
			if (jQuery("#cardControlHistory").css("display") == "block") {
				jQuery("#show-hide-cc-history").html("Hide card controls history");
				showCardControlHistory(cardId, cuscalToken, ccHistoryPageIndex, url);
			} else {
				jQuery("#show-hide-cc-history").html("Review card controls history");
			}
		});
}

function showCardControlHistory(cardId, cuscalToken, ccHistoryPageIndex, url) {
	jQuery('#cardId').val(cardId);
	jQuery('#cuscalToken').val(cuscalToken);
	jQuery('#ccHistoryPageIndex').val(ccHistoryPageIndex);
	jQuery("#cardDetailsForm").attr("action", url);
	jQuery('#cardDetailsForm').submit();
}

function showMccControls(cardId, cuscalToken, url) {
	/*jQuery('#cardId').val(cardId);
	jQuery('#cuscalToken').val(cuscalToken);
	jQuery('#cardDetailsForm').attr('action', url);
	jQuery('#cardDetailsForm').submit();*/

	location.href = url + "&cardId=" + cardId + "&cuscalToken=" + cuscalToken + "&tflag=" + new Date().getTime();
}

function updateMccControls(cardId, cuscalToken, url) {
	if(confirm("Are you sure you want to update mcc controls?")){
	    var mccControlVals = [];
	    jQuery("input[type=checkbox][name=mcc]").each(
            function() {
                var mccControlVal = {};
                mccControlVal["id"] = jQuery(this).val();
                if(jQuery(this).is(":checked")){
                     mccControlVal["value"] = '1';
                }else{
                     mccControlVal["value"] = '0';
                }
                mccControlVals.push(mccControlVal);
            }
        );

		jQuery('#cardId').val(cardId);
		jQuery('#cuscalToken').val(cuscalToken);
		jQuery('#mccControlVals').val(JSON.stringify(mccControlVals));
		jQuery("#cardDetailsForm").attr("action", url);
		jQuery('#cardDetailsForm').submit();
	}
}

function showHideMccHistory(cardId, cuscalToken, mccHistoryPageIndex, url) {
	jQuery("#mccHistory").toggle(
		"slow",
		function() {
			if (jQuery("#mccHistory").css("display") == "block") {
				jQuery("#show-hide-mcc-history").html("Hide MCC blocking history");
				showMccHistory(cardId, cuscalToken, mccHistoryPageIndex, url);
			} else {
				jQuery("#show-hide-mcc-history").html("Review MCC Blocking history");
			}
		});
}

function showMccHistory(cardId, cuscalToken, mccHistoryPageIndex, url) {
	jQuery('#cardId').val(cardId);
	jQuery('#cuscalToken').val(cuscalToken);
	jQuery('#mccHistoryPageIndex').val(mccHistoryPageIndex);
	jQuery("#cardDetailsForm").attr("action", url);
	jQuery('#cardDetailsForm').submit();
}

function showCardLimits(cardId, cuscalToken, url) {
	/*jQuery('#cardId').val(cardId);
	jQuery('#cuscalToken').val(cuscalToken);
	jQuery('#cardDetailsForm').attr('action', url);
	jQuery('#cardDetailsForm').submit();*/

	location.href = url + "&cardId=" + cardId + "&cuscalToken=" + cuscalToken + "&tflag=" + new Date().getTime();
}

function updateCardLimits(cardId, cuscalToken, url, cardLimitId) {
   var status = jQuery('#limitAction' + cardLimitId).val();
   if (status == -1) {
      alert("Please select an action");
      return;
   }

   var newLimit = parseInt(jQuery('#newLimit' + cardLimitId).val(), 10);
   if (status == 'on') {
      if (newLimit == 0 || isNaN(newLimit)) {
         alert("Please enter a valid limit");
         return;
      } else {
         var minLimit = parseInt(jQuery('#minLimit' + cardLimitId).val(), 10);
         var maxLimit = parseInt(jQuery('#maxLimit' + cardLimitId).val(), 10);

         if (newLimit > maxLimit || newLimit < minLimit) {
            alert("Limit needs to be between minimum and maximum value");
            return;
         }

         var tranLimitId = 1, dayLimitId = 2, weekLimitId = 3;
         var currentTranLimit = parseInt(jQuery('#currentLimit' + tranLimitId).val(), 10);
         var currentDayLimit = parseInt(jQuery('#currentLimit' + dayLimitId).val(), 10);
         var currentWeekLimit = parseInt(jQuery('#currentLimit' + weekLimitId).val(), 10);

         if (cardLimitId == tranLimitId) {
            if (currentDayLimit != 0 && (newLimit > currentDayLimit)) {
               alert("Per Transaction Limit needs to be less than Daily Transaction Limit");
               return;
            }

            if (currentWeekLimit != 0 && (newLimit > currentWeekLimit)) {
               alert("Per Transaction Limit needs to be less than Weekly Transaction Limit");
               return;
            }

         } else if (cardLimitId == dayLimitId) {
            if (currentTranLimit != 0 && (newLimit < currentTranLimit)) {
               alert("Daily Transaction Limit needs to be greater than Per Transaction Limit");
               return;
            }

            if (currentWeekLimit != 0 && (newLimit > currentWeekLimit)) {
               alert("Daily Transaction Limit needs to be less than Weekly Transaction Limit");
               return;
            }
         } else if (cardLimitId == weekLimitId) {
            if (currentDayLimit != 0 && newLimit < currentDayLimit) {
               alert("Weekly Transaction Limit needs to be greater than Daily Transaction Limit");
               return;
            }

            if (currentTranLimit != 0 && (newLimit < currentTranLimit)) {
               alert("Weekly Transaction Limit needs to be greater than Per Transaction Limit");
               return;
            }
         }
      }
   }

   if (confirm("Are you sure you want to update card limits?")) {
      var cardLimitsVals = {};
      cardLimitsVals["id"] = cardLimitId;
      if (status == 'resume') {
         status = 'on';
      }
      cardLimitsVals["status"] = status;

      if (isNaN(newLimit)) {
         cardLimitsVals["limit"] = jQuery('#currentLimit' + cardLimitId).val();
      } else {
         cardLimitsVals["limit"] = jQuery('#newLimit' + cardLimitId).val();
      }

      jQuery('#cardId').val(cardId);
      jQuery('#cuscalToken').val(cuscalToken);
      jQuery('#cardLimitId').val(cardLimitId);
      jQuery('#cardLimitsVals').val(JSON.stringify(cardLimitsVals));
      jQuery("#cardDetailsForm").attr("action", url);
      jQuery('#cardDetailsForm').submit();
   }
}

function changeLimitAction(cardLimitId) {
   if (jQuery('#limitAction' + cardLimitId).val() == 'on') {
      jQuery('#newLimit' + cardLimitId).removeAttr('disabled');
   } else {
      jQuery('#newLimit' + cardLimitId).val('');
      jQuery('#newLimit' + cardLimitId).attr('disabled', 'disabled');
   }
}

function showHideLimitsHistory(cardId, cuscalToken, limitsHistoryPageIndex, url) {
	jQuery("#limitsHistory").toggle(
		"slow",
		function() {
			if (jQuery("#limitsHistory").css("display") == "block") {
				jQuery("#show-hide-limits-history").html("Hide limits history");
				showLimitsHistory(cardId, cuscalToken, limitsHistoryPageIndex, url);
			} else {
				jQuery("#show-hide-limits-history").html("Review limits history");
			}
		});
}

function showLimitsHistory(cardId, cuscalToken, limitsHistoryPageIndex, url) {
	jQuery('#cardId').val(cardId);
	jQuery('#cuscalToken').val(cuscalToken);
	jQuery('#limitsHistoryPageIndex').val(limitsHistoryPageIndex);
	jQuery("#cardDetailsForm").attr("action", url);
	jQuery('#cardDetailsForm').submit();
}

Liferay.on('allPortletsReady',function() {
	
	/*setupLoader();
	
	jQuery("#cardSearchForm, #cardDetailsForm").submit(function () {
		jQuery("#loading").show();
	});*/
	
	jQuery("#pan").focus();
	
	if (jQuery("#expiryMonth").val() == "") {
		jQuery("#expiryMonth").val("MM");
	}
	
	if (jQuery("#expiryYear").val() == "") {
		jQuery("#expiryYear").val("YYYY");
	}
	
	jQuery("#expiryMonth").bind("focus", function() {
		if (jQuery("#expiryMonth").val() == "" || jQuery("#expiryMonth").val() == "MM") {
			jQuery("#expiryMonth").val("");
		}
	});
	
	jQuery("#expiryYear").bind("focus", function() {
		if (jQuery("#expiryYear").val() == "" || jQuery("#expiryYear").val() == "YYYY") {
			jQuery("#expiryYear").val("");
		}
	});
	
	//Check if the PAN field is empty or not.
	if (jQuery("#panOrBin").val() != "") {
		disableIssuerCardholder(jQuery("#panOrBin"));
	} else {
		if (jQuery("#autocomplete").length > 0 && (jQuery("#autocomplete").val() != ""
			|| jQuery("#cardholderName").val() != ""
			|| jQuery("#postCode").val() != "")) {
			disablePan();
		} else if (jQuery("#oneOrg").val() != ""
					&& (jQuery("#cardholderName").val() != "" || jQuery("#postCode").val() != "")) {
			disablePan();
		}
	}
});

jQuery(function() {	
	jQuery("#panOrBin").keyup(function() {
		disableIssuerCardholder(jQuery(this));
	});
	
	if (jQuery("#autocomplete").length > 0) {
		jQuery("#autocomplete, #cardholderName, #postCode").keyup(function() {
			if (jQuery(this).val() != "") {
				disablePan();
			} else {
				enablePan();
			}
		});
	} else {
		jQuery("#cardholderName, #postCode").keyup(function() {
			if (jQuery(this).val() != "") {
				disablePan();
			} else {
				enablePanForOneIssuerName();
			}
		});
	}
	
	jQuery("#show-hide-reference-list").bind("click", function() {
		jQuery("#reference-list-wrapper").toggle(300, function() {
			if (jQuery(this).css("display") == "block") {
				jQuery("#show-hide-reference-list").html("Hide Card Reference List");
			} else if(jQuery(this).css("display") == "none") {
				jQuery("#show-hide-reference-list").html("Show Card Reference List");
			}
		});
	});
});

//Disable all the fields in the User Details area.
function disableIssuerCardholder(elm) {
	if (jQuery(elm).val() != "") {
		jQuery("fieldset.user-details").find(":input").each(function() {
			jQuery(this).addClass("disabled");
			jQuery(this).attr("disabled", true);
		});
	} else {
		jQuery("fieldset.user-details").find(":input").each(function() {
			jQuery(this).removeClass("disabled");
			jQuery(this).attr("disabled", false);
		});
	}	
}

//Disable the PAN field when the user inputs anything in the User Details input boxes.
function disablePan() {
	jQuery("#panOrBin").addClass("disabled");
	jQuery("#panOrBin").attr("disabled", true);
}

//Enable the PAN only if all the three fields in are blank.
function enablePan() {
	if (jQuery("#autocomplete").val() == "" 
		&& jQuery("#cardholderName").val() == "" 
		&& jQuery("#postCode").val() == "") {
			
		jQuery("#panOrBin").removeClass("disabled");
		jQuery("#panOrBin").attr("disabled", false);
		
	}
}

//Enable the PAN field if there is only one organisation.
function enablePanForOneIssuerName() {
	if (jQuery("#cardholderName").val() == "" 
		&& jQuery("#postCode").val() == "") {
		
		jQuery("#panOrBin").removeClass("disabled");
		jQuery("#panOrBin").attr("disabled", false);
		
	}
}

//Open the cards Details.
function openCardsDetails(cardId, cuscalToken, url) {
	/*jQuery('#cardId').val(cardId);
	jQuery('#cuscalToken').val(cuscalToken);
	jQuery("#cardDetailsForm").attr("action", url);
	jQuery('#cardDetailsForm').submit();*/

	location.href = url + "&cardId=" + cardId + "&cuscalToken=" + cuscalToken + "&tflag=" + new Date().getTime();
}

//Return back to the Search Results page.
function backToSearchPage(url){
	jQuery("#cardDetailsForm").attr("action", url);
	jQuery('#cardDetailsForm').submit();
}

function updateCardStatus(cardId,cuscalToken,url,oldStatus){
	jQuery('#cardId').val(cardId);
	jQuery('#cuscalToken').val(cuscalToken);
	var changedStatus = jQuery('#cardAvalStatus').val();
	jQuery('#cardNewStatus').val(changedStatus);
		
	if(oldStatus != changedStatus){
		if(confirm("Click OK to proceed with the card update. Click Cancel, if you do not wish to update the card.")){
			jQuery("#cardDetailsForm").attr("action", url);
			jQuery('#cardDetailsForm').submit();
		}
	}
}

function addToShortlist(pan, cardId, cuscalToken, detailsUrl, updateCardReferenceUrl) {

	jQuery.ajax({
		type: "post",
		url: updateCardReferenceUrl,
		data: {
			cardId: cardId,
			cuscalToken: cuscalToken
		},
		success: function(data) {
			if (data == "removeCard") {
				jQuery("#card-" + cardId).parent().remove();
				jQuery("#reference-" + cardId).html("+");
			} else if (data == "addCard") {
				var cardWrapper = jQuery("<p></p>");
				jQuery("<a></a>").
					text(pan).
					attr("class", "cards").
					attr("id", "card-" + cardId).
					attr("href", "javascript: openCardsDetailsPage('" + cardId + "', '" + cuscalToken + "', '" + detailsUrl + "')").
					append(" ").
					appendTo(cardWrapper);
				jQuery("<a></a>").
					text("X").
					attr("class", "remove-card").
					attr("href", "javascript: removeCardFromCardReferenceList('" + cardId + "', '" + updateCardReferenceUrl + "')").
					appendTo(cardWrapper);
				cardWrapper.appendTo("#reference-list-cards");
				jQuery("#reference-" + cardId).html("-");
			}
			
			if (jQuery("#reference-list-cards p").size() > 0) {
				jQuery("#reference-list-parent").show(300);
				if (jQuery("#reference-list-wrapper").css("display") == "block") {
					jQuery("#show-hide-reference-list").html("Hide Card Reference List");
				} else if(jQuery("#reference-list-wrapper").css("display") == "none") {
					jQuery("#show-hide-reference-list").html("Show Card Reference List");
				}
			} else {
				jQuery("#reference-list-parent").hide(300,
					function() {
						jQuery("#reference-list-wrapper").hide();
						jQuery(".cards").remove();
					}
				);
			}
		}
	});
}

function clearCardReferenceList(url) {
	jQuery.ajax({
		type: "post",
		url: url,
		success: function(data) {
			if (data) {
				jQuery("#reference-list-parent").hide(300,
						function() {
							jQuery(".update-shortlist").each(function(){
								jQuery(this).html("+");
							});
							jQuery("#reference-list-cards p").each(function(){
							    jQuery(this).remove();
							});
							jQuery("#reference-list-wrapper").hide();
							jQuery(".cards").remove();
						}
				);
			}
		}
	});
}

function removeCardFromCardReferenceList(cardId, url) {
	jQuery.ajax({
		type: "post",
		url: url,
		data: {
			cardId: cardId
		},
		success: function(data) {
			if (data) {
				jQuery("#card-" + cardId).parent().remove();
				jQuery("#reference-" + cardId).html("+");
				if (jQuery("#reference-list-cards p").size() == 0) {
					jQuery("#reference-list-parent").hide(300,
						function() {
							jQuery("#reference-list-wrapper").hide();
							jQuery(".cards").remove();
						}
					);
				}
			}
		}
	});
}


function openTokenLastUpdatedByBlurbPage() {
	jQuery.ajax({
		type 	: "post",
		url 	: jQuery("#tokenLastUpdatedByBlurbUrl").val(),
		success : function(data) {
			var failedMessage = "<tr><td>An error has occurred while trying to process your request. If this error continues, please contact "+
			"<a class='normal-link' href='mailto:calldirect@cuscal.com.au'>CallDirect</a> on <span class='nowrap'>1300 650 501</span>.</td></tr>";;
			
			if(data != "") {
				jQuery("#tokenLastUpdatedByBlurb").html(data);
			} else {
				jQuery("#tokenLastUpdatedByBlurb").html(failedMessage);
			}
						
			openModalWindow("tokenLastUpdatedByBlurbDiv");
		}
	});

}


/**
 * Open the modal div.
 * 
 * @param divId
 */
function openModalWindow(divId) {
	
	var modalDiv = jQuery("#" + divId);
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
		minWidth	: 826,
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
	
		jQuery.modal.close();
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


jQuery(function(){
    if (jQuery('#mccControlVals').val() !== undefined) {
        jQuery("input[type=checkbox][name=mccBlockAll]").click(function(){
            if(jQuery(this).is(":checked")){
                jQuery("input[type=checkbox][name=mcc]").each(
                    function() {
                        jQuery(this).attr('checked', true);
                        jQuery('#mccCheckedTotal').val(jQuery('#mccTotal').val());
                    }
                 );
           }else{
                jQuery("input[type=checkbox][name=mcc]").each(
                     function() {
                         jQuery(this).attr('checked', false);
                         jQuery('#mccCheckedTotal').val(0);
                     }
                 );
           }
        });

        jQuery("input[type=checkbox][name=mcc]").click(function(){
             if(jQuery(this).is(":checked")){
                jQuery('#mccCheckedTotal').val(+jQuery('#mccCheckedTotal').val()+1);
             }else{
                jQuery('#mccCheckedTotal').val(+jQuery('#mccCheckedTotal').val()-1);
             }

             if(jQuery('#mccCheckedTotal').val() == jQuery('#mccTotal').val()){
                jQuery("input[type=checkbox][name=mccBlockAll]").attr('checked', true);
             }else{
                jQuery("input[type=checkbox][name=mccBlockAll]").attr('checked', false);
             }
        });
    }
});

jQuery(function(){
	
	var controlVals = {};

	if (jQuery('#hideIfEnableds').val() !== undefined) {
		
		var hideIfEnabledsObj = JSON.parse(jQuery('#hideIfEnableds').val());

		jQuery('input[type="checkbox"][class="uiSwitch"]').click(function(){
			
			var controlId = jQuery(this).val();

	        if(jQuery(this).is(":checked")){
	        	
	        	controlVals[controlId] = '1';
	        	
	        	if(hideIfEnabledsObj[controlId]){
	        		var hideIfEnableds = hideIfEnabledsObj[controlId];
	        		var hideIfEnabledsLength = hideIfEnableds.length;

	        		for(var i = 0; i < hideIfEnabledsLength; i++){
	        			jQuery('input[type="checkbox"][name=' + hideIfEnableds[i] + ']').attr('disabled','disabled');
	        		}
	        	}

	        }

	        else if(jQuery(this).is(":not(:checked)")){

	        	controlVals[controlId] = null;
	        	
	        	if(hideIfEnabledsObj[controlId]){
	        		var hideIfEnableds = hideIfEnabledsObj[controlId];
	        		var hideIfEnabledsLength = hideIfEnableds.length;

	        		for(var i = 0; i < hideIfEnabledsLength; i++){
	            		jQuery('input[type="checkbox"][name=' + hideIfEnableds[i] + ']').removeAttr('disabled');
	        		}
	        	}

	        }
	        
	        jQuery('#controlVals').val(JSON.stringify(controlVals));

	    });
		
		for(var controlId in hideIfEnabledsObj){
			if(jQuery('input[type="checkbox"][name=' + controlId + ']').is(":checked")){
				var hideIfEnableds = hideIfEnabledsObj[controlId];
	    		var hideIfEnabledsLength = hideIfEnableds.length;

	    		for(var i = 0; i < hideIfEnabledsLength; i++){
	    			jQuery('input[type="checkbox"][name=' + hideIfEnableds[i] + ']').attr('disabled','disabled');
	    		}
			}
		}
	}
	
});

function removeURLParameter(url, parameter) {
    //prefer to use l.search if you have a location/link object
    var urlparts = url.split('?');
    if (urlparts.length >= 2) {

        var prefix = encodeURIComponent(parameter) + '=';
        var pars = urlparts[1].split(/[&;]/g);

        //reverse iteration as may be destructive
        for (var i = pars.length; i-- > 0;) {
            //idiom for string.startsWith
            if (pars[i].lastIndexOf(prefix, 0) !== -1) {
                pars.splice(i, 1);
            }
        }

        return urlparts[0] + (pars.length > 0 ? '?' + pars.join('&') : '');
    }
    return url;
}