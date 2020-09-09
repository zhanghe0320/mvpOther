/*
 * Copyright 2009 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mvp.serialPort;

import android.util.Log;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;

import android_serialport_api.SerialPort;

/**
 * 串口工具处理
 */
public abstract class SerialPortUtil {
    private static SerialPortUtil serialPortUtil;
    public String strComNo = null;
    public SerialPort mSerialPort = null;
    public OutputStream mOutputStream = null;
    public InputStream mInputStream;
    private ReadThread mReadThread;
    private static final String TAG = "SerialPortActivity";
    //55 11 21  8 8
    public static final int mdatalength = 40;
    private static byte[] receivelist = new byte[256];//缓冲区大小 ljs
    private static int receiveLength = 40;//接收的长度 ljs
    private static List<byte[]> ReceiveByteList = null;//ljs
    private static Date dateFlag = new Date();//ljs

    String byteMessFrom = "";

    public static SerialPortUtil getInstance() {
        // 双重判空机制 保证线程安全
        if (serialPortUtil == null) {
            synchronized (SerialPortUtil.class) {
                if (serialPortUtil == null) {
                    serialPortUtil = new SerialPortUtil() {
                        @Override
                        protected void onDataReceived(byte[] buffer, int size) {
                            //发送数据处理
                            byteMessFrom = byteMessFrom + ConstantValueSerialPort.byte2hex(buffer, size);
                            if (byteMessFrom.length() >= 36) {
                                Log.i(TAG, "----" + byteMessFrom);
                            }
                            //接收到数据立即进行关闭处理
                            serialPortUtil.closeSerialPort();
                        }
                    };
                    // serialPortUtil.onCreate();
                }
            }
        }
        return serialPortUtil;
    }

    public SerialPortUtil() {
        onCreate();
    }


    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            Date dateFlag = new Date();//ljs
            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[256];
                    if (mInputStream == null) {
                        return;
                    }

                    //size = mInputStream.read(buffer);
                    size = mInputStream.read(buffer, 0, mdatalength);
                    if (size > 0) {
                        onDataReceived(buffer, size);
                        Log.i(TAG, "run: " + buffer.toString());
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private void waitBagData(byte b[], int size, long diff) {
        byte finalCmdByte[] = new byte[256];
        int finalByteSize = 0;
        if (receiveLength > 0 && diff > 100) {
            byte[] finalCmdByte1 = new byte[receiveLength];
            for (int j = 0; j < receiveLength; j++) {
                finalCmdByte1[j] = receivelist[j];
            }
            onDataReceived(finalCmdByte1, receiveLength);
            //	Log.i(TAG, "waitBagData: "+finalCmdByte1.toString());
            receiveLength = 0;
        }
        if (receiveLength + size < 256) {
            receiveLength = receiveLength + size;
        } else
            receiveLength = 0;

        for (int i = receiveLength - size; i < receiveLength; i++)//拼包加入大缓冲区
        {
            receivelist[i] = b[i - receiveLength + size];
        }
    }


    public void onCreate() {
        //strComNo = MyOperFun.ReadSharedPreferencesString(MyOperFun.KeyID.eComeNo,this);
        //	Log.i(TAG, "onCreate: ********************getSerialPort");
        if (mSerialPort != null) {
            new Thread() {
                @Override
                public void run() {
                    if (strComNo == null) {
                        // strComNo = new SystemValuesDao(context).dbQueryOneByName("{ConstantValue.serialPort}").getValue();
                    }
                    try {
                        if (mSerialPort == null) {
                            mSerialPort = serialPortUtil.getSerialPort();
                        }
                        if (mOutputStream == null) {
                            mOutputStream = mSerialPort.getOutputStream();
                        }
                        mInputStream = mSerialPort.getInputStream();
                        /* Create a receiving thread */
                        mReadThread = new ReadThread();
                        mReadThread.start();
                    } catch (SecurityException e) {
                        //DisplayError(R.string.error_security);
                        Log.i(TAG, "run: 串口");
                    } catch (IOException e) {
                        // DisplayError(R.string.error_unknown);
                        Log.i(TAG, "run: IO错误");
                    } catch (InvalidParameterException e) {
                        // DisplayError(R.string.error_configuration);
                        Log.i(TAG, "run: 初始化出错");
                    }
                }
            }.start();
        }
    }

    //接收信息
    protected abstract void onDataReceived(final byte[] buffer, final int size);


    protected void onDestroy() {
        if (mReadThread != null)
            mReadThread.interrupt();
        serialPortUtil.closeSerialPort();
        mSerialPort = null;
    }


    //发送信息
    public class SendMessage extends Thread {
        public byte[] bytes;

        @Override
        public void run() {
            super.run();
            try {
                //for(int i = 0; i < 1;i++){

                mOutputStream.write(bytes);
                //Thread.sleep(5000);
                //	Log.i(TAG, "run: "+bytes);
                //Log.i(TAG, "run: "+byteUtil.toHexString(bytes));
                //	Log.i(TAG, "run: "+byteUtil.byteArrayToHexString(bytes));


                //	Log.i(TAG, "run: "+byteUtil.byte2HexStr(bytes)+"-------------------");
                //}

            } catch (IOException e) {
                e.printStackTrace();
            }/*catch (InterruptedException e ){
				e.printStackTrace();
			}*/

        }
    }


    public synchronized void SendMessage(byte[] bytes) {
        if(mSerialPort == null){
            return;
        }
        //防止丢包，发三次
	/*	if (null != BaseActivity.mstrComdata){
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					Log.i(TAG, "SendMessage: "+byteUtil.byte2HexStr(bytes));
					SendMessage sendMessage = new SendMessage();
					sendMessage.bytes = bytes;
					sendMessage.start();
				}
			},5000);

		}*/


        //	Log.i(TAG, "SendMessage: "+byteUtil.byte2HexStr(bytes));
        SendMessage sendMessage = new SendMessage();
        sendMessage.bytes = bytes;
        sendMessage.start();

    }


    private SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {

        if (mSerialPort == null) {
            /* Read serial port parameters */
            //  SharedPreferences sp = getSharedPreferences("android.serialport.sample_preferences", MODE_PRIVATE);
            String path = "valueSystemDao.dbQueryOneByName(ConstantValue.serialPort).getValue()";//"/dev/ttyS0";// sp.getString("DEVICE", "");

            int baud_rate = Integer.decode("9600");
            int data_bits = Integer.decode("8");
            int stop_bits = Integer.decode("1");
            int flow = 0;
            int parity = 'N';
            String flow_ctrl = "None";
            String parity_check = "None";
            /* Check parameters */
            if ((path.length() == 0) || (baud_rate == -1)) {
                throw new InvalidParameterException();
            }

            if (flow_ctrl.equals("RTS/CTS")) {
                flow = 1;
            } else if (flow_ctrl.equals("XON/XOFF")) {
                flow = 2;
            }

            if (parity_check.equals("Odd")) {
                parity = 'O';
            } else if (parity_check.equals("Even")) {
                parity = 'E';
            }

            /* Open the serial port */
            //mSerialPort = new SerialPort(new File("/dev/ttySAC2"), 9600, 8);
            mSerialPort = new SerialPort(new File("9600"), 9600, 8);
            //mSerialPort = new SerialPort(new File("/dev/ttyS0"), 9600, 8);
        }
        return mSerialPort;
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }

}
