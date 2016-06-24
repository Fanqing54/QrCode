package service;

import java.util.List;

import dao.MyFileDao;
import domain.MyFile;

public class MyFileService {
	private MyFileDao myFileDao = new MyFileDao();
	
	/**
	 * 添加文件
	 * @param myFile
	 */
	public void add(MyFile myFile) {
		myFileDao.add(myFile);
	}
	
	/**
	 * 查询所有
	 * @return
	 */
	public List<MyFile> findAll() {
		return myFileDao.findAll();
	}
	//删除一个myfile
	public void delete(String fid) {
		myFileDao.delete(fid);
	}
	
	/**
	 * 下载文件
	 * @param fid
	 * @return
	 */
	public MyFile download(String fid) {
		MyFile myFile = myFileDao.load(fid);
		myFile.setCnt(myFile.getCnt()+1);
		myFileDao.edit(myFile);
		return myFile;
	}
}
