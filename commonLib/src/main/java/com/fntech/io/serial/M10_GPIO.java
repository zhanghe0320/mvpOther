package com.fntech.io.serial;

import com.fntech.Loger;

import java.io.FileOutputStream;

public class M10_GPIO {

	public static void _14443A_PowerOn() {
		writeFile("/sys/bus/platform/devices/odroid-sysfs/ic14443a_enable", "1");
		writeFile(
				"/sys/bus/platform/devices/odroid-sysfs/ic14443a_serial_switch",
				"0");
	}

	public static void _14443A_PowerOFF() {
		writeFile("/sys/bus/platform/devices/odroid-sysfs/ic14443a_enable", "0");
		writeFile(
				"/sys/bus/platform/devices/odroid-sysfs/ic14443a_serial_switch",
				"0");
	}

	public static void _14443_SwitchSerialPort() {
		writeFile("/sys/class/gpio/gpio908/direction", "out");
		writeFile("/sys/class/gpio/gpio908/value", "1");
	}
	public static void _1D_PowerOn()
	{
		writeFile("/sys/bus/platform/devices/odroid-sysfs/bardecoder_enable_switch", "1");
	}

	public static void _1D_PowerOff()
	{
		writeFile("/sys/bus/platform/devices/odroid-sysfs/bardecoder_enable_switch", "0");
	}

	public static void gamaPowerOn()
	{
		writeFile("/sys/bus/platform/devices/odroid-sysfs/bardecoder_enable_switch", "1");
		writeFile("/sys/bus/platform/devices/odroid-sysfs/r1000_serial_switch", "1");
	}

	public static void gamaPowerOff()
	{
		writeFile("/sys/bus/platform/devices/odroid-sysfs/bardecoder_enable_switch", "0");
		writeFile("/sys/bus/platform/devices/odroid-sysfs/r1000_serial_switch", "0");
	}

	public static void H7_PowerOn()
	{
		//writeFile("/sys/bus/platform/devices/odroid-sysfs/ic14443a_enable", "1");
      //writeFile("/sys/bus/platform/devices/odroid-sysfs/ic14443a_serial_switch", "0");
	}

	public static void H7_PowerOFF()
	{
		writeFile("/sys/bus/platform/devices/odroid-sysfs/ic14443a_enable", "0");
		writeFile("/sys/bus/platform/devices/odroid-sysfs/ic14443a_serial_switch", "0");
	}

	public static void U1_PowerOn() {
		writeFile("/sys/bus/platform/devices/odroid-sysfs/r1000_enable", "1");
		writeFile("/sys/bus/platform/devices/odroid-sysfs/r1000_serial_switch",
				"1");
		Loger.disk_log("M10_GPIO","上电成功","IDcard");
	}

	public static void U1_PowerOff() {
		writeFile("/sys/bus/platform/devices/odroid-sysfs/r1000_enable", "0");
		writeFile("/sys/bus/platform/devices/odroid-sysfs/r1000_serial_switch",
				"0");
	}

	public static void U6_PowerOn() {
		writeFile("/sys/bus/platform/devices/odroid-sysfs/ic14443a_enable", "1");
		writeFile("/sys/bus/platform/devices/odroid-sysfs/ic14443a_serial_switch",
				"0");
		Loger.disk_log("M10_GPIO","上电成功","IDcard");
	}

	public static void U6_PowerOFF() {
		writeFile("/sys/bus/platform/devices/odroid-sysfs/ic14443a_enable", "0");
		writeFile("/sys/bus/platform/devices/odroid-sysfs/ic14443a_serial_switch",
				"0");
	}

	public static void W433_PowerOn() {
		writeFile("/sys/bus/platform/devices/odroid-sysfs/rfid_enable", "1");
		writeFile("/sys/ bus/platform/devices/odroid-sysfs/rfid_serial_switch",
				"0");
	}

	public static void W433_PowerOff() {
		writeFile("/sys/bus/platform/devices/odroid-sysfs/rfid_enable", "0");
		writeFile("/sys/ bus/platform/devices/odroid-sysfs/rfid_serial_switch",
				"1");
	}
	public static void R1000_PowerOn() {
		writeFile("/sys/bus/platform/devices/odroid-sysfs/r1000_enable", "1");
		writeFile("/sys/bus/platform/devices/odroid-sysfs/r1000_serial_switch",
				"1");
	}

	public static void R1000_PowerOFF() {
		writeFile("/sys/bus/platform/devices/odroid-sysfs/r1000_enable", "0");
		writeFile("/sys/bus/platform/devices/odroid-sysfs/r1000_serial_switch",
				"0");
	}
	public static void GPS_PowerOn(){
		writeFile("/sys/devices/platform/odroid-sysfs/gps_enable", "1");
		writeFile("/sys/devices/platform/odroid-sysfs/gps_enable_switch", "0");
	}

	/**
	 * GPSģ���µ�
	 */
	public static void GPS_PowerOff(){
		writeFile("/sys/devices/platform/odroid-sysfs/gps_enable", "0");
		writeFile("/sys/devices/platform/odroid-sysfs/gps_enable_switch", "1");
	}
	public static void _15693_PowerOn()
	{
		writeFile("/sys/bus/platform/devices/odroid-sysfs/ic15693_enable", "1");
		writeFile("/sys/bus/platform/devices/odroid-sysfs/ic15693_serial_switch", "0");
	}

	public static void _15693_PowerOff()
	{
		writeFile("/sys/bus/platform/devices/odroid-sysfs/ic15693_enable", "1");
		writeFile("/sys/bus/platform/devices/odroid-sysfs/ic15693_serial_switch", "0");
	}
	private static void writeFile(String fileName, String writestr) {
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			byte[] bytes = writestr.getBytes();
			fout.write(bytes);
			Loger.disk_log("写文件","成功","走进去");
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
			Loger.disk_log("异常",":"+e,"走进去");
		}
	}
}
