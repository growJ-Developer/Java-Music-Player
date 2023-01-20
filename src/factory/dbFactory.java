package factory;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import app.mainApp;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class dbFactory {
	private String databaseUrl = "";
	private int maxPoolSize = 10;
	private int connNum = 0;
	
	private static final String SQL_VERIFYCONN = "SELECT 1";
	
	Stack<Connection> freePool = new Stack<>();
	Set<Connection> occuupiedPool = new HashSet<>();
	
	public dbFactory() {
		
	}
	
	/* SQL 구문에 대한 결과를 HASHMap으로 반환합니다 */
	public static ArrayList<HashMap<String, Object>> getQueryResult(String sql){
		Connection conn = null;
				
		try {
			//SQLite JDBC 체크
			Class.forName("org.sqlite.JDBC");
			
			//SQLite 데이터베이스 파일에 연결
			//String url = "jdbc:sqlite::resource:JMusicPlayerDB.db";
			//개발용(Windows)
			String url = "jdbc:sqlite:/resource/JMusicPlayerDB.db";
			conn = DriverManager.getConnection(url);
			
			Statement statement = conn.createStatement();
			ResultSet res = statement.executeQuery(sql);
            ResultSetMetaData md = res.getMetaData();
            int columns = md.getColumnCount();
            
            ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
            while(res.next()) {
            	HashMap<String, Object> row = new HashMap<String, Object>(columns);
            	for(int i = 1; i <= columns; ++i) {
            		row.put(md.getColumnName(i), res.getObject(i));
            	}
            	list.add(row);
            }
			
            return list;
        } catch (Exception e) {
        	e.printStackTrace();
		} finally {
            if (conn != null) {
            	try {
            		conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        }
		return null;
	}
	
	/* INSERT Query를 수행합니다  */
	public static boolean insertQuery(String sql){
		Connection conn = null;
				
		try {
			//SQLite JDBC 체크
			Class.forName("org.sqlite.JDBC");
			
			//SQLite 데이터베이스 파일에 연결
			//String url = "jdbc:sqlite::resource:JMusicPlayerDB.db";
			//개발용(Windows)
			String url = "jdbc:sqlite:/resource/JMusicPlayerDB.db";
			conn = DriverManager.getConnection(url);
			
			Statement statement = conn.createStatement();
			statement.execute(sql);
            			
			return true;
        } catch (Exception e) {
        	e.printStackTrace();
		} finally {
            if (conn != null) {
            	try {
            		conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        }
		return false;
	}
	
	/* UPDATE Query를 수행합니다  */
	public static boolean updateQuery(String sql){
		Connection conn = null;
				
		try {
			//SQLite JDBC 체크
			Class.forName("org.sqlite.JDBC");
			
			//SQLite 데이터베이스 파일에 연결
			//String url = "jdbc:sqlite::resource:JMusicPlayerDB.db";
			//개발용(Windows)
			String url = "jdbc:sqlite:/resource/JMusicPlayerDB.db";
			conn = DriverManager.getConnection(url);
			
			Statement statement = conn.createStatement();
			statement.executeUpdate(sql);
            			
			return true;
        } catch (Exception e) {
        	e.printStackTrace();
		} finally {
            if (conn != null) {
            	try {
            		conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        }
		return false;
	}
		
	/* DELETE Query를 수행합니다  */
	public static boolean deleteQuery(String sql){
		Connection conn = null;
				
		try {
			//SQLite JDBC 체크
			Class.forName("org.sqlite.JDBC");
			
			//SQLite 데이터베이스 파일에 연결
			//String url = "jdbc:sqlite::resource:JMusicPlayerDB.db";
			//개발용(Windows)
			String url = "jdbc:sqlite:/resource/JMusicPlayerDB.db";
			conn = DriverManager.getConnection(url);
			
			Statement statement = conn.createStatement();
			statement.executeUpdate(sql);
            			
			return true;
        } catch (Exception e) {
        	e.printStackTrace();
		} finally {
            if (conn != null) {
            	try {
            		conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        }
		return false;
	}

}
