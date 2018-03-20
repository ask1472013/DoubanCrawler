package com.crawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaveBookInfo {
	public static final String url = "jdbc:mysql://localhost/Books?characterEncoding=utf8&useSSL=true";
	public static final String name = "com.mysql.jdbc.Driver";
	public static final String user = "root";
	public static final String password = "root";

	Connection conn = null;
	PreparedStatement pst = null;

	public void saveBookInfo(String sql) {
		try {
			Class.forName(name);
			conn = DriverManager.getConnection(url, user, password);
			pst = conn.prepareStatement(sql);
			pst.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.conn.close();
				this.pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	
}
