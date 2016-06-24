package dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import domain.MyFile;
import cn.itcast.jdbc.TxQueryRunner;

public class MyFileDao {
	private QueryRunner qr = new TxQueryRunner();
	public void add(MyFile myFile) {
		try {
			String sql = "insert into tb_myfile2 values(?,?,?,?,?,?)";
			Object[] params = { myFile.getFid(), myFile.getFilepath(), myFile.getFramename(),
					myFile.getUploadtime(), myFile.getCnt(), myFile.getFilesize()};
			qr.update(sql, params);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public List<MyFile> findAll() {
		try {
			String sql = "select * from tb_myfile2";
			return qr.query(sql, new BeanListHandler<MyFile>(MyFile.class));
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public void delete(String fid) {
		try {
			String sql = "delete from tb_myfile2 where fid=?";
		    qr.update(sql,fid);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public MyFile load(String fid) {
		try {
			String sql = "select * from tb_myfile2 where fid=?";
			return qr.query(sql, new BeanHandler<MyFile>(MyFile.class), fid);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public void edit(MyFile myFile) {
		try {
			String sql = "update tb_myfile2 set filepath=?,framename=?,uploadtime=?," +
					"cnt=?,filesize=? where fid=?";
			Object[] params = {myFile.getFilepath(), myFile.getFramename(),
					myFile.getUploadtime(), myFile.getCnt(), myFile.getFilesize(),
					myFile.getFid()};
			qr.update(sql, params);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
