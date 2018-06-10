package king.helper.iface;

/**
 * Created by King on 2017/8/7.
 * 指令发送控制接口
 */

public interface OnTransmissionListener {
	 void receive(byte[]data);
}
