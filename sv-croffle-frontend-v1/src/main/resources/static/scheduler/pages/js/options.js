const importWB = new ExcelJS.Workbook();
$(document).ready(function() {
	croffle.initialize();
});

const croffle = {
	initialize: async function() {
		let _self = croffle;

		await _self.optionGroup.initialize();
		_self.option.initialize();

	},
	optionGroup: {
		initialize: async function() {
			let _self = croffle;
			let optionGroupRes = await _self.optionGroup.search(); //resolved 될 때까지 대기
			$.each(optionGroupRes.data, function(idx, obj){
				_self.optionGroup.list[obj.id]=obj.name;
			});
			_self.optionGroup.setModal();
			_self.optionGroup.setEventEnter();
		},
		list: {},
		search: function() {
			return new Promise(function(resolve, reject) {
				$.ajax({
					url: `${CROFFLE_URL}/v1/api/scheduler/management/optiongroups`,
					type: 'GET',
					data: {},
					dataType: 'JSON',
					contentType: 'application/json; charset=utf-8',
				}).then(function(res) {
					resolve(res);
				});
			});
		},
		save: function() {
			let _self = croffle;
			let _name = $("#optiongroup_name").val();
			if (_name.trim().length == 0) {
				cCommon.alert.fail('Fail', '옵션 그룹명을 입력하세요.');
				return;
			}

			let params = { name: _name };
			console.log(JSON.stringify(params));
			$.ajax({
				url: `${CROFFLE_URL}/v1/api/scheduler/management/optiongroups`,
				type: 'POST',
				data: JSON.stringify(params),
				contentType: 'application/json; charset=utf-8',
			}).then(function(res) {
				console.log(res);
				if (res.code == '200') {
					$("#optiongroup_name").val('');
					cCommon.alert.success('Success', '저장 되었습니다.');
					_self.optionGroup.modal.hide();
				} else {
					cCommon.alert.fail('Fail', '옵션 그룹명을 확인하세요.');
				}
			});
		},
		delete: function() {
			let _self = croffle;
			let params = { name: $("#optiongroup_name").val() };
			console.log(JSON.stringify(params));
			$.ajax({
				url: `${CROFFLE_URL}/v1/api/scheduler/management/optiongroups`,
				type: 'DELETE',
				data: JSON.stringify(params),
				contentType: 'application/json; charset=utf-8',
			}).then(function(res) {
				if (res.code == '200') {
					$("#optiongroup_name").val('');
					cCommon.alert.success('Success', '삭제 되었습니다.');
					_self.optionGroup.modal.hide();
				} else {
					cCommon.alert.fail('Fail', '옵션 그룹명을 확인하세요.');
				}
			});
		},
		modal: null,
		setModal: function() {
			let _self = croffle;
			_self.optionGroup.modal = new bootstrap.Modal('#modal_optiongroup');			
		},
		showModal: function() {
			let _self = croffle;
			_self.optionGroup.modal.show();
			setTimeout(function(){
				$("#optiongroup_name").focus();
			}, 500);
		},
		setEventEnter: function() {
			let _self = croffle;
			$("#optiongroup_name").on('keydown', function(e) {
				if (e.keyCode == 13) {
					croffle.optionGroup.save();
				}
			});
		},
	},
	option: {
		initialize: function() {
			let _self = croffle;
			_self.option.table.initialize();
			_self.option.setModal();
			
			$("#file_import").on('change', function(e){
				let file = e.target.files[0];
				let reader = new FileReader();
				reader.readAsArrayBuffer(file)
				
				reader.onload = function(e){
					console.log('Read xlsx to ArrayBuffer...');
					_self.option.table.import(e.currentTarget.result);
				}
			});
		},
		setModal: function(){
			let _self = croffle;
			console.log('set Modal');
			_self.option.excel.modal = new bootstrap.Modal('#modal_excel');
		},
		excel: {
			modal: null
		},
		table: {
			id: "tbl_option",
			datatable: null,
			columns: [
				{ data: 'id', name: 'ID', visible: false },
				{
					data: 'application_id',
					name: 'ApplicationID',
					type: 'dom-select',
					render: function(data, type, row, meta) {
						let startStr = '<select class="form-select">';
						let endStr = '</select>'
						let optionStr = '<option value="1"';
						if(data == '2'){
							optionStr += '>1</option><option value="2" selected>2</option>';
						}else{
							optionStr += ' selected>1</option><option value="2">2</option>';
						}

						return startStr + optionStr + endStr;
					},
					createdCell: function(cell, cellData, rowData, rowIndex, colIndex) {
						let _self = croffle;
						$(cell).on('change', function() {
							_self.option.table.datatable.cell(this).data(Number($(cell).find('select').val()));
							console.log(_self.option.table.datatable.cell(this).data());

							$(this).parent().attr('_modified', 'change');
						});
					}
				},
				{
					data: 'group_id',
					name: 'Group',
					type: 'dom-select',
					render: function(data, type, row, meta) {
						let makeStr = '<select class="form-select"><option value="0"';
						if (data == '') makeStr += ' selected';
						makeStr += '>-</option>';

						$.each(croffle.optionGroup.list, function(key, val) {
							makeStr += `<option value="${key}"`
							if (data == key) makeStr += ' selected';
							makeStr += `>${val}</option>`;
						});

						makeStr += '</select>';
						return makeStr;
					},
					createdCell: function(cell, cellData, rowData, rowIndex, colIndex) {
						let _self = croffle;
						$(cell).on('change', function() {
							_self.option.table.datatable.cell(this).data(Number($(cell).find('select').val()));
							console.log(_self.option.table.datatable.cell(this).data());

							$(this).parent().attr('_modified', 'change');
						});
					}
				},
				{
					data: 'name',
					name: 'Name',
					type: 'dom-text',
					render: function(data, type, row, meta) {
						return `<input type="text" class="form-control" value="${data}" data-bs-toggle="tooltip">`;
					},
					createdCell: function(cell, cellData, rowData, rowIndex, colIndex) {
						let _self = croffle;
						$(cell).on('change', function() {
							_self.option.table.datatable.cell(this).data($(cell).find('input').val());
							console.log(_self.option.table.datatable.cell(this).data());
							$(cell).parent().attr('_modified', 'change');
						});
					}
				},
				{
					data: 'value',
					name: 'Value',
					type: 'dom-text',
					render: function(data, type, row, meta) {
						return `<input type="text" class="form-control" value="${data}" data-bs-toggle="tooltip">`;
					},
					createdCell: function(cell, cellData, rowData, rowIndex, colIndex) {
						let _self = croffle;
						$(cell).on('change', function() {
							_self.option.table.datatable.cell(this).data($(cell).find('input').val());
							console.log(_self.option.table.datatable.cell(this).data());
							$(cell).parent().attr('_modified', 'change');
						});
					}
				},
				{
					data: 'created_date',
					name: 'CreatedDate',
					render: function(data, type, row, meta) {
						return data != '' ? `${data.substring(0, 4)}-${data.substring(4, 6)}-${data.substring(6, 8)} ${data.substring(8, 10)}:${data.substring(10, 12)}:${data.substring(12, 14)}` : '';
					}
				},
				{
					data: 'modified_date',
					name: 'ModifiedDate',
					render: function(data, type, row, meta) {
						return data != '' ? `${data.substring(0, 4)}-${data.substring(4, 6)}-${data.substring(6, 8)} ${data.substring(8, 10)}:${data.substring(10, 12)}:${data.substring(12, 14)}` : '';
					}
				},
			],
			initialize: function() {
				let _self = croffle;
				_self.option.table.datatable = new DataTable(`#${_self.option.table.id}`, {
					columns: _self.option.table.columns,
					destroy: true,
					info: false,
					paging: true,
					searchDelay: 1000,
					lengthMenu: [-1],
					lengthChange: false,
					pageLength: 50,
					order: [[1, 'asc']],
					scrollCollapse: true,
					scrollY: '65vh',
					/*rowGroup: {
						dataSrc: 4,
					},*/
					initComplete: function(settings, json) {
					},
					drawCallback: function(settings) {
						$(`#${_self.option.table.id} tbody > tr`).on('click', function() {
							$(this).toggleClass('selected');
						})
					},
				});
				croffle.option.search();
			},
			serialize: function(code) {
				let _self = croffle;
				let serializeList = [];
				let trList = [];
				if(code == 'modified') trList = _self.option.table.datatable.$('tr[_modified]');
				else trList = _self.option.table.datatable.$('tr');
				
				$.each(trList, function(idx, obj) {
					let param = _self.option.table.datatable.row(obj).data();
					param['group_name'] = _self.optionGroup.list[param.group_id]; 
					console.log(param);
					param._modified = $(obj).attr('_modified');
					serializeList.push(param);
				});
				return serializeList;
			},
			addRow: function() {
				let _self = croffle;
				_self.option.table.datatable.row.add({
					"id": 0,
					"application_id": 1,
					"group_id": 0,
					"name": "",
					"value": "",
					"created_date": "",
					"modified_date": "",
				}).draw();
			},
			removeRow: function() {
				let _self = croffle;
				let removeList = _self.option.table.datatable.rows('.selected').nodes();

				$.each(removeList, function(idx, obj) {
					console.log($(obj));
					$(obj).css('display', 'none');
					$(obj).attr('_modified', 'remove');
				});
			},
			export: async function() {
				let _self = croffle;

				const wb = new ExcelJS.Workbook();
				
				const sheet = wb.addWorksheet('My Shhet');

				let sheetcolumn = [];
				sheet.columns = [];
				
				$.each(_self.option.table.columns, function(idx, obj){
					if(obj.data=='group_id'){
						sheetcolumn.push({ header: 'Group', key: 'group_name' });
					}else{
						sheetcolumn.push({ header: obj.name, key: obj.data });
					}
				});
				sheet.columns = sheetcolumn;
				
				let rowList = _self.option.table.serialize();
				
				$.each(rowList, function(idx, obj){
					sheet.addRow(obj);
				});
				
				await wb.xlsx.writeBuffer().then(data => {
					const blob = new Blob([data], { type: this.blobType });
					saveAs(blob, `scheduler_option_${dayjs().format('YYYYMMDDHHmmss')}.xlsx`);
				});
				_self.option.excel.modal.hide();
			},
			import: async function(file) {
				let _self = croffle;
				/*importWB = new ExcelJS.Workbook();*/
				console.log('Read xlsx to Workbook...');
				await importWB.xlsx.load(file);
				console.log(importWB);
				const columnList = [];
				importWB.getWorksheet(1).getRow(1).eachCell(function(cell) {
					$.each(croffle.option.table.columns, function(idx, obj){
						if(cell.value == obj.name){
							columnList.push(obj.data);
						}
					});
				});
				console.log(columnList);
				
				let optionGroupList = {};
				$.each(_self.optionGroup.list, function(key, val){
					optionGroupList[val]=key;
				});
				importWB.getWorksheet(1).eachRow(function(row, idx) {
					if(idx == 1){
						return;
					}
					let thisNode = _self.option.table.datatable.row.add({
						"id": 0,
						"application_id": row.getCell(2).value,
						"group_id": optionGroupList[row.getCell(3).value],
						"name": row.getCell(4).value,
						"value": row.getCell(5).value,
						"created_date": "",
						"modified_date": "",
					}).node();
					$(thisNode).attr('_modified', 'import');
				});
				_self.option.table.datatable.draw();
				
				_self.option.excel.modal.hide();
			},
		},
		search: function() {
			let _self = croffle;
			_self.option.table.datatable.ajax.url(`${CROFFLE_URL}/v1/api/scheduler/management/option`).load(function() {
				let tooltipList = $('input[data-bs-toggle="tooltip"]');
				$.each(tooltipList, function(idx, obj) {
					$(obj).attr('data-bs-placement', 'top');
					$(obj).attr('data-bs-html', 'true');
					$(obj).attr('data-bs-title', '<div>' + $(obj).val() + '</div>');
					new bootstrap.Tooltip($(obj));
				});
			});
		},
		save: function() {
			let _self = croffle;
			let params = _self.option.table.serialize('modified');
			$.ajax({
				url: `${CROFFLE_URL}/v1/api/scheduler/management/option`,
				type: 'POST',
				data: JSON.stringify(params),
				dataType: 'JSON',
				contentType: 'application/json; charset=utf-8',
			}).then(function(res) {
				console.log(res);
				if (res.code == '200') {
					$("#optiongroup_name").val('');
					cCommon.alert.success('Success', '저장 되었습니다.');
					_self.optionGroup.modal.hide();
					croffle.option.search();
				} else {
					cCommon.alert.fail('Fail', '저장에 실패하였습니다.');
				}
			});
		},

	},
};


$.fn.dataTable.ext.search.push(
    function( settings, searchData, index, rowData, counter ) {
		let groupTxt = croffle.optionGroup.list[rowData.group_id];
		
		let nameTxt = $(searchData[3]).val();
		let valueTxt = $(searchData[4]).val();
		
		if(groupTxt.indexOf(settings.oPreviousSearch.sSearch) > -1){
			return true;
		}else if(nameTxt.indexOf(settings.oPreviousSearch.sSearch) > -1){
			return true;
		}else if(valueTxt.indexOf(settings.oPreviousSearch.sSearch) > -1){
			return true;
		}else{
			return false;
		}
    }
);