package king.helper.iface;
import king.helper.model.*;

public interface OnInstructionMakeListener
{
	void onTouch(double polarAngle, double polarDiameter, double maxPolarDiameter);
	void onInstructionMade(Instruction instruction);
	
}
