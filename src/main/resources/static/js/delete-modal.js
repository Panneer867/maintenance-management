function deleteId(href) {
	$('#delRef').attr('href', href);
}

function enable() {
	document.getElementById('senableApp').value = $('#ienableApp').is(':checked') ? $('#ienableApp').val() : "off";
}