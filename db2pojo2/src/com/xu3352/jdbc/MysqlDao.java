package com.xu3352.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.xu3352.core.Column;
import com.xu3352.util.StringUtil;

/**
 * MySQL database Dao
 * @author xuyl
 * @date 2013-1-7
 */
public class MysqlDao extends AbstractDaoSupport {
	
	@Override
	public List<String> queryAllTables() {
		return queryAllTables("show tables");
	}

	@Override
	public List<Column> queryColumns(String tableName) {
		List<Column> list = new ArrayList<Column>();
		try {
			checkDriver();
			Connection conn = getConn();
			ResultSet rs = createQuary(conn, "show full fields from " + tableName);
			while (rs.next()) {
				String type = typesConvert(rs.getString(2));
				String javaStyle = StringUtil.javaStyle(rs.getString(1));
				list.add(new Column(type, rs.getString(1), javaStyle, rs.getString(9)));
			}
			rs.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public String typesConvert(String mysqlType) {
		if (mysqlType.startsWith("varchar") || mysqlType.startsWith("longtext") || mysqlType.startsWith("text")) {
			return "String";
		} else if (mysqlType.startsWith("int") || mysqlType.startsWith("bigint") || mysqlType.startsWith("tinyint")) {
			return "Integer";
		} else if (mysqlType.startsWith("double")) {
			return "Double";
		} else if (mysqlType.startsWith("date")) {
			return "Date";
		} 
		return mysqlType;
	}
	
	/**
	 * 测试入口
	 * @param args
	 */
	public static void main(String[] args) {
		AbstractDaoSupport dao = new MysqlDao();
		List<String> tables = dao.queryAllTables();
		System.out.println(tables);
		List<Column> list = dao.queryColumns(tables.get(0));
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
}
