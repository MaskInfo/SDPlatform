package cn.org.upthink.util;

import cn.org.upthink.persistence.mybatis.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * Created by rover on 2017/12/9.
 */
public class QiniuVideoUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(QiniuVideoUtil.class);

    private static String ACCESS_KEY = "WJBKijRUR4rk0W8dHCS2M0g6YdZQ-FSBEvN20OGT";
    private static String SECRET_KEY = "1GNzY3ToCzuNsWDBVnYj8HRvfYhRJ1EmJqce-PDv";
    public static String UP_HOST = "http://video.enernet.mobi/";
    //空间名
    private static String BUCKET = "upthink-video";
    private static Auth auth = null;

    private static UploadManager uploadManager = null;

    static {
//        ACCESS_KEY = Global.getQiniuAccessKey();
//        SECRET_KEY = Global.getQiniuSecretKey();
//        UP_HOST = Global.getQiniuUpHost();
//        BUCKET = Global.getQiniuBucket();
        auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        //华东:zone0, 华北zone1, 华南:zone2;
        init();
    }

    private static void init(){
        if(uploadManager==null){
            Zone z = Zone.autoZone();
            Configuration c = new Configuration(z);
            //创建上传对象
            uploadManager = new UploadManager(c);
            LOGGER.info("###### qiniu初始化完毕,创建上传对象. ######");
        }
    }

    private static String getUpToken() {
        //init();
        return auth.uploadToken(BUCKET);
    }

    private static String getUpToken(String bucketname) {
        if(bucketname==null || "".equals(bucketname)){
            return getUpToken();
        }
        BUCKET = bucketname;
        //init();
        return auth.uploadToken(BUCKET);
    }

    /**
     * 上传1
     * @param bucketname
     * @param key
     * @param file
     * @return
     */
    public static boolean upload(String bucketname, String key, File file) {
        try {
            //调用put方法上传
            Response res = uploadManager.put(file, key, getUpToken(bucketname));
            //打印返回的信息
            LOGGER.info("qiniu.upload.res"+res.bodyString());
            return res.isOK();
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            LOGGER.info(r.toString());
            try {
                //响应的文本信息
                LOGGER.info(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
        return false;
    }

    /**
     * 上传2
     * @param bucketname
     * @param key
     * @param inputStream
     * @return
     */
    public static boolean upload(String bucketname, String key, InputStream inputStream) {
        try {
            //调用put方法上传
            Response res = uploadManager.put(inputStream, key, getUpToken(bucketname), null, null);
            //打印返回的信息
            LOGGER.info("qiniu.upload.res"+res.bodyString());
            return res.isOK();
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            LOGGER.info(r.toString());
            try {
                //响应的文本信息
                LOGGER.info(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
        return false;
    }

    /**
     * 删除
     * @param bucketname
     * @param key
     * @return
     */
    public static boolean delete(String bucketname, String key){
        try {
            if(bucketname == null || "".equals(bucketname)){
                bucketname = BUCKET;
            }
            Zone z = Zone.autoZone();
            Configuration c = new Configuration(z);
            BucketManager bucketManager = new BucketManager(auth, c);
            Response res = bucketManager.delete(bucketname, key);
            LOGGER.info("qiniu.delete.res="+res.isOK());
            return res.isOK();
        }catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            LOGGER.info("qiniu.delete.res.error="+r.toString());
            try {
                //响应的文本信息
                LOGGER.info(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
        return false;
    }

    /**
     * 重命名
     * @param bucketname
     * @param key
     * @param newKey
     * @return
     */
    public static boolean rename(String bucketname, String key, String newKey){
        try {
            if(bucketname == null || "".equals(bucketname)){
                bucketname = BUCKET;
            }
            Zone z = Zone.autoZone();
            Configuration c = new Configuration(z);
            BucketManager bucketManager = new BucketManager(auth, c);
            Response res = bucketManager.rename(bucketname, key, newKey);
            LOGGER.info("qiniu.rename.res="+res.isOK());
            return res.isOK();
        }catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            LOGGER.info("qiniu.rename.res.error="+r.toString());
            try {
                //响应的文本信息
                LOGGER.info(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
        return false;
    }

    /**
     * 获取前缀为图片的文件
     * @param marker
     * @param limit
     * @return
     */
    public static JSONObject getImageFileList(String marker, Integer limit){
        return getFileList("static/image", marker, limit);
    }

    /**
     * 获取前缀为视频,音频的文件
     * @param marker
     * @param limit
     * @return
     */
    public static JSONObject getMediaFileList(String marker, Integer limit){
        return getFileList("static/media", marker, limit);
    }

    /**
     * 获取前缀为音频的文件
     * @param marker
     * @param limit
     * @return
     */
    public static JSONObject getVideoFileList(String marker, Integer limit){
        return getFileList("static/video", marker, limit);
    }

    /**
     * 获取前缀为视频的文件
     * @param marker
     * @param limit
     * @return
     */
    public static JSONObject getAudioFileList(String marker, Integer limit){
        return getFileList("static/audio", marker, limit);
    }

    public static JSONObject getFileList(String prefix, String marker, Integer limit){
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);
        BucketManager bucketManager = new BucketManager(auth, c);
        JSONObject json = new JSONObject();
        try {
            if(limit==null || limit<0){ limit = 10; }
            //调用listFiles方法列举指定空间的指定文件
            //参数一：bucket    空间名
            //参数二：prefix    文件名前缀
            //参数三：marker    上一次获取文件列表时返回的 marker
            //参数四：limit     每次迭代的长度限制，最大1000，推荐值 100
            //参数五：delimiter 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
            FileListing fileListing = bucketManager.listFiles(BUCKET, prefix, marker, limit, null);
            String markerNext = fileListing.marker;
            LOGGER.info("markerNext="+markerNext);
//            json.put("domain", Global.getQiniuDomain());
            json.put("nextMarker", markerNext==null?"":markerNext);
            FileInfo[] items = fileListing.items;
            JSONArray jsonArray = new JSONArray();
            if(items.length>0){
                for (FileInfo fileInfo : items) {
                    //System.out.println(fileInfo.key);
                    JSONObject jsonObject = (JSONObject) JSON.toJSON(fileInfo);
                    jsonArray.add(jsonObject);
                }
            }
            json.put("fileList", jsonArray);
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            LOGGER.info(r.toString());
        }
        return json;
    }

    public static void mainXX(String args[]) {
		//设置需要操作的账号的AK和SK

//		Zone z = Zone.autoZone();
//		Configuration c = new Configuration(z);
//
//		//实例化一个BucketManager对象
//		BucketManager bucketManager = new BucketManager(auth, c);
//
//		try {
//			//调用listFiles方法列举指定空间的指定文件
//			//参数一：bucket    空间名
//			//参数二：prefix    文件名前缀
//			//参数三：marker    上一次获取文件列表时返回的 marker
//			//参数四：limit     每次迭代的长度限制，最大1000，推荐值 100
//			//参数五：delimiter 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
//            String marker = null;
//			FileListing fileListing = bucketManager.listFiles(BUCKET, null, marker, 30, null);
//            String markerNext = fileListing.marker;
//            LOGGER.info("markerNext="+markerNext);
//			FileInfo[] items = fileListing.items;
//			for (FileInfo fileInfo : items) {
//				System.out.println(fileInfo.key);
//			}
//		} catch (QiniuException e) {
//			//捕获异常信息
//			Response r = e.response;
//            LOGGER.info(r.toString());
//		}
        JSONObject json = getFileList(null, null, 30);
        System.out.println("json="+json.toString());
        JSONArray jsonArray = json.getJSONArray("fileList");
        for(int i=0;i<jsonArray.size();i++){
            JSONObject jsonFile = jsonArray.getJSONObject(i);
            String key = jsonFile.getString("key");
            System.out.println("key="+key);
            if(key.contains("static/video")){
                //rename(null, key, key.replaceFirst("static/video", "static/audio"));
            }
            //rename(null, key, key.replaceFirst("static", "static/image"));
        }
	}

   /* public static void main(String[] args){
		String fileName = StringUtils.getRandomStr(12);
		String uuid = UUID.randomUUID().toString().replaceAll("\\-", "");
		String hashPath = HashPathUtil.getHashPath("image", uuid);
		File file = new File("H:\\demo5.jpg");
        //String hashPath = HashPathUtil.getHashPath("media", uuid);
        //File file = new File("/Users/rover/Downloads/TentCoo/SB_省博物馆微信项目/getvoice1.mp3");
		String suffix = "";
		try {
			String file_name = file.getCanonicalPath();
			int point = file_name.lastIndexOf(".");
			int length = file_name.length();
			suffix = file_name.substring(point, length);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			boolean res = upload(null, hashPath+fileName + suffix, file);
            System.out.println("******res="+res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//String webPath = Global.getQiniuDomain() + hashPath + fileName + suffix;
        //System.out.println("webPath="+webPath);
	}*/

    public static void mainX3(String[] args){
        try {
            boolean res = delete(null, "static/2a/08/32/c4/0Kw0WK2mBtmC.png");
            LOGGER.info("******res="+res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String upLoadToQiNiu(HttpServletRequest request, MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isNotBlank(originalFilename)) {
                String uuid = UUID.randomUUID().toString().replaceAll("\\-", "");
                //新的文件名字
                String fileName = uuid + originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
                //路径   static/images/
                String fileType = originalFilename.substring(originalFilename.lastIndexOf(".")+1, originalFilename.length());
                String hashPath = "static/images/";
                if(!"PNG".equalsIgnoreCase(fileType) && !"jpg".equalsIgnoreCase(fileType) && !"jpeg".equalsIgnoreCase(fileType) && !"gif".equalsIgnoreCase(fileType) && !"bmp".equalsIgnoreCase(fileType)) {
                    hashPath = "static/videoAndaudio/";
                }
                if("pdf".equalsIgnoreCase(fileType)) {
                    hashPath = "static/pdf/";
                }
                //获取测试服务器的路径
                String filePath = request.getSession().getServletContext().getRealPath(hashPath);
                File f = new File(filePath);
                //判断是否存在，不存在就创建
                if (!f.exists()) {
                    f.mkdirs();
                }
                File localFile = new File(f,fileName);
                //转存文件
                file.transferTo(localFile);
                //将图片上传到七牛
                String key = hashPath+fileName;
                boolean upload = QiniuVideoUtil.upload(BUCKET, key, localFile);
                //查看是否上传成功
                System.out.println("upload是否成功：=================>>>" + upload);

                //添加七牛的域名
//				resource.setUri(PropertiesUtil.DOMAIN + hashPath+fileName);
//				resource.setFileName(originalFilename);
//				resource.setLength(file.getSize());
                return UP_HOST + hashPath+fileName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 上传文件到七牛
     * @param request
     * @return 图片路径
     */
    public static String upLoadToQiNiu(MultipartHttpServletRequest request) {
        try {
            Map<String, MultipartFile> files = ((MultipartHttpServletRequest) request).getFileMap();
            if(files.isEmpty()){
                return "";
            }
            MultipartFile file = files.get("file");
            String originalFilename = file.getOriginalFilename();
            int dot = originalFilename.lastIndexOf('.');
            String suffix = "";
            if (dot > 0) {
                // 后缀名
                suffix = originalFilename.substring(dot).toLowerCase();
            }
            //文件类型
            String[] fileTypeArr = new String[]{".jpg",".jpeg",".bmp",".gif",".png"};
            List<String> fileTypes = Arrays.asList(fileTypeArr);
            String prefix = "images";
            if (!fileTypes.contains(suffix)) {
                prefix = "files";
            }
            InputStream inputStream = file.getInputStream();
            //新的文件名字
            String uuid = UUID.randomUUID().toString().replaceAll("\\-", "");
            String hashPath = HashPathUtil.getHashPath(prefix, uuid);
            String fileName = uuid + suffix;
            //将图片上传到七牛
            String key = hashPath + fileName;
            boolean upload = QiniuVideoUtil.upload(BUCKET, key, inputStream);
            //查看是否上传成功
            LOGGER.info("upload是否成功：=================>>>" + upload);
            //添加七牛的域名
            return UP_HOST + key;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 上传多个文件到七牛
     * @param request
     * @return 图片路径
     */
    public static Map upLoadMultiFileToQiNiu(MultipartHttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            Map<String, MultipartFile> files = ((MultipartHttpServletRequest) request).getFileMap();
            if(files.isEmpty()){
                return null;
            }
            for (Map.Entry<String, MultipartFile> entry : files.entrySet()) {
                String fileKey = entry.getKey();
                MultipartFile file = entry.getValue();
                String originalFilename = file.getOriginalFilename();
                int dot = originalFilename.lastIndexOf('.');
                String suffix = "";
                if (dot > 0) {
                    // 后缀名
                    suffix = originalFilename.substring(dot).toLowerCase();
                }
                //文件类型
                String[] fileTypeArr = new String[]{".jpg",".jpeg",".bmp",".gif",".png"};
                List<String> fileTypes = Arrays.asList(fileTypeArr);
                String prefix = "images";
                if (!fileTypes.contains(suffix)) {
                    prefix = "files";
                }
                InputStream inputStream = file.getInputStream();
                //新的文件名字
                String uuid = UUID.randomUUID().toString().replaceAll("\\-", "");
                String hashPath = HashPathUtil.getHashPath(prefix, uuid);
                String fileName = uuid + suffix;
                //将图片上传到七牛
                String key = hashPath + fileName;
                boolean upload = QiniuVideoUtil.upload(BUCKET, key, inputStream);
                //查看是否上传成功
                LOGGER.info("upload是否成功：=================>>>" + upload);
                //添加七牛的域名
                map.put(fileKey, UP_HOST + key);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        File f = new File("/Users/meishukai/gitProject/SDPlatform/electric-sale-ms/src/main/resources/logback.xml");
        upload(BUCKET,
                "111111",f);
    }

}
