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

public class QrCodeServlet extends HttpServlet {
	private MyFileService myFileService = new MyFileService();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		// 工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 解析器
		ServletFileUpload sfu = new ServletFileUpload(factory);
		//sfu.setFileSizeMax(100 * 1024);//限制单个文件大小为100K
	   // sfu.setSizeMax(1024 * 1024);//限制整个表单大小为1M
		// 创建MyFile对象
		MyFile myFile = null;

		// 解析，得到List
		try {
			List<FileItem> list = sfu.parseRequest(request);
			FileItem fi = list.get(0);
			// 判断上传文件是否为空，用判断文件有无名字的方法
			String filename = fi.getName();// 获取上传的文件名称
			if (filename != null && filename != "") {
				myFile = new MyFile();
				myFile.setFid(CommonUtils.uuid());
				myFile.setFramename(fi.getName());
				myFile.setCnt(0);
				// 获取当前时间
				Date date = new Date();
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = format.format(date);
				myFile.setUploadtime(time);
				myFile.setFilesize(fi.getSize());
				//1. 得到文件保存的路径
				String root = this.getServletContext().getRealPath("/files/");
				/* 2. 生成二层目录 1). 得到文件名称 2). 得到hashCode 3). 转发成16进制 4).
				 *  获取前二个字符用来生成目录*/
				 //处理文件名的绝对路径问题
				int index = filename.lastIndexOf("\\");
				if (index != -1) {
					filename = filename.substring(index + 1);
				}
				//1. 得到hashCode
				int hCode = filename.hashCode();
				String hex = Integer.toHexString(hCode);
				// 拼凑路径
				String path1 = hex.charAt(0) + "/" + hex.charAt(1) + "/"
						+ filename;
				// 2. 获取hex的前两个字母，与root连接在一起，生成一个完整的路径
				File dirFile = new File(root, hex.charAt(0) + "/"
						+ hex.charAt(1));
				String path = dirFile.getAbsolutePath() + "\\" + filename;
				// 3. 创建目录链
				dirFile.mkdirs();
				// 保存路径至MyFile中
				myFile.setFilepath(path);
				// 4. 创建目录文件
				File destFile = new File(dirFile, filename);
                // 5. 保存
				fi.write(destFile);
				// 将myFile写入数据库
				myFileService.add(myFile);
				// 获得文件的url字符串
				String content = "http://10.1.1.145:8088/QrCodeDemo/files/"
						+ path1;
				double id = TwoDimensionCode.generate(content);
				request.setAttribute("id", id);
				request.setAttribute("msg", "二维码生成成功");
				request.getRequestDispatcher("/msg1.jsp").forward(request,
						response);
			}
			// 没传文件时
			else {
				System.out.println("没有上传文件");
				request.setAttribute("msg", "请选择一个文件");
				request.getRequestDispatcher("/qrcode.jsp").forward(request,
						response);
				// /////////////////////////////////////////////////////
			}
		} catch (FileUploadException e) {
			//if (e instanceof FileUploadBase.FileSizeLimitExceededException) {
				System.out.println("您上传的文件超出了100KB！");
				request.setAttribute("msg", "您上传的文件超出了100KB！");
				request.getRequestDispatcher("/qrcode.jsp").forward(request,
						response);
			//}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
