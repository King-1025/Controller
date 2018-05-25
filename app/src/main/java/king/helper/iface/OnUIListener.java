package king.helper.iface;

public interface OnUIListener
{
	void OnLightStatusChange(boolean islightAlarmOpen,boolean islightSunOpen);
	void OnVoiceStatusChange(int selectId);
	void OnLEDWordStatusChange(int selectId);
}
