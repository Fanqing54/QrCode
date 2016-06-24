package servlet;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import qrcode.TwoDimensionCode;
import cn.itcast.commons.CommonUtils;
import domain.MyFile;
import service.MyFileService;

public class QrcodeServlet2 extends HttpServlet {
   private MyFileService myFileService = new MyFileService();
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		// ����
		DiskFileItemFactory factory = new DiskFileItemFactory(/*20*1024, new File("F:/f/temp")*/);
		// ������
		ServletFileUpload sfu = new ServletFileUpload(factory);
	   //sfu.setFileSizeMax(100 * 1024);//���Ƶ����ļ���СΪ100K
       //sfu.setSizeMax(1024 * 1024);//������������СΪ1M
		//����MyFile����
		MyFile myFile=null;
		try {
			List<FileItem> list = sfu.parseRequest(request);
			//��װ����Ϣ
			 List itemList=new ArrayList();
			 //ƴ���ϴ�ͼƬ·��
			 String path1=null;
			 //��������Ϣ
			for(FileItem item:list){
				//��Ϊ���ı���Ϣʱ
			      if(item.isFormField()){
			    	  String name=item.getFieldName();
			    	  String value = item.getString("UTF-8");
			    	  itemList.add(value);
			      }
			      //��Ϊ�ϴ��ļ�ʱ
			      else{
			    	  //���ϴ�ͼƬΪ�վ�����
			    	  String filename = item.getName();//��ȡ�ϴ����ļ�����
			    	  //��filenameΪ����û���ϴ��ļ�
			    	  if(filename!=null&&filename!=""){
			    	   myFile=new MyFile();
						myFile.setFid(CommonUtils.uuid());
						myFile.setFramename(item.getName());
						myFile.setCnt(0);
						//��ȡ��ǰʱ��
						Date date=new Date();
						DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String time=format.format(date);
						myFile.setUploadtime(time);
						myFile.setFilesize(item.getSize());
						String root = this.getServletContext().getRealPath("/files/");
						int index = filename.lastIndexOf("\\");
						if(index != -1) {
							filename = filename.substring(index+1);
						}
						int hCode = filename.hashCode();
						String hex = Integer.toHexString(hCode);
						path1=hex.charAt(0)+"/"+hex.charAt(1)+"/"+filename;
						File dirFile = new File(root, hex.charAt(0) + "/" + hex.charAt(1));
						String path=dirFile.getAbsolutePath()+"\\"+filename;
						dirFile.mkdirs();
						//����·����MyFile��
						myFile.setFilepath(path);
						// 4. ����Ŀ¼�ļ�
						File destFile = new File(dirFile, filename);
						//5. ����
						item.write(destFile);
						//��myFileд�����ݿ�
						myFileService.add(myFile);
			    	  }
			      }
			}
			//���list
			System.out.println("list:"+itemList);
			//�ϴ�ͼƬ�������ַ
			String picPath="";
			//��path1Ϊnull��picPath��Ϊ���ַ���
			if(path1!=null)
		       picPath="http://10.1.1.145:8088/QrCodeDemo/files/"+path1;
			//����vcard���ַ���
			String content="BEGIN:VCARD\n" +
				    "VERSION:3.0\n" +
				    "N:"+itemList.get(0)+"\n" +
				    "PHOTO;VALUE=URL;TYPE=PNG:"+picPath+"\n"+
				    "EMAIL:"+itemList.get(4)+"\n" +
				    "TEL:"+itemList.get(1)+"\n" +
				    "ADR:"+itemList.get(5)+"\n" +
				    "ORG:" 
				    +itemList.get(2)+"\n" +
				    "TITLE:"+itemList.get(3)+"\n" +
				    "URL:http://www.ipaiban.com\n" +
				    "NOTE:\n" +
				    "END:VCARD";
			double id=TwoDimensionCode.generate(content);
			request.setAttribute("id", id);
			request.setAttribute("msg", "��ά�����ɳɹ�");
			request.getRequestDispatcher("/msg1.jsp").forward(request, response);
			///////////////////////////////////////////////////////
			
		} catch (FileUploadException e) {
			if(e instanceof FileUploadBase.FileSizeLimitExceededException) {
				request.setAttribute("msg", "���ϴ����ļ�������100KB��");
				request.getRequestDispatcher("/vcard.jsp").forward(request, response);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}