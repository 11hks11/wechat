package com.bootdo.common.service;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;



@Component
public class test {
	
	@Scheduled(fixedDelay = 1000000)
	public void reportCurrentTime() throws InterruptedException, IOException {
		JSONObject object = new JSONObject(); // 创建Json格式的数据
		object.put("category", "IT");
		object.put("pop", true);
		
		
		String path="C:\\Users\\huangkaisheng\\Desktop\\text1.json";
		// 保证创建一个新文件
        File file = new File(path);
        if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
            file.getParentFile().mkdirs();
        }
        if (file.exists()) { // 如果已存在,删除旧文件
            file.delete();
        }
        file.createNewFile();
		
		
		JSONArray array = new JSONArray();

		JSONObject subJsonObj1 = new JSONObject();
		subJsonObj1.put("id", 1);
		subJsonObj1.put("ide", "Eclipse");
		subJsonObj1.put("name", "Java");
		array.add(subJsonObj1);

		JSONObject subJsonObj2 = new JSONObject();
		subJsonObj2.put("id", 2);
		subJsonObj2.put("ide", "XCode");
		subJsonObj2.put("name", "Swift");
		array.add(subJsonObj2);

		object.put("languages", array);

		System.out.println(object.toString());
		System.err.println(new Date());
		
		String jsonString=object.toString();
		jsonString = JsonFormatTool.formatJson(jsonString);
		
		Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        write.write(jsonString);
        write.flush();
        write.close();

	}
}