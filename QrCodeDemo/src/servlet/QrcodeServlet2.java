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

		// 工厂
		DiskFileItemFactory factory = new DiskFileItemFactory(/*20*1024, new File("F:/f/temp")*/);
		// 解析器
		ServletFileUpload sfu = new ServletFileUpload(factory);
	   //sfu.setFileSizeMax(100 * 1024);//限制单个文件大小为100K
       //sfu.setSizeMax(1024 * 1024);//限制整个表单大小为1M
		//创建MyFile对象
		MyFile myFile=null;
		try {
			List<FileItem> list = sfu.parseRequest(request);
			//封装表单信息
			 List itemList=new ArrayList();
			 //拼凑上传图片路径
			 String path1=null;
			 //遍历表单信息
			for(FileItem item:list){
				//若为纯文本信息时
			      if(item.isFormField()){
			    	  String name=item.getFieldName();
			    	  String value = item.getString("UTF-8");
			    	  itemList.add(value);
			      }
			      //若为上传文件时
			      else{
			    	  //若上传图片为空就跳过
			    	  String filename = item.getName();//获取上传的文件名称
			    	  //若filename为空则没有上传文件
			    	  if(filename!=null&&filename!=""){
			    	   myFile=new MyFile();
						myFile.setFid(CommonUtils.uuid());
						myFile.setFramename(item.getName());
						myFile.setCnt(0);
						//获取当前时间
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
						//保存路径至MyFile中
						myFile.setFilepath(path);
						// 4. 创建目录文件
						File destFile = new File(dirFile, filename);
						//5. 保存
						item.write(destFile);
						//将myFile写入数据库
						myFileService.add(myFile);
			    	  }
			      }
			}
			//输出list
			System.out.println("list:"+itemList);
			//上传图片的物理地址
			String picPath="";
			//若path1为null则picPath就为空字符串
			if(path1!=null)
		       picPath="http://10.1.1.145:8088/QrCodeDemo/files/"+path1;
			//生成vcard的字符串
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
			request.setAttribute("msg", "二维码生成成功");
			request.getRequestDispatcher("/msg1.jsp").forward(request, response);
			///////////////////////////////////////////////////////
			
		} catch (FileUploadException e) {
			if(e instanceof FileUploadBase.FileSizeLimitExceededException) {
				request.setAttribute("msg", "您上传的文件超出了100KB！");
				request.getRequestDispatcher("/vcard.jsp").forward(request, response);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}