package qrcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class TwoDimensionCode {
	public static double generate(String content) {
		double userId = 0;
		try {
			//String content1 = "http://10.1.1.145:8088/day22_5/files/" + content;
			//System.out.println(content1);
			String path = "E:\\apache-tomcat-7.0.64\\webapps\\QrCodeDemo\\qrcodePic";
			MultiFormatWriter multiFormatWrite = new MultiFormatWriter();
			Map hints = new HashMap();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			//��ά���������
			System.out.println("��ά��������ݣ�"+content);
			// ����ָ���Ŀ�ȣ��߶Ⱥ͸��Ӳ������ַ������б���
			// ���ɶ�ά��
			BitMatrix bitMatrix = multiFormatWrite.encode(content,
					BarcodeFormat.QR_CODE, 400, 400, hints);
			userId = (Math.random() * 10);
			System.out.println(userId);
			File file1 = new File(path, userId + ".jpg");
			// д���ļ�
			MartixToImageWriter.writeToFile(bitMatrix, "jpg", file1);
			System.out.println("��ά��ͼƬ���ɳɹ���");
			// return userId;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userId;
	}
}
