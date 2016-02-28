package core.util;
/**
 * @author Maher Safadi
 *
 */
public class Message{
	public static final int info = 1;
	public static final int warning = 2;
	public static final int error = 3;
	private String code;
	private String arabicMsg;
	private String englishMsg;
	private int msgType;
	public Message(String code, String arabicMsg, String englishMsg, int type){
		this.code = code;
		this.arabicMsg = arabicMsg;
		this.englishMsg = englishMsg;
		this.msgType = type;
	}
	public Message(String code, int type){
		this.code = code;
		this.msgType = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getArabicMsg() {
		return arabicMsg;
	}
	public void setArabicMsg(String arabicMsg) {
		this.arabicMsg = arabicMsg;
	}
	public String getEnglishMsg() {
		return englishMsg;
	}
	public void setEnglishMsg(String englishMsg) {
		this.englishMsg = englishMsg;
	}
	public int getMsgType() {
		return msgType;
	}
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	public static int getInfo() {
		return info;
	}
	public static int getWarning() {
		return warning;
	}
	public static int getError() {
		return error;
	}
}
