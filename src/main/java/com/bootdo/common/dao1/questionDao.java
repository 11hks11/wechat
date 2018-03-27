package com.bootdo.common.dao1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface questionDao {

//	@Select("select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables"
//	+ " where table_schema = (select database())")
//@Select("SELECT q.questionnaireId AS id,q.questionnaireCn AS `name`,q.subTitle AS `type`,q.begindate AS `date` FROM ls_questionnaire_info q WHERE q.isshow=1")
List<Map<String, Object>> list(Map map);



List<Integer> questionIdOne(Map map);
List<Integer> questionIdTwo(Map map);

List<String> title(Map map);
List<String> questionName(Map map);

List<HashMap<String, String>> map(Map map);


void beginResearch(Map map);

void endResearch(Map map);

void changeBefore(Map map);

void changeAfter(Map map);


}
