package models.utils;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import play.i18n.Messages;

import javax.servlet.ServletException;
import java.io.*;

/**
 * 合同生成
 */
public class WordController{

    //执行方法
    public static void word(String html,String contractno_buyer) throws ServletException, IOException {
        InputStream is = new ByteArrayInputStream(html.getBytes("UTF-8"));
     //   OutputStream os = new FileOutputStream("F:\\购销合同103.doc");
      OutputStream os = new FileOutputStream("/home/backManage/contract/buyer/"+contractno_buyer+Messages.get("SUFFIX_WORD"));
        inputStreamToWord(is, os);
    }

    /**
     * 把is写入到对应的word输出流os中
     * 不考虑异常的捕获，直接抛出
     *
     * @param is
     * @param os
     * @throws IOException
     */
    private static void inputStreamToWord(InputStream is, OutputStream os) throws IOException {
        POIFSFileSystem fs = new POIFSFileSystem();
        //对应于org.apache.poi.hdf.extractor.WordDocument
        fs.createDocument(is, "WordDocument");
        fs.writeFilesystem(os);
        os.close();
        is.close();
    }

    /**
     * 把输入流里面的内容以UTF-8编码当文本取出。
     * 不考虑异常，直接抛出
     *
     * @param ises
     * @return
     * @throws IOException
     */
    private static String getContent(InputStream... ises) throws IOException {
        if (ises != null) {
            StringBuilder result = new StringBuilder();
            BufferedReader br;
            String line;
            for (InputStream is : ises) {
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
            }
            return result.toString();
        }
        return null;
    }

}
