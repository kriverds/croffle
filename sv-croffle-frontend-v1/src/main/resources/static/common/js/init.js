$(document).ready(function() {
	loadingSpinner.initialize();
	cCommon.initialize();
});

const cCommon = {
	initialize: function() {
		let _self = cCommon;
		_self.selectSidebar();
		_self.initTooltip();
		_self.setAjax();
		_self.alert.setModal();
	},
	selectSidebar: function() {
		$(".nav-link").removeClass('active');
		$('[data-ds="' + TITLE + '"].nav-link').addClass("active");
	},
	initTooltip: function() {
		let tooltipList = $('[data-bs-toggle="tooltip"]');
		$.each(tooltipList, function(idx, obj) {
			new bootstrap.Tooltip($(obj));
		});
	},
	setAjax: function() {
		$(document).ajaxStart(function() {
			loadingSpinner.show();
		});
		$(document).ajaxComplete(function() {
			loadingSpinner.hide();
		});
		$(document).ajaxError(function(e1, e2, e3, e4, e5) {
			console.log(e1);
			console.log(e2);
			console.log(e3);
			console.log(e4);
			console.log(e5);
		});
	},
	alert: {
		success: function(title, msg) {
			let _self = cCommon;
			$("#modal_success_title").text(title);
			$("#modal_success_message").text(msg);
			_self.alert.modal.success.show();
		},
		fail: function(title, msg) {
			let _self = cCommon;
			$("#modal_fail_title").text(title);
			$("#modal_fail_message").text(msg);
			_self.alert.modal.fail.show();
		},
		modal: {
			success: null,
			fail: null,
		},
		setModal: function() {
			let _self = cCommon;
			_self.alert.modal.success = new bootstrap.Modal('#modal_success');
			_self.alert.modal.fail = new bootstrap.Modal('#modal_fail');
		},
	},
};

const loadingSpinner = {
	initialize: function() {
		let _self = loadingSpinner;
		_self.modal = new bootstrap.Modal('#modal_loading', {
			keyboard: false,
			/*backdrop: "static",*/
		});

	},
	modal: null,
	process: "ready",
	retryMs: 300,
	show: function() {
		let _self = loadingSpinner;
		let currDisplay = $("#modal_loading").css('display');
		console.log('show:'+currDisplay);

		if (_self.process == "ready" && currDisplay == 'none') {
			_self.process = "show";

			if (_self.waitInterval.show != null) {
				clearInterval(_self.waitInterval.show);
				_self.waitInterval.show = null;
			}
			_self.modal.show();
			_self.toggleInterval.show = setInterval(function() {
				let currDisplay = $("#modal_loading").css('display');
				console.log('show:'+currDisplay);
				if (currDisplay == 'block') {
					clearInterval(_self.toggleInterval.show);
					_self.toggleInterval.show = null;
					_self.process = "ready";
					console.log('loadingSpinner Show');
				} else {
					_self.modal.show();
				}
			}, _self.retryMs);
		} else if (_self.process == "hide") {
			if (_self.waitInterval.show == null) {
				_self.waitInterval.show = setInterval(function() {
					_self.show();
				}, _self.retryMs);
			}
		}
	},
	hide: function() {
		let _self = loadingSpinner;
		let currDisplay = $("#modal_loading").css('display');
		console.log('show:'+currDisplay);
		
		if (_self.process == "ready" && currDisplay == 'block') {
			_self.process = "hide";

			if (_self.waitInterval.hide != null) {
				clearInterval(_self.waitInterval.hide);
				_self.waitInterval.hide = null;
			}

			_self.modal.hide();
			_self.toggleInterval.hide = setInterval(function() {
				let currDisplay = $("#modal_loading").css('display');
				console.log('show:'+currDisplay);
				if (currDisplay == 'none') {
					clearInterval(_self.toggleInterval.hide);
					_self.toggleInterval.hide = null;
					_self.process = "ready";
					console.log("loadingSpinner Hide")
				} else {
					_self.modal.hide();
				}
			}, _self.retryMs);
		} else if (_self.process == "show") {
			if (_self.waitInterval.hide == null) {
				_self.waitInterval.hide = setInterval(function() {
					_self.hide();
				}, _self.retryMs);
			}
		}
	},
	toggleInterval: { show: null, hide: null },
	waitInterval: { show: null, hide: null },
};

$.fn.dataTable.ext.order["dom-select"] = function(settings, col) {
	return this.api().column(col, { order: 'index' }).nodes().map(function(td, i) {
		return $('select option:checked', td).text() == undefined ? $(td).text() : $('select option:checked', td).text();
	});
};
$.fn.dataTable.ext.order["dom-text"] = function(settings, col) {
	return this.api().column(col, { order: 'index' }).nodes().map(function(td, i) {
		return $('input', td).val() == undefined ? $(td).text() : $('input', td).val();
	});
};