jQuery.noConflict();

function replaceAll(strMain, strFind, strRep) {
	return strMain.replace(new RegExp(strFind, 'g'), strRep);
}

function downloadEncryptedFileWS(url, id, rd, type) {
	jQuery.ajax({
		type : "post",
		url : url,
		data : {
			id : id,
			rd : rd,
			type : type
		}
	});
}

function clearAll(url) {
	jQuery('#fromDate').val("");
	jQuery('#toDate').val("");
	jQuery('#customerName').val("");
	jQuery('#customerBin').val("");
	jQuery('#reportName').val("");
	jQuery("#reportForm").attr("action", url);
	jQuery('#reportForm').submit();
}

function filterByDates(url) {
	jQuery("#reportForm").attr("action", url);
	jQuery("#reportForm").submit();
}

function openReportDetails(divId) {
	jQuery("#" + divId).modal({
		opacity 	: 30,
		minWidth 	: 500,
		maxWidth	: 800,
		closeClass 	: "modal-close",
		overlayCss 	: {
			backgroundColor : "#000"
		}
	});
}

function closeModel() {
	jQuery.modal.close();
}