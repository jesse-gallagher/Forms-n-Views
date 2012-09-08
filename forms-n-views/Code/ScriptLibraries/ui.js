function fnv_mark_dirty(id) {
	var pane = dijit.byId(id)
	if(!/\bdirty\b/.test(pane.get("class"))) {
		pane.attr("class", pane.get("class") + " dirty")
		pane.attr("title", "* " + pane.get("title"))
	}
}
function fnv_mark_clean(id) {
	var pane = dijit.byId(id)
	if(/\bdirty\b/.test(pane.get("class"))) {
		pane.attr("class", pane.get("class").replace(" dirty", ""))
		pane.attr("title", pane.attr("title").substring(2))
	}
}