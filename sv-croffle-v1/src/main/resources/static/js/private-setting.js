$(document).ready(function() {
	croffle.init();

	$(window).on('unload', function() {
		opener.location.reload();
	});
});

const croffle = {
	init: async function() {
		window.resizeTo(570, 510);
		const _self = croffle;
		_self.initModal();
		const results = await Promise.allSettled([_self.getSkillGroupList, _self.getQueueGroupList]);
		_self.skillGroupList = results[0].value.data;
		_self.queueGroupList = results[1].value.data;
		_self.initDataTable();
		_self.initCheckBox();
	},
	skillGroupTable: null,
	skillGroupList: [],
	queueGroupTable: null,
	queueGroupList: [],
	initCheckBox: function(){
		$("#skillGroupHeaderCheckbox").on("change", function() {
			const checkBoxChecked = $("#skillGroupHeaderCheckbox").is(":checked");
			$.each($("#skillGroupTbl tbody > tr"), function(idx,obj){
				$(obj).find('input').prop("checked", checkBoxChecked);
				if(checkBoxChecked === true){
					$(obj).addClass("checked");
				}else{
					$(obj).removeClass("checked");
				}
			});
		});
		$("#queueGroupHeaderCheckbox").on("change", function() {
			const checkBoxChecked = $("#queueGroupHeaderCheckbox").is(":checked");
			$("#queueGroupTbl tbody").find('input').prop("checked", checkBoxChecked);
		});
	},
	successModal: null,
	initModal: function() {
		const _self = croffle;
		new bootstrap.Modal('#modalSaveSkillGroup');
		new bootstrap.Modal('#modalSaveQueueGroup');
		new bootstrap.Modal('#exampleModalSm');
		_self.successModal = new bootstrap.Modal('#modalSuccess');
	},
	initDataTable: function() {
		const _self = croffle;
		_self.skillGroupTable = new DataTable('#skillGroupTbl', {
			columns: [
				{ data: 'zcheck', name: 'check', width: '10px', orderable: false },
				{ data: 'skillGroupId', name: 'ID', width: '10px', visible: false },
				{ data: 'skillGroupName', name: 'Name' },
			],
			destroy: true,
			info: false,
			paging: false,
			searching: false,
			searchDelay: 1000,
			lengthMenu: [-1],
			lengthChange: false,
			pageLength: 50,
			order: [[1, 'asc']],
			scrollCollapse: false,
			scrollY: '300px',
			initComplete: function(settings, json) {
			},
			drawCallback: function(settings) {
				$.each($('#skillGroupTbl tbody > tr'), function(idx,obj){
					if($(obj).find('input').is(":checked") === true){
						$(obj).addClass('checked');						
					}
				});
				$('#skillGroupTbl tbody > tr').on('change', function() {
					$(this).toggleClass('checked');
				});
			},
		});
		_self.queueGroupTable = new DataTable('#queueGroupTbl', {
			columns: [
				{ data: 'zcheck', name: 'check', width: '10px', orderable: false },
				{ data: 'queueGroupId', name: 'ID', width: '10px', visible: false },
				{ data: 'queueGroupName', name: 'Name' },
			],
			destroy: true,
			info: false,
			paging: false,
			searching: false,
			searchDelay: 1000,
			lengthMenu: [-1],
			lengthChange: false,
			pageLength: 50,
			order: [[1, 'asc']],
			scrollCollapse: false,
			scrollY: '300px',
			initComplete: function(settings, json) {
			},
			drawCallback: function(settings) {
				$.each($('#queueGroupTbl tbody > tr'), function(idx,obj){
					if($(obj).find('input').is(":checked") === true){
						$(obj).addClass('checked');						
					}
				});
				$('#queueGroupTbl tbody > tr').on('change', function() {
					$(this).toggleClass('checked');
				});
			},
		});
		const privateSkillGroup = _self.getPrivateSkillGroupList();
		const privateQueueGroup = _self.getPrivateQueueGroupList();
		console.log(privateSkillGroup);
		$.each(_self.skillGroupList, function(idx,obj){
			let _zcheck = '<div class="form-check form-switch"><input class="form-check-input" type="checkbox" role="switch"';
			$.each(privateSkillGroup, function(idx2,skillGroupId){
				if(obj.skillGroupId == skillGroupId){
					_zcheck += ' checked';
					return false;
				}
			});
			_zcheck += '></div>';
			_self.skillGroupTable.row.add({
				zcheck: _zcheck,
				skillGroupName: obj.skillGroupName,
				skillGroupId: obj.skillGroupId
			});
		});
		$.each(_self.queueGroupList, function(idx,obj){
			let _zcheck = '<div class="form-check form-switch"><input class="form-check-input" type="checkbox" role="switch"';
			$.each(privateQueueGroup, function(idx2,queueGroupId){
				if(obj.queueGroupId == queueGroupId){
					_zcheck += ' checked';
					return false;
				}
			});
			_zcheck += '></div>';
			_self.queueGroupTable.row.add({
				zcheck: _zcheck,
				queueGroupName: obj.queueGroupName,
				queueGroupId: obj.queueGroupId
			});
		});
		_self.skillGroupTable.draw();
		_self.queueGroupTable.draw();
	},
	getSkillGroupList: new Promise(function(resolve, reject) {
		$.ajax({
			url: '/v1/wallboard/mini/agent/setting/skillgroup',
			type: 'GET',
			data: {},
			dataType: 'JSON',
			contentType: 'application/json; charset=utf-8',
		}).then(function(res) {
			return resolve(res);
		});
	}),
	getQueueGroupList: new Promise(function(resolve, reject) {
		$.ajax({
			url: '/v1/wallboard/mini/agent/setting/queuegroup',
			type: 'GET',
			data: {},
			dataType: 'JSON',
			contentType: 'application/json; charset=utf-8',
		}).then(function(res) {
			return resolve(res);
		});
	}),
	saveSkillGroupList: function(){
		const _self = croffle;
		const checkedData = _self.skillGroupTable.rows(".checked").data();
		let saveSkillGroupList = [];
		$.each(checkedData, function(idx,obj){
			saveSkillGroupList.push(obj.skillGroupId);
		});
		console.log(saveSkillGroupList);
		let miniWallBoard = JSON.parse(localStorage.getItem("miniWallBoard"));
		miniWallBoard.privateSkillGroupList = saveSkillGroupList;
		localStorage.setItem("miniWallBoard", JSON.stringify(miniWallBoard));
		console.log(localStorage.getItem("miniWallBoard"));
		_self.successModal.show();
	},
	saveQueueGroupList: function(){
		const _self = croffle;
		const checkedData = _self.queueGroupTable.rows(".checked").data();
		let saveQueueGroupList = [];
		$.each(checkedData, function(idx,obj){
			saveQueueGroupList.push(obj.queueGroupId);
		});
		console.log(saveQueueGroupList);
		let miniWallBoard = JSON.parse(localStorage.getItem("miniWallBoard"));
		miniWallBoard.privateQueueGroupList = saveQueueGroupList;
		localStorage.setItem("miniWallBoard", JSON.stringify(miniWallBoard));
		console.log(localStorage.getItem("miniWallBoard"));
		_self.successModal.show();
	},
	getPrivateSkillGroupList: function() {
		let miniWallBoard = JSON.parse(localStorage.getItem("miniWallBoard"));
		return miniWallBoard.privateSkillGroupList;
	},
	getPrivateQueueGroupList: function(){
		let miniWallBoard = JSON.parse(localStorage.getItem("miniWallBoard"));
		return miniWallBoard.privateQueueGroupList;
	}
}


