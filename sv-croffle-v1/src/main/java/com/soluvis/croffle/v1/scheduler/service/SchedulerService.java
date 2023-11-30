package com.soluvis.croffle.v1.scheduler.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soluvis.croffle.v1.scheduler.mapper.SchedulerMapper;

@Service
public class SchedulerService {

	@Autowired
	SchedulerMapper schedulerMapper;

//	private final Logger logger = LoggerFactory.getLogger(SchedulerService.class);

	public List<Map<String, Object>> selectOptionGroup() throws Exception {

		return schedulerMapper.selectOptionGroup();
	}

	public List<Map<String, Object>> selectOption() throws Exception {

		return schedulerMapper.selectOption();
	}

	public int insertOptionGroup(Map<String, Object> params) throws Exception {

		return schedulerMapper.insertOptionGroup(params);
	}

	public int saveOption(List<Map<String, Object>> params) throws Exception {
		int totalCnt = 0;
		for (Map<String, Object> cMap : params) {
			int dbResult = 0;
			int id = (int) cMap.get("id");
			String modified = (String) cMap.get("_modified");

			if ("remove".equals(modified))
				dbResult = schedulerMapper.deleteOption(cMap);
			else if (id > 0)
				dbResult = schedulerMapper.updateOption(cMap);
			else
				dbResult = schedulerMapper.insertOption(cMap);

			if (dbResult > 0)
				totalCnt += dbResult;
		}
		return totalCnt;
	}

	public int insertOption(Map<String, Object> params) throws Exception {

		return schedulerMapper.insertOption(params);
	}

	public int updateOptionGroup(Map<String, Object> params) throws Exception {

		return schedulerMapper.updateOptionGroup(params);
	}

	public int updateOption(Map<String, Object> params) throws Exception {

		return schedulerMapper.updateOption(params);
	}

	public int deleteOptionGroup(Map<String, Object> params) throws Exception {

		return schedulerMapper.deleteOptionGroup(params);
	}

	public int deleteOption(Map<String, Object> params) throws Exception {

		return schedulerMapper.deleteOption(params);
	}

	public int test() throws Exception {
		int groupId = 32;
		String fileName = "D:\\dev\\test\\scheduler_options2.cfg";
		File file = new File(fileName);


		BufferedReader br = new BufferedReader(new FileReader(file));

		while(br.ready()) {
			Map<String, Object> params = new HashMap<>();
			String strRead = br.readLine();
			params.put("name", strRead.split("=")[0]);
			params.put("value", strRead.split("=")[1]);
			params.put("group_id", groupId);
			int result = schedulerMapper.insertOption(params);
			System.out.println(params.toString() + result);
		}
		br.close();
		return 0;
	}



























}
