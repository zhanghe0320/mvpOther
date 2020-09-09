package com.mvp.serialPort;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *串口工具
 * */
//保存一些基础功能和算法
public class ConstantValueSerialPort {
    public static final int ESendSuccess = 1;       //success
    public static final int ESendTimeOut = 2;       //time out
    public static final int ESendFail   = 3;        //other error
    public static final int ESendRepeat = 4;
    public static final int ESent = 1;
    public static final int EUnSent = 0;
    public enum KeyID{E_STATE,E_BT_NO,E_IP,E_PORT,eComeNo};



    public static Message uploadFile(Context mIn, String oldName , String newName, String urlIn) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        //newName += ".txt";
        Message msg = new Message();
        String uploadFile = oldName;
        String actionUrl = "http://" + urlIn + "/TempHumSystem/file";
        //String actionUrl = "http://" + urlIn + "/dataUpload/phone";
        //String actionUrl = "http://www.nj-lsj.com/dataUpload/phone";
        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

      /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(5000);
      /* 设置传送的method=POST */
            con.setRequestMethod("POST");
      /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
      /* 设置DataOutputStream */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "
                    + "name=\"file1\";filename=\"" + newName + "\"" + end);
            ds.writeBytes(end);
      /* 取得文件的FileInputStream */
            FileInputStream fStream = mIn.openFileInput(uploadFile);
      /* 设置每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
      /* 从文件读取数据至缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
        /* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
      /* close streams */
            fStream.close();
            ds.flush();
      /* 取得Response内容 */
            InputStream is = con.getInputStream();
            //int ch;
            //StringBuffer b = new StringBuffer();
            byte[] buffers = new byte[1024];
            int bytes;
            String receiveBuffer = "";
            while ((bytes = is.read(buffers)) != -1) {
                String strReCode = new String(buffers,0,bytes);
                receiveBuffer += strReCode;
            }
            //Toast.makeText( mIn,"上传成功",Toast.LENGTH_SHORT).show();
            ds.close();
            msg.what = ESendSuccess;
            msg.obj = receiveBuffer;
            return msg;
      /* 将Response显示于Dialog */

      /* 关闭DataOutputStream */

        }catch (MalformedURLException e) {
            msg.what = ESendTimeOut;
            msg.obj = "连接超时，请检查网络设置";
            return msg;
        }catch (Exception e) {
            //Toast.makeText( mIn,"上传失败",Toast.LENGTH_SHORT).show();\
            msg.what = ESendFail;
            String strout = "上传失败，请检查上传链接设置";
            msg.obj = strout;
            return msg;
        }

    }
    //获取系统时间
//    public static String getcurrentTime() {
//        return  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format( new Date(System.currentTimeMillis()));
//    }
//    public static String getcurrentTimeNo() {
//        return  new SimpleDateFormat("yyyyMMddHHmmss").format( new Date(System.currentTimeMillis()));
//    }


    //清除文件内容
    public static void cleanFileData(String fileName, Context mContext) {
        try {
            FileOutputStream fout = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //跟在文件内容的结尾续写内容到文件
    public static void writeFileDataAppend(String fileName, byte[] bytesIn, Context mContext) {
        try {
            FileOutputStream fout = mContext.openFileOutput(fileName, Context.MODE_APPEND);
            fout.write(bytesIn);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void MoveFile(String oldfileName, String newfileName, Context mContext){
        try {
            FileInputStream fin = mContext.openFileInput(oldfileName);
            int fileLength = fin.available();
            if (fileLength <= 0) return ;
            byte[] byteout = new byte[1024];

            while (true){
                int filesize = fin.read(byteout);
                if (filesize <=0) break;
                byte[] bytes1 = new byte[filesize];
                for (int i = 0;i < filesize;i++){
                    bytes1[i] = byteout[i];
                }
                FileOutputStream fout = mContext.openFileOutput(newfileName, Context.MODE_APPEND);
                fout.write(bytes1);
                fout.close();

            }
            fin.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static int ReadSharedPreferencesInt(KeyID KeyIn,Context mContext) {
        SharedPreferences user = mContext.getSharedPreferences("user_info", 0);
        switch (KeyIn) {
            case E_STATE:
                return user.getInt("UPLOAD_STATE", EUnSent);

            default:
                return  0;
        }

    }
    public static String ReadSharedPreferencesString(KeyID KeyIn, Context mContext) {
        switch (KeyIn) {
            case E_BT_NO:
                return mContext.getSharedPreferences("user_info", 0).getString("BT_NO", "");
            case E_IP:
                return mContext.getSharedPreferences("user_info", 0).getString("IP", "127.0.0.1");
            case E_PORT:
                return mContext.getSharedPreferences("user_info", 0).getString("PORT", "8080");
            case eComeNo:
                return mContext.getSharedPreferences("user_info", 0).getString("COM_NO", "10000000001");
            default:
                return  "";
        }

    }


    public static void WriteSharedPreferences(KeyID KeyIn,int IntValue,Context mContext) {
        SharedPreferences mySharedPreferences = mContext.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        switch (KeyIn) {
            case E_STATE:
                editor.putInt("UPLOAD_STATE", IntValue);
                break;
            default:
                break;
        }
        editor.apply();
    }
    public static void WriteSharedPreferences(KeyID KeyIn, String InValue, Context mContext) {
        SharedPreferences mySharedPreferences = mContext.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        switch (KeyIn) {
            case E_BT_NO:
                editor.putString("BT_NO", InValue);
                break;
            case E_IP:
                editor.putString("IP",InValue);
                break;
            case E_PORT:
                editor.putString("PORT",InValue);
                break;
            case eComeNo:
                editor.putString("COM_NO",InValue);
                break;
            default:
                break;
        }
        editor.apply();
    }
    public static String GetDeviveState(Boolean[]  stateIn){
        if (stateIn.length == 0) return "00";

        String strOut = "";
        int[] byteOut = new int[(stateIn.length-1)/8 + 1];
        for (int i = 0;i < byteOut.length; i++){
            byteOut[i] = 0x00;
        }
        int icnt = 0;
        for (Boolean state:stateIn){
            if (state == true){
                byteOut[icnt/8] += 1 << (icnt - (icnt/8)*8);
            }
            icnt++;
        }
        for (int iout:byteOut){
            String Hex = Integer.toHexString(iout & 0xFF);
            if (Hex.length()==1)
            {
                Hex = '0' + Hex;
            }
            strOut = Hex.toUpperCase() + strOut;
        }

        return strOut;
    }
    //获取文件内容长度
    public static int GetFileLength(String fileName, Context mContext) {
        int fileLength = 0;
        try {
            FileInputStream fin = mContext.openFileInput(fileName);
            fileLength = fin.available();
            fin.close();
            return fileLength;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  -1;
    }
    public static String GetFileContent(String fileName, Context mContext){
        int fileLength = 0;
        String strOut = "";
        try {
            FileInputStream fin = mContext.openFileInput(fileName);
            fileLength = fin.available();
            if (fileLength <= 0) return "";
            byte[] byteout = new byte[1024];

            while (true){
                int filesize = fin.read(byteout);
                if (filesize <=0) break;
                byte[] bytes1 = new byte[filesize];
                for (int i = 0;i < filesize;i++){
                    bytes1[i] = byteout[i];
                }
                strOut += new String(bytes1);
            }
            return strOut;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 字节数组转换为十六进制字符串
     *
     * @param b
     *            byte[] 需要转换的字节数组
     * @return String 十六进制字符串
     */
    public static final String byte2hex(byte b[], int size) {
        if (b == null) {
            throw new IllegalArgumentException(
                    "Argument b ( byte array ) is null! ");
        }
        String hs = "";
        String stmp = "";
        for (int n = 0; n < size; n++) {
            stmp = Integer.toHexString(b[n] & 0xff);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
    //设备号发送
    private static byte[] GetDeviceNobyte(byte[] bytes) {
        byte[] byteout = new byte[4];
        //55  消息头     1010指令    00 长度       00 00 01 00 设备号发送     01 02 00 00设备号接收
        if (bytes.length != 11) {
            byteout[0]=0x00;
            byteout[1]=0x00;
            byteout[2]=0x00;
            byteout[3]=0x00;
        }else {
            byteout[0] = bytes[3];
            byteout[1] = bytes[4];
            byteout[2] = bytes[4];
            byteout[3] = bytes[5];
        }
        return  byteout;
    }
    public static byte[] GetBytes(byte [] bytes,int start,int size){

        byte [] bytes1 = new byte[size];//头部
        GetBytes(bytes1,start,size);
        //55 1234 44 123456789012 098765432112
        byte []   bytes2 = new byte[size];//信息
        GetBytes(bytes1,0,2);
        byte []   bytes3 = new byte[size];//长度
        GetBytes(bytes1,0,2);
        byte []   bytes4 = new byte[size];//地址1
        GetBytes(bytes1,0,2);
        byte []   bytes5 = new byte[size];//地址2
        GetBytes(bytes1,0,2);
        return  bytes1;
    }
    //传感器号
    private static byte GetSensorNobyte(byte[] bytes){
        byte byteout = 0x00;

        if (bytes.length != 12) {
            byteout=0x00;
        }else {
            byteout = bytes[5];
        }
        return  byteout;
    }
    //温度
    private static byte[] GetTemperaturebyte(byte[] bytes){
        byte[] byteout = new byte[2];

        if (bytes.length != 12) {
            byteout[0]=0x00;
            byteout[1]=0x00;
        }else {
            byteout[0] = bytes[6];
            byteout[1] = bytes[7];
        }
        return  byteout;
    }
    //湿度
    private static byte[] Gethumiditybyte(byte[] bytes){
        byte[] byteout = new byte[2];

        if (bytes.length != 12) {
            byteout[0]=0x00;
            byteout[1]=0x00;
        }else {
            byteout[0] = bytes[8];
            byteout[1] = bytes[9];
        }
        return  byteout;
    }
    //电压
    private static byte[] GetVoltagebyte(byte[] bytes){
        byte[] byteout = new byte[2];

        if (bytes.length != 12) {
            byteout[0]=0x00;
            byteout[1]=0x00;
        }else {
            byteout[0] = bytes[10];
            byteout[1] = bytes[11];
        }
        return  byteout;
    }
    private static int power(int a,int b){
        if (b == 0) return 1;
        int iout = 1;
        for (int i = 0;i < b;i++){
            iout*= a;
        }
        return iout;
    }
    public static String GetDeviceNo(byte[] bytes){
        byte[] bytesDeviceNo = GetDeviceNobyte(bytes);
        int iout = 0;
        int icnt = bytesDeviceNo.length;
        for (byte byteo:bytesDeviceNo){
            iout += (byteo&0xFF)*power(256,--icnt);

        }
        return new String().format("%05d",iout);
    }
    public static String GetSensorNo(byte[] bytes){
        byte byteSensorNo = GetSensorNobyte(bytes);
        int iout = byteSensorNo&0xFF;
        return new String().format("%02d",iout);
    }
    public static String GetTemperature(byte[] bytes){
        byte[] byteTemperature = GetTemperaturebyte(bytes);
        int iout = 0;
        int icnt = byteTemperature.length;
        for(byte b:byteTemperature){
            iout += (b&0xFF)*power(256,--icnt);
        }
        double iresult = 175.72*iout/65536.0 - 46.85;
        return new String().format("%.2f",iresult);
    }
    public static String Gethumidit(byte[] bytes){
        byte[] byteHumidit = Gethumiditybyte(bytes);
        int iout = 0;
        int icnt = byteHumidit.length;
        for (byte b:byteHumidit){
            iout += (b&0xFF)*power(256,--icnt);
        }
        double iresult = -6 + 125.0*iout / 65536.0;
        return new String().format("%.2f",iresult);
    }
    public static String GetVoltage(byte[] bytes){
        byte[] byteVoltage = GetVoltagebyte(bytes);
        int iout = 0;
        int icnt = byteVoltage.length;
        for(byte b:byteVoltage){
            iout += (b&0xFF)*power(256,--icnt);
        }
        double iresult = iout*5400.0/4095/1000;
        return new String().format("%.2f",iresult);
    }




    //判断字头前三字节是否合法,设备序号是否合法,字节长度是否是12字节
    public static boolean Judgefirst3bytes(byte[] bytes){
        if (bytes.length != 64) return  false;
        //if (bytes[5] < 0 || bytes[5] > 19) return false;
        if (bytes[0] == 0x56 ) return true;
        if (bytes[0] == 0X01 ) return true;
        return false;
    }


    //判断地址是否相同
    public static boolean CompareAddressbytes2(byte[] bytesOrigin,byte[] bytesNew){
        return (bytesOrigin[3] == bytesNew[3] && bytesOrigin[4] == bytesNew[4]);
    }



    //获取地址
    public static String GetAdress(String str){
        return str.substring(6,12);
    }
    public static byte[] hex2Bytes(String src){
        byte[] res = new byte[src.length()/2];
        char[] chs = src.toCharArray();
        int[] b = new int[2];

        for(int i=0,c=0; i<chs.length; i+=2,c++){
            for(int j=0; j<2; j++){
                if(chs[i+j]>='0' && chs[i+j]<='9'){
                    b[j] = (chs[i+j]-'0');
                }else if(chs[i+j]>='A' && chs[i+j]<='F'){
                    b[j] = (chs[i+j]-'A'+10);
                }else if(chs[i+j]>='a' && chs[i+j]<='f'){
                    b[j] = (chs[i+j]-'a'+10);
                }
            }
            b[0] = (b[0]&0x0f)<<4;
            b[1] = (b[1]&0x0f);
            res[c] = (byte) (b[0] | b[1]);
        }
        return res;
    }
    public static int GetContentLines(String string){
        if (string.length() <4) return 0;
        String strArry[] = string.split("\r\n");
        return strArry.length;
    }
}
