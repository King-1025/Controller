package king.helper.iface;

/**
 * Created by King on 2017/8/12.
 * 连接状态监听器
 */

public interface OnConnectionListener {
	  void  onConnect();
	  void  onReconnect();
	  void  onClose();
	  void  onSuccess();
	  void  onFaild(String error);
}
