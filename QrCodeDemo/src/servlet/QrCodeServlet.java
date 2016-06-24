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
		// ����
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// ������
		ServletFileUpload sfu = new ServletFileUpload(factory);
		//sfu.setFileSizeMax(100 * 1024);//���Ƶ����ļ���СΪ100K
	   // sfu.setSizeMax(1024 * 1024);//������������СΪ1M
		// ����MyFile����
		MyFile myFile = null;

		// �������õ�List
		try {
			List<FileItem> list = sfu.parseRequest(request);
			FileItem fi = list.get(0);
			// �ж��ϴ��ļ��Ƿ�Ϊ�գ����ж��ļ��������ֵķ���
			String filename = fi.getName();// ��ȡ�ϴ����ļ�����
			if (filename != null && filename != "") {
				myFile = new MyFile();
				myFile.setFid(CommonUtils.uuid());
				myFile.setFramename(fi.getName());
				myFile.setCnt(0);
				// ��ȡ��ǰʱ��
				Date date = new Date();
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = format.format(date);
				myFile.setUploadtime(time);
				myFile.setFilesize(fi.getSize());
				//1. �õ��ļ������·��
				String root = this.getServletContext().getRealPath("/files/");
				/* 2. ���ɶ���Ŀ¼ 1). �õ��ļ����� 2). �õ�hashCode 3). ת����16���� 4).
				 *  ��ȡǰ�����ַ���������Ŀ¼*/
				 //�����ļ����ľ���·������
				int index = filename.lastIndexOf("\\");
				if (index != -1) {
					filename = filename.substring(index + 1);
				}
				//1. �õ�hashCode
				int hCode = filename.hashCode();
				String hex = Integer.toHexString(hCode);
				// ƴ��·��
				String path1 = hex.charAt(0) + "/" + hex.charAt(1) + "/"
						+ filename;
				// 2. ��ȡhex��ǰ������ĸ����root������һ������һ��������·��
				File dirFile = new File(root, hex.charAt(0) + "/"
						+ hex.charAt(1));
				String path = dirFile.getAbsolutePath() + "\\" + filename;
				// 3. ����Ŀ¼��
				dirFile.mkdirs();
				// ����·����MyFile��
				myFile.setFilepath(path);
				// 4. ����Ŀ¼�ļ�
				File destFile = new File(dirFile, filename);
                // 5. ����
				fi.write(destFile);
				// ��myFileд�����ݿ�
				myFileService.add(myFile);
				// ����ļ���url�ַ���
				String content = "http://10.1.1.145:8088/QrCodeDemo/files/"
						+ path1;
				double id = TwoDimensionCode.generate(content);
				request.setAttribute("id", id);
				request.setAttribute("msg", "��ά�����ɳɹ�");
				request.getRequestDispatcher("/msg1.jsp").forward(request,
						response);
			}
			// û���ļ�ʱ
			else {
				System.out.println("û���ϴ��ļ�");
				request.setAttribute("msg", "��ѡ��һ���ļ�");
				request.getRequestDispatcher("/qrcode.jsp").forward(request,
						response);
				// /////////////////////////////////////////////////////
			}
		} catch (FileUploadException e) {
			//if (e instanceof FileUploadBase.FileSizeLimitExceededException) {
				System.out.println("���ϴ����ļ�������100KB��");
				request.setAttribute("msg", "���ϴ����ļ�������100KB��");
				request.getRequestDispatcher("/qrcode.jsp").forward(request,
						response);
			//}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
