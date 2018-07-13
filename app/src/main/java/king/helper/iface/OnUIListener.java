package king.helper.iface;

public interface OnUIListener
{
	int FLG_LIGHT_ALARM=0x00;
	
	int FLAG_LIGHT_SUN=0x01;
	
	void OnLightStatusChange(int flag,boolean isOpen);
	//void OnLightStatusChange(boolean islightAlarmOpen,boolean islightSunOpen);
	void OnVoiceStatusChange(int selectId);
	void OnLEDWordStatusChange(int selectId);
	void OnVolumeStatusChange(int value);
	void OnSpeedStatusChange(int type);
}
