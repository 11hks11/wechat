package com.bootdo.common.controller;

import com.alibaba.fastjson.JSON;
import com.bootdo.common.dao1.questionDao;
import com.bootdo.common.service.GeneratorService;
import com.bootdo.common.utils.GenUtils;
import com.bootdo.common.utils.R;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
@RequestMapping("/common/generator")
@Controller
public class GeneratorController {
	String prefix = "common/generator";
	@Autowired
	GeneratorService generatorService;
	
	@Autowired
	questionDao questionDao;
	
	
	@GetMapping()
	String generator() {
		return prefix + "/list";
	}

	@ResponseBody
	@GetMapping("/list")
	List<Map<String, Object>> list(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<"+request.getParameter("typeId"));
		
		String typeId=request.getParameter("typeId");
		
		Map map = new HashMap<>();
		
		
		map.put("typeId", typeId);
		
		 
		List<Map<String, Object>> list = generatorService.list(map);
		return list;
	};

	
	 
	
	@RequestMapping("/dataOut")
	public void dataOut(HttpServletRequest request, HttpServletResponse response,int id) throws IOException {
		
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+id);
		Map map = new HashMap<>();
		
		map.put("id", id);
		OutputStream os = null ;
		ServletOutputStream out = null;
        XSSFWorkbook xbook = new XSSFWorkbook();    
        SXSSFWorkbook book = new SXSSFWorkbook(xbook, 100);
        List<Integer> questionIdOne = questionDao.questionIdOne(map);
        List<Integer> questionIdTwo = questionDao.questionIdTwo(map);
        
        map.put("questionIdOne", questionIdOne);
        map.put("questionIdTwo", questionIdTwo);
        
        //把特殊符号.转成。 查出来后在后面再转回来
        questionDao.changeBefore(map);
        
        List<String> title=questionDao.title(map);
        map.put("title", title);
        List<String> questionName=questionDao.questionName(map);
        
        
        List<HashMap<String,String>> list= questionDao.map(map);
        System.out.println(list);
        
        try {     
            Sheet sheet= book.createSheet("test");  
            Row titlerow = sheet.createRow(0);
            for(int i=0;i<list.size();i++){
            	Row row = sheet.createRow(i+1);
            	
            	List<String> mapList=new ArrayList<String>();
            	List<String> titleList=new ArrayList<String>();
            	for(Map.Entry entry :list.get(i).entrySet()){
            		if(i==0){
            			titleList.add(String.valueOf(entry.getKey()));
            		}
            		mapList.add(String.valueOf(entry.getValue()));
            	}
            	
            	if(titleList.size()>0){
            		for(int j=0;j<titleList.size();j++){
            			
            			titlerow.createCell(j).setCellValue(titleList.get(j).replace("。", "."));		
            		}
            	}
            	for(int j=0;j<mapList.size();j++){
            		row.createCell(j).setCellValue(mapList.get(j));
            		
            		int l =i%100 ;
            	}
            }
            
            //在这里把特殊符号转回来
            questionDao.changeAfter(map);
            
            String excelName = java.net.URLEncoder.encode(questionName.get(0), "UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");  
			response.setHeader("Content-Disposition", "attachment;filename="+excelName+".xlsx" );  
			out = response.getOutputStream();  
            book.write(out); 
            
                
        } catch (FileNotFoundException e) {    
            e.printStackTrace();    
        } catch (IOException e) {    
            e.printStackTrace();    
        } finally {     
            // 关闭输出流    
            try {    
            	out.close();    
            } catch (IOException e) {    
                e.printStackTrace();    
            }    
        }  
        
	 
		
	}	
	
	@ResponseBody
	@PostMapping("/begin")
	public R beginResearch(int id){
		
		Map map = new HashMap<>();
			
		 
		map.put("id", id);
		
		questionDao.beginResearch(map);
		
		return R.ok();
	}
	@ResponseBody
	@PostMapping("/end")
	public R  endResearch(int id){
		
		
		Map map = new HashMap<>();
		
		map.put("id", id);
		
		if(id==3 ||id==21 ||id==40 ||id==60 ||id==80 ||id==20100 || id==20200 ||id==20300){
			return R.error(1, "非测试问卷不要随便结束!");
		}
		
		questionDao.endResearch(map);
		return R.ok();
		
	}
	
	
	
}
