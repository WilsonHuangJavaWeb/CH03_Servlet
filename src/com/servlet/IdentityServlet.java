package com.servlet;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ki264 on 2017/4/20.
 */
@WebServlet(name = "IdentityServlet")
public class IdentityServlet extends HttpServlet {

    //隨機字元字典，不包括 0、O、1、I 等等難辨認的字元
    private static final char[] CHARS = {'2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public static Random random = new Random();//亂數

    /**
     * 獲得六位亂數
     *
     * @return
     */
    public static String getRandomString() {
        StringBuffer buffer = new StringBuffer();   //字元快取記憶體
        for (int i = 1; i < 6; i++) {               //迴圈六次
            buffer.append(CHARS[random.nextInt(CHARS.length)]); //每次取一個隨機字元
        }
        return buffer.toString();
    }

    /**
     * 獲得隨機的顏色
     *
     * @return
     */
    public static Color getRandomColor() {
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));

    }

    /**
     * 傳回某顏色的反色
     *
     * @param c
     * @return
     */
    public static Color getReverseColor(Color c) {
        return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("image/jpeg");//設定輸出類型(必須)
        String randomString = getRandomString();//隨機字串
        request.getSession(true).setAttribute("randomString", randomString);//放入session中

        int width = 100;//圖片的寬
        int height = 30;//圖片的高

        Color color = getRandomColor();//隨機顏色，用於背景色
        Color reverse = getReverseColor(color);//反色，用於前景色

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);//建立一個彩色圖片
        Graphics2D graphics2D = bufferedImage.createGraphics();//獲得繪圖物件
        graphics2D.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));//設定字體
        graphics2D.setColor(color);//設定顏色
        graphics2D.fillRect(0, 0, width, height);//設定背景
        graphics2D.setColor(reverse);//設定顏色
        graphics2D.drawString(randomString, 18, 20);//繪製隨機字元
        for (int i = 0, n = random.nextInt(100); i < n; i++) {//話最多100個噪音點
            graphics2D.drawRect(random.nextInt(width), random.nextInt(height), 1, 1);//隨機噪音點
        }

        ServletOutputStream out = response.getOutputStream();//轉成JPEG格式
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);//編碼器

        encoder.encode(bufferedImage);//對圖片進行編碼
        out.flush();//輸出到用戶端


    }
}
