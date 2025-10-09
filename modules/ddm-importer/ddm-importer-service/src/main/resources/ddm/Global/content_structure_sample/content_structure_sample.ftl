<div>${Text2dte.getData()}</div>

<div>
	<#if (Imagevion.getData())?? && Imagevion.getData() != "">
		<img alt="${Imagevion.getAttribute("alt")}" data-fileentryid="${Imagevion.getAttribute("fileEntryId")}" src="${Imagevion.getData()}" />
	</#if>
</div>

${HTML1yr2.getData()}