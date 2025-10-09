Liferay.on('allPortletsReady', function() {
		
	jQuery("#intendedAccountOrWrongPayee").change(function(){
		
		jQuery("#intendedAccountDiv").hide("fast"); 
		jQuery("#intendedAccountBSBNumberDiv").hide("fast");  
		jQuery("#intendedAccountNumberDiv").hide("fast");  
		jQuery("#intendedAccountTitleDiv").hide("fast");  

		jQuery("#customerDeclarationAttachedDiv").hide("fast"); 

		if(jQuery(this).attr("value")=="1"){
			
			jQuery("#intendedAccountDiv").show("fast");
			jQuery("#intendedAccountBSBNumberDiv").show("fast");  
			jQuery("#intendedAccountNumberDiv").show("fast");  
			jQuery("#intendedAccountTitleDiv").show("fast");  
			
		}else if(jQuery(this).attr("value")=="2"){
			
			jQuery("#customerDeclarationAttachedDiv").show("fast"); 
		}

	}).change();
	
	jQuery("#claimReason").change(function(){
		
		jQuery("#drawingInTheNameOfDiv").hide("fast");  

		if(jQuery(this).attr("value")=="2"){
			jQuery("#drawingInTheNameOfDiv").show("fast");    
		}else {
			jQuery("#drawingInTheNameOfDiv").hide("fast"); 
		}

	}).change();
	
	jQuery("#ofiClaimResult").change(function(){
		
		jQuery("#claimResultAcceptedDiv").hide("fast");  
		jQuery("#claimResultRefusedDiv").hide("fast");  

		if(jQuery(this).attr("value")=="1"){
			jQuery("#claimResultAcceptedDiv").show("fast");   
		}else if(jQuery(this).attr("value")=="2") {
			jQuery("#claimResultRefusedDiv").show("fast"); 
		}

	}).change();
	
	
	jQuery("#requestFor").change(function(){
		
		jQuery("#remitterNameDiv").hide("fast");  
		jQuery("#finInstUserIdMandatory").hide();  
		jQuery("#finInstNameMandatory").hide(); 
		jQuery("#destinationAccountNumberHoverText").hide(); 
		jQuery("#destinationBSBHoverText").hide(); 

		if(jQuery(this).attr("value")=="1"){
			jQuery("#remitterNameDiv").show("fast");  
			jQuery("#finInstUserIdMandatory").show();  
			jQuery("#finInstNameMandatory").show();  
			jQuery("#destinationAccountNumberHoverText").hide(); 
			jQuery("#destinationBSBHoverText").hide(); 
		}else if(jQuery(this).attr("value")=="2") {
			jQuery("#remitterNameDiv").hide("fast");  
			jQuery("#finInstUserIdMandatory").hide();  
			jQuery("#finInstNameMandatory").hide(); 
			jQuery("#destinationAccountNumberHoverText").show(); 
			jQuery("#destinationBSBHoverText").show(); 
		}

	}).change();
	
//	attachRemoveLink();
	
//	setupLoader();
	jQuery("#deForm").submit(function() {
		jQuery("#loading").show();
	});
	
	attachRemoveLink();
	
	jQuery(".dpInput").datePicker({
		clickInput 			: true,
		createButton 		: false,
		startDate 			: "01/01/2000",
		endDate				: (new Date()).asString(),
		showYearNavigation 	: true,
		verticalOffset 		: 20
	});
	
});

function onFormSubmit(event) {

	var count = jQuery("#more-amounts").val();
		var flag=false;
		var rootElement;
		if (count!=0){
			for (i = 0; i <= count ; i++) {
					if (i==0){
						rootElement =jQuery("#disputed-amounts0");
					} else {
						rootElement =jQuery("#disputed-amounts0"+i);
					}
					rootElement.children().children().children().each(function() {
					if (jQuery(this).attr("id") && jQuery(this).attr("name")) {
						var id=jQuery(this).attr("id");
						var val=jQuery("#"+id).val();
					
						if (val==""){
//							flag=true;
//							jQuery("#"+id).next("a").next("span").text("Required Field!");
						} else {
							jQuery("#"+id).next("a").next("span").text("");
						}
					}
				});
			}
		}
		if (flag==true){
				event.preventDefault();
		}

}

function addMoreAmounts() {
	var addFlag="true";
	jQuery("#maxClaimsError").text("");
	var count = jQuery("#more-amounts").val();
	if (count == 14){
	  jQuery("#maxClaimsError").text("you reached to max count of 15 attach a file for more claims");
	} else {
		count=(+count + 1);
		var clonedElement = jQuery("#disputed-amounts0").clone();
		var removeLinkDiv = document.createElement("div");	
		var removeLink = document.createElement("a");
		var newId = "disputed-amounts" + count;
		clonedElement.attr("id", newId);
		clonedElement.removeAttr("style");
		

		clonedElement.find(".error").remove();
		clonedElement.find(".removeLink").remove();
		
//		removeLinkDiv.className="removeLink"
		removeLinkDiv.setAttribute("class", "removeLink divCell");
//		removeLinkDiv.attr("class", "removeLink");
		removeLinkDiv.appendChild(removeLink);
		removeLink.innerHTML = "X";
		removeLink.href = "#";
		removeLink.onclick = function() {
			var totalTransactions = jQuery("#more-amounts").val();
			if(totalTransactions == 0){
				jQuery("#maxClaimsError").text("At least one amount is required");
				return false;
			}
			
			jQuery("#maxClaimsError").text("");

			jQuery(this).parent().parent().parent().remove();
			
			var targetElement =jQuery(this).parent().parent().parent();
			var targetElementId =targetElement.attr("id");

			var itemNum=targetElementId.substring("disputed-amounts".length);

			if (itemNum==totalTransactions){
				//do nothing.
			} else {

				onRemoveUpdateIdAndNames(itemNum,totalTransactions)
			}
			
			
			jQuery("#more-amounts").val(+jQuery("#more-amounts").val() - 1);
			
			return false;
		};

		clonedElement.children(':last').append(removeLinkDiv);
		clonedElement.insertBefore(".add-amounts");
		updateIdAndNames(clonedElement, count, addFlag);			
		jQuery("#more-amounts").val(count);
		
		jQuery(".dpInput").datePicker({
			clickInput 			: true,
			createButton 		: false,
			startDate 			: "01/01/2000",
			endDate				: (new Date()).asString(),
			showYearNavigation 	: true,
			verticalOffset 		: 20
		});
		
//		removeLinkClick();
		
	}
}

function updateIdAndNames(parentWrapper, count, addFlag) {
	
	parentWrapper.children().children().children().each(function() {
		
		if (jQuery(this).attr("for")) {
			var oldFor = jQuery(this).attr("for");
		} else if (jQuery(this).attr("id") && jQuery(this).attr("name")) {
			var oldId = jQuery(this).attr("id");
			var oldName = jQuery(this).attr("name");
			if (addFlag =="true"){
				jQuery(this).val("");
			}
		}
		
		var newId, newFor, newName;
		if (jQuery(this).attr("for")) {
		
			if(oldFor.lastIndexOf("-")==-1){
				if (oldFor.lastIndexOf("0")==-1){
					newFor = oldFor + "-"+count;	
				} else {
					newFor = oldFor.replace(oldFor.substring(oldFor.lastIndexOf("0")),("-"+count));
				}
				
			} else {
			    newFor = oldFor.replace(oldFor.substring(oldFor.lastIndexOf("-")),("-" + count));			
			}
		} else if (jQuery(this).attr("id") && jQuery(this).attr("name")) {
			newId = oldId + count;
			
			if(oldId.lastIndexOf("-")==-1){
				newId = oldId + "-"+count;	
			} else {
			    newId = oldId.replace(oldId.substring(oldId.lastIndexOf("-")),("-" + count));			
			}
			newName = oldName.replace(oldName.substring(oldName.lastIndexOf("[")),("[" + count + "]"));
			jQuery(this).attr("readonly", false);
			if(newId.indexOf("processedDate") > -1){
				jQuery(this).addClass("dpInput");
			}
		}
		
		if (jQuery(this).attr("for")) {
			jQuery(this).attr("for", newFor);
		} else if (jQuery(this).attr("id") && jQuery(this).attr("name")) {
			jQuery(this).attr("id", newId);
			jQuery(this).attr("name", newName);
		}
	});
}	
	
function onRemoveUpdateIdAndNames(startItem,totalTransactions) {
  var addFlag="false";
  
  for (i=startItem;i<totalTransactions;i++){
	var nextElement =jQuery("#disputed-amounts"+(+i+1));
	updateIdAndNames(nextElement, i, addFlag);
	nextElement.attr("id", "disputed-amounts"+i);
  }
	
}

function gotoUrl(url) {
	window.location = url;
}

function goBack() {
    window.history.back()
}

function clearAll(url) {
	jQuery('#destinationBSB').val("");
	jQuery('#destinationAccountNumber').val("");
	jQuery('#destinationLodgementRef').val("");
	jQuery('#transactionAmount').val("");
	jQuery('#batchNumber').val("");
	jQuery('#processedDate').val("");
	jQuery('#finInstUserId').val("");
	
	jQuery("#deForm").attr("action", url);	
	jQuery("#deForm").submit();
}


function addMoreFiles(){
    var fileIndex = jQuery("#fileTable tr").children().length;
    fileIndex = fileIndex - 1;
    jQuery("#fileTable").append(
            '<tr><td>'+
            '   <input type="file" name="requestAttachments['+ fileIndex +']" />'+
            '</td></tr>');
}


function removeLinkClick() {

	jQuery(".removeLink").click(function() {
		var totalTransactions = jQuery("#more-amounts").val();
		if(totalTransactions == 0){
			jQuery("#maxClaimsError").text("At least one amount is required");
		}else{
			
			jQuery("#maxClaimsError").text("");

			jQuery(this).parent().parent().parent().remove();
			
			var targetElement =jQuery(this).parent().parent().parent();
			var targetElementId =targetElement.attr("id");

			var itemNum=targetElementId.substring("disputed-amounts".length);

			if (itemNum==totalTransactions){
				//do nothing.
			} else {
				onRemoveUpdateIdAndNames(itemNum,totalTransactions)
			}
			jQuery("#more-amounts").val(+jQuery("#more-amounts").val() - 1);
		}
		
		return false;
	});
}

function attachRemoveLink() {
	
	var prepopulated = jQuery("#prepopulated").val();
	
	var totalTransactions = jQuery("#more-amounts").val();
	
	var i = 0;
	if(prepopulated == 'true'){
		i = 1;
	}
	
	for (;i<=totalTransactions;i++){
		
		var element = jQuery("#disputed-amounts" + i);
		var removeLinkDiv = document.createElement("div");	
		var removeLink = document.createElement("a");
		removeLinkDiv.appendChild(removeLink);
		removeLinkDiv.setAttribute("class", "removeLink divCell");
		removeLink.innerHTML = "X";
		removeLink.href = "#";
		removeLink.onclick = function() {
			var totalTransactions = jQuery("#more-amounts").val();
			if(totalTransactions == 0){
				jQuery("#maxClaimsError").text("At least one amount is required");
				return false;
			}
			
			jQuery("#maxClaimsError").text("");

			jQuery(this).parent().parent().parent().remove();
			
			var targetElement =jQuery(this).parent().parent().parent();
			var targetElementId =targetElement.attr("id");

			var itemNum=targetElementId.substring("disputed-amounts".length);

			if (itemNum==totalTransactions){
				//do nothing.
			} else {

				onRemoveUpdateIdAndNames(itemNum,totalTransactions)
			}
			
			
			jQuery("#more-amounts").val(+jQuery("#more-amounts").val() - 1);
			
			return false;
		};

		element.children(':last').append(removeLinkDiv);
		
	}
}



