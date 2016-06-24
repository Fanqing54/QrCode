package service;

import java.util.List;

import dao.MyFileDao;
import domain.MyFile;

public class MyFileService {
	private MyFileDao myFileDao = new MyFileDao();
	
	/**
	 * ����ļ�
	 * @param myFile
	 */
	public void add(MyFile myFile) {
		myFileDao.add(myFile);
	}
	
	/**
	 * ��ѯ����
	 * @return
	 */
	public List<MyFile> findAll() {
		return myFileDao.findAll();
	}
	//ɾ��һ��myfile
	public void delete(String fid) {
		myFileDao.delete(fid);
	}
	
	/**
	 * �����ļ�
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
