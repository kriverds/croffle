let croffle = null;
$(document).ready(function() {
	croffle = new Croffle();
	croffle.init();
});

class Croffle {
	/*
	*	Object
	*/
	screenMode = 2;
	factor = {
		private: {
			inboundCnt: { selector: $("span[name=privateInboundCnt]"), type: "number" },
			inboundTime: { selector: $("span[name=privateInboundTime]"), type: "time" },
			outboundCnt: { selector: $("span[name=privateOutboundCnt]"), type: "number" },
			outboundTime: { selector: $("span[name=privateOutboundTime]"), type: "time" },
			loginTime: { selector: $("span[name=privateLoginTime]"), type: "time" },
			readyTime: { selector: $("span[name=privateReadyTime]"), type: "time" },
			acwTime: { selector: $("span[name=privateAcwTime]"), type: "time" },
			notReadyTime: { selector: $("span[name=privateNotReadyTime]"), type: "time" },
			stateTime: { selector: $("span[name=presentTime]"), type: "time", prevVal: 0 },
			workMode: { selector: $("span[name=presentName]"), type: "workMode", prevMode: 0, prevVal: 0 }
		},
		total: {
			inboundCnt: { selector: $("span[name=totalInboundCnt]"), type: "number" },
			answerCnt: { selector: $("span[name=totalAnswerCnt]"), type: "number" },
			answerRate: { selector: $("span[name=totalAnswerRate]"), type: "%" },
			waitingCnt: { selector: $("span[name=totalWaitingCnt]"), type: "number" },
			readyCnt: { selector: $("span[name=totalReadyCnt]"), type: "number" },
		}
	};
	privateList = {
		skill: [],
		queue: [],
	};
	/*
	*	Initialize
	*/
	init = function() {
		croffle.initLocalStorage();
		croffle.screenToggle();
		croffle.initComponent();
		croffle.startTotal();
		croffle.startPrivate();
	};
	initComponent = function() {
		$(window).on('resize', function(e) {
			let miniWallBoard = JSON.parse(localStorage.getItem("miniWallBoard"));
			let screen = miniWallBoard['screen' + croffle.screenMode];
			screen.outerWidth = e.target.outerWidth;
			screen.outerHeight = e.target.outerHeight;
			localStorage.setItem("miniWallBoard", JSON.stringify(miniWallBoard));
		});
		$(".screen-btn").on('click', function() {
			croffle.screenToggle();
		});
	};
	initLocalStorage = function() {
		let miniWallBoard = localStorage.getItem("miniWallBoard");
		if (miniWallBoard == null) {
			miniWallBoard = {};
			miniWallBoard.screen1 = { outerWidth: 659, outerHeight: 360 };
			miniWallBoard.screen2 = { outerWidth: 840, outerHeight: 124 };
			miniWallBoard.privateSkillGroupList = [];
			miniWallBoard.privateQueueGroupList = [];
			localStorage.setItem("miniWallBoard", JSON.stringify(miniWallBoard));
		}
	};
	/*
	*	Skill/Queue 개인화 세팅
	*/
	startTotal = function() {
		Promise.allSettled([croffle.getPrivateSkillList, croffle.getPrivateQueueList])
			.then(function(result) {
				result[0].value().then(function(res) {
					croffle.privateList.skill = res.data;
					croffle.getSkillData();
				});
				result[1].value().then(function(res) {
					croffle.privateList.queue = res.data;
					croffle.getQueueData();
				});
			});
	};
	getPrivateSkillList = function() {
		return new Promise(function(resolve, reject) {
			const miniWallBoard = JSON.parse(localStorage.getItem("miniWallBoard"));
			const param = {};
			param.skillGroupList = miniWallBoard.privateSkillGroupList.join(',');
			if (param.skillGroupList.length == 0) {
				resolve([]);
			} else {
				$.ajax({
					url: '/v1/wallboard/mini/agent/skill/list',
					type: 'GET',
					data: param,
					dataType: 'JSON',
					contentType: 'application/json; charset=utf-8',
				}).then(function(res) {
					resolve(res);
				});
			}
		});
	};
	getPrivateQueueList = function() {
		return new Promise(function(resolve, reject) {
			const miniWallBoard = JSON.parse(localStorage.getItem("miniWallBoard"));
			const param = {};
			param.queueGroupList = miniWallBoard.privateQueueGroupList.join(',');
			if (param.queueGroupList.length == 0) {
				resolve([]);
			} else {
				$.ajax({
					url: '/v1/wallboard/mini/agent/queue/list',
					type: 'GET',
					data: param,
					dataType: 'JSON',
					contentType: 'application/json; charset=utf-8',
				}).then(function(res) {
					resolve(res);
				});
			}
		});
	};
	/*
	*	Data 가져오기
	*/
	stateTimeInterval = null;
	startPrivate = function() {
		const param = {};
		param.empId = empId;

		croffle.doSetInterval(function() {
			$.ajax({
				url: '/v1/wallboard/mini/agent/stat/private',
				type: 'GET',
				data: param,
				dataType: 'JSON',
				contentType: 'application/json; charset=utf-8',
			}).then(function(res) {
				let workMode = 0;
				let stateTime = 0;
				$.each(res.list[0], function(key, val) {
					const factor = croffle.factor.private[key];
					if (factor != null && key != 'workMode' && key != 'stateTime') {
						$("#screen" + croffle.screenMode).find(factor.selector).text(croffle.formatter(factor.type, val));
					} else if (key == 'workMode') {
						workMode = val;
					} else if (key == 'stateTime') {
						stateTime = val;
					}
				});
				const workModeFactor = croffle.factor.private['workMode'];
				const stateTimeFactor = croffle.factor.private['stateTime'];
				if (workModeFactor.prevMode != workMode) {
					workModeFactor.selector.text(croffle.formatter(workModeFactor.type, workMode));
					workModeFactor.prevMode = workMode;
					stateTimeFactor.prevVal = stateTime;
					stateTimeFactor.selector.text(croffle.formatter(stateTimeFactor.type, stateTime));
					$('img[name=presentImage]').attr('src', croffle.mWorkMode[workMode].image);
					clearInterval(croffle.stateTimeInterval);
					croffle.stateTimeInterval = croffle.doSetInterval(function() {
						const stateTimeFactor = croffle.factor.private['stateTime'];
						let currStateTime = stateTimeFactor.prevVal++;
						stateTimeFactor.selector.text(croffle.formatter(stateTimeFactor.type, currStateTime));
						if (workMode == 30 && currStateTime >= 600) {
							if (croffle.threshold.longTel.interval == null) {
								croffle.threshold.longTel.start();
							}
						} else if (croffle.threshold.longTel.interval != null) {
							croffle.threshold.longTel.stop();
						}
					}, 1000);
				}
			});
		}, 2000);
	};
	getSkillData = function() {
		const param = {};
		if (croffle.privateList.skill.length == 0) {
			return;
		} else {
			let skillIdList = [];
			$.each(croffle.privateList.skill, function(idx, obj) {
				skillIdList.push(obj.skillId);
			});
			param.skillId = skillIdList.join(',');
			croffle.doSetInterval(function() {
				$.ajax({
					url: '/v1/wallboard/mini/agent/stat/total/skill',
					type: 'GET',
					data: param,
					dataType: 'JSON',
					contentType: 'application/json; charset=utf-8',
				}).then(function(res) {
					const statObj = {};
					$.each(res.list[0], function(key) {
						statObj[key] = 0;
					});
					$.each(res.list, function(idx, obj) {
						$.each(obj, function(key, val) {
							if (!isNaN(val)) {
								statObj[key] += Number(val);
							}
						});
					});
					$.each(statObj, function(key, val) {
						let factor = croffle.factor.total[key];
						if (factor != null) {
							factor.selector.text(croffle.formatter(factor.type, val));
						}
					});
				});
			}, 2000);
		}
	};
	getQueueData = function() {
		const param = {};
		if (croffle.privateList.queue.length == 0) {
			return;
		} else {
			let queueIdList = [];
			$.each(croffle.privateList.queue, function(idx, obj) {
				queueIdList.push(obj.queueId);
			});
			param.queueId = queueIdList.join(',');

			croffle.doSetInterval(function() {
				$.ajax({
					url: '/v1/wallboard/mini/agent/stat/total/queue',
					type: 'GET',
					data: param,
					dataType: 'JSON',
					contentType: 'application/json; charset=utf-8',
				}).then(function(res) {
					const statObj = {};
					$.each(res.list[0], function(key) {
						statObj[key] = 0;
					});
					$.each(res.list, function(idx, obj) {
						$.each(obj, function(key, val) {
							if (!isNaN(val)) {
								statObj[key] += Number(val);
							}
						});
					});
					$.each(statObj, function(key, val) {
						let factor = croffle.factor.total[key];
						if (factor != null) {
							factor.selector.text(croffle.formatter(factor.type, val));
						}
					});
					let factor = croffle.factor.total['answerRate'];
					let rateVal = (statObj.answerCnt / statObj.inboundCnt) * 100;
					factor.selector.text(croffle.formatter(factor.type, rateVal.toFixed(2)));
				});
			}, 2000);
		}
	};
	formatter = function(type, val) {
		let result = '';
		if (type == '%') {
			result = val + '%';
		} else if (type == 'time') {
			let date = dayjs(0);
			date = date.subtract(9, 'hour');
			date = date.add(Number(val), 'second');
			result = date.format('HH:mm:ss');
		} else if (type == 'workMode') {
			result = croffle.mWorkMode[val].name;
		} else if (type == 'number') {
			result = Number(val).toLocaleString('ko-KR');
		} else {
			result = val;
		}
		return result;
	};
	/*
	*	Function
	*/
	screenToggle = function() {
		if (croffle.screenMode == 2) {
			croffle.screenMode = 1;
			$("#screen1").show();
			$("#screen2").hide();
		} else if (croffle.screenMode === 1) {
			croffle.screenMode = 2;
			$("#screen1").hide();
			$("#screen2").show();
		}
		let miniWallBoard = JSON.parse(localStorage.getItem("miniWallBoard"));
		let outerWidth = miniWallBoard['screen' + croffle.screenMode].outerWidth;
		let outerHeight = miniWallBoard['screen' + croffle.screenMode].outerHeight;
		window.resizeTo(outerWidth, outerHeight);
	};
	threshold = {
		longTel: {
			interval: null,
			start: function() {

				let colorFlag = false;
				croffle.threshold.longTel.interval = croffle.doSetInterval(function() {
					if (colorFlag === false) {
						colorFlag = true;
						$(".screen-btn").css("background-color", "#F92425");
					} else {
						colorFlag = false;
						$(".screen-btn").css("background-color", "#FFFFFF");
					}
				}, 300);
			},
			stop: function() {
				clearInterval(croffle.threshold.longTel.interval);
				croffle.threshold.longTel.interval = null
				$(".screen-btn").css("background-color", "#FFFFFF");
			},
		}
	};
	openPrivateSetting = function() {
		window.open('http://localhost:8001/v1/wallboard/mini/agent/setting?empId=' + empId, 'setting', 'location=no, directories=no,resizable=no,status=no,toolbar=no,menubar=no, width=500,height=500,left=0, top=0, scrollbars=yes');
	};
	doSetInterval = function(callback, ms) {
		callback();
		return setInterval(callback, ms);
	};
	/*
	*	Const
	*/
	mWorkMode = {
		10: {
			name: '로그아웃', image: "/img/new/img_user_logout.png"
		},
		20: {
			name: '대기', image: "/img/new/img_total_ready.png"
		},
		30: {
			name: '통화중', image: "/img/new/img_user_call.png"
		},
		40: {
			name: '후처리', image: "/img/new/img_acw.png"
		},
		50: {
			name: '휴식', image: "/img/new/img_total_break.png"
		}
	}
}