package MariaDB;

import java.sql.*;

public class ConnectDB {
	// 싱글톤 패턴으로 사용 하기위 한 코드들
	private static ConnectDB instance = new ConnectDB();

	public static ConnectDB getInstance() {
		return instance;
	}

	public ConnectDB() {

	}
	private String driver = "org.mariadb.jdbc.Driver";
	private String jdbcUrl = ""; // MySQL 계정 
	private String dbId = ""; // MySQL 계정 "로컬일 경우 root"
	private String dbPw = ""; // 비밀번호 "mysql 설치 시 설정한 비밀번호"
	
	
	//회원 가입
	public String memberInsert(String mem_id, String mem_passwd, String mem_nickname, String mem_birth, String mem_job, String mem_interest) {
		Connection con = null;
		PreparedStatement ps = null;
		int cnt;
		String sql = "";
		String returns = "";
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql = "insert into member values(?,?,?,?,?,?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, mem_id);
			ps.setString(2, mem_passwd);
			ps.setString(3, mem_nickname);
			ps.setString(4, mem_birth);
			ps.setString(5, mem_job);
			ps.setString(6, mem_interest);
			cnt = ps.executeUpdate();
			
			if(cnt>0)
				returns = "success";//가입 성공
			else
				returns = "false";//가입 실패

		} catch (Exception e) {
			System.out.println("join 에러 : "+e);
			e.printStackTrace();
		} finally {
			if (ps != null)try {ps.close();} catch (SQLException ex) {}
			if (con != null)try {con.close();} catch (SQLException ex) {}
		}
		return returns;
	}

	//로그인
	public String memberLogin(String mem_id, String mem_passwd) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		String returns = "";
		
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql = "select mem_id, mem_passwd from member where mem_id=? and mem_passwd=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, mem_id);
			ps.setString(2, mem_passwd);
			rs = ps.executeQuery();
			if (rs.next()) {
				System.out.println(rs.getString("mem_id"));
				System.out.println(rs.getString("mem_passwd"));
				if (rs.getString("mem_id").equals(mem_id) && rs.getString("mem_passwd").equals(mem_passwd)) {
					returns = "success";// 로그인 가능
				} else {
					returns = "false"; // 로그인 실패
				}
			} else {
				returns = "noId"; // 아이디 또는 비밀번호 존재 X
			}

		} catch (Exception e) {
			System.out.println("login 에러 : "+e);
			e.printStackTrace();
		} finally {
			if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (ps != null)try {ps.close();} catch (SQLException ex) {}
			if (con != null)try {con.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	//id check
	public String memberSelect(String mem_id) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		String returns = "";
			
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql = "select mem_id from member where mem_id=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, mem_id);
			rs = ps.executeQuery();
			if (rs.next()) {
				returns = "nonono"; // 아이디 중복
			} else {
				returns =  "noId"; // 아이디 중복 x
			}
		} catch (Exception e) {
			System.out.println("id check 에러 : "+e);
			e.printStackTrace();
		} finally {
			if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (ps != null)try {ps.close();} catch (SQLException ex) {}
			if (con != null)try {con.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	//gps 저장
	public String gpsSave(String gps, String mem_id) {
		Connection con = null;
		PreparedStatement ps = null;
		int cnt;
		String sql = "";
		String returns = "";
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql = "insert into gps values(null,?,now(),?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, mem_id);
			ps.setString(2, gps);
			cnt = ps.executeUpdate();			
			if(cnt>0)
				returns = "success";//추가 성공
			else
				returns = "false";//추가 실패

		} catch (Exception e) {
			System.out.println("gps 에러 : "+e);
			e.printStackTrace();
		} finally {
			if (ps != null)try {ps.close();} catch (SQLException ex) {}
			if (con != null)try {con.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	//list 저장
	public String mailSave(String data, String id) {
		Connection con = null;
		PreparedStatement ps = null;
		int cnt;
		String sql = "";
		String returns = "";
		try {
		Class.forName(driver);
			con = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql = "insert into checklist values(null,?,?,now())";
			ps = con.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, data);
			cnt = ps.executeUpdate();			
			if(cnt>0)
				returns = "success";//추가 성공
			else
				returns = "false";//추가 실패
		} catch (Exception e) {
			System.out.println("mailSave 에러 : "+e);
			e.printStackTrace();
		} finally {
			if (ps != null)try {ps.close();} catch (SQLException ex) {}
			if (con != null)try {con.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	//스케줄 등록
	public String scheduleInsert(String mem_id, String sche_num, String sche_contents, String sche_date) {
		Connection con = null;
		PreparedStatement ps = null;
		int cnt;
		String sql = "";
		String returns = "";
		//SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		//Date savedate = sdFormat.parse(date);
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql = "insert into schedule values(null,?,?,?,?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, mem_id);
			ps.setString(2, sche_num);
			ps.setString(3, sche_contents);
			ps.setString(4, sche_date);
			cnt = ps.executeUpdate();
		
			if(cnt>0)
				returns = "success";//추가 성공
			else
				returns = "false";//추가 실패
		} catch (Exception e) {
			System.out.println("schedule 삽입 에러 : "+e);
			e.printStackTrace();
		} finally {
			if (ps != null)try {ps.close();} catch (SQLException ex) {}
			if (con != null)try {con.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	//스케줄 수정
	public String scheduleUpdate(String id, String sche_num, String contents, String date) {
		Connection con = null;
		PreparedStatement ps = null;
		int cnt;
		String sql = "";
		String returns = "";
		//SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		//Date savedate = sdFormat.parse(date);
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql = "update schedule set sche_contents=?, sche_date=?, where mem_id=? and sche_num=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, contents);
			ps.setString(2, date);
			ps.setString(3, id);
			ps.setString(4, sche_num);
			cnt = ps.executeUpdate();
		
			if(cnt>0)
				returns = "success";//수정 성공
			else
				returns = "false";//수정 실패
		} catch (Exception e) {
			System.out.println("schedule 수정 에러 : "+e);
			e.printStackTrace();
		} finally {
			if (ps != null)try {ps.close();} catch (SQLException ex) {}
			if (con != null)try {con.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	//스케줄 삭제
	public String scheduleDelete(String mem_id, String sche_num) {
		Connection con = null;
		PreparedStatement ps = null;
		int cnt;
		String sql = "";
		String returns = "";
		//SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		//Date savedate = sdFormat.parse(date);
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql = "delete from schedule where mem_id=? and sche_num=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, mem_id);
			ps.setString(2, sche_num);
			cnt = ps.executeUpdate();
		
			if(cnt>0)
				returns = "success";//삭제 성공
			else
				returns = "false";//삭제 실패
		} catch (Exception e) {
			System.out.println("schedule 삭제 에러 : "+e);
			e.printStackTrace();
		} finally {
			if (ps != null)try {ps.close();} catch (SQLException ex) {}
			if (con != null)try {con.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	//러닝 결과 전달
	public String learning_Result(String mem_id) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		String returns = "";
		
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql = "select * from learing_result r where result_mem_id=? and result_id=(SELECT MAX(result_id) FROM learing_result WHERE result_mem_id = r.result_mem_id)";
			ps = con.prepareStatement(sql);
			ps.setString(1, mem_id);
			rs = ps.executeQuery();
			if(rs.next()) {
				returns = rs.getString("result_date")+","
						+rs.getString("result_lat")+","
						+rs.getString("result_lng")+","
						+rs.getString("result_day_of_week")+","
						+rs.getString("result_hour");
			} 

		} catch (Exception e) {
			System.out.println("LearningResult 에러 : "+e);
			e.printStackTrace();
		} finally {
			if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (ps != null)try {ps.close();} catch (SQLException ex) {}
			if (con != null)try {con.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	//promotion 서치 결과 전달
	public String promotion_Result(String location) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		String returns = "";
		System.out.println("location: "+location);
		
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql = "select * from promotion where promotion_location like '%"+location+"%'"
					+ " and promotion=(SELECT MAX(promotion) FROM promotion)";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()) {
				returns = rs.getString("promotion_location")+","
						+rs.getString("promotion_location_detail")+","
						+rs.getString("promotion_name")+","
						+rs.getString("promotion_start_day")+","
						+rs.getString("promotion_end_day");
			} 
		} catch (Exception e) {
			System.out.println("promotion_Result 에러 : "+e);
			e.printStackTrace();
		} finally {
			if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (ps != null)try {ps.close();} catch (SQLException ex) {}
			if (con != null)try {con.close();} catch (SQLException ex) {}
		}
		return returns;
	}
}
