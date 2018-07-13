package king.helper.model;

public class CircularRodAction
{
	private int direction;
	private double polarDiameter;
	private double maxPolarDiameter;
	
	public final static int DIRECTION_EAST=0x0;
	public final static int DIRECTION_SOUTH=0x1;
	public final static int DIRECTION_WEST=0x2;
	public final static int DIRECTION_NORTH=0x3;
	public final static int DIRECTION_SOUTH_EAST=0x4;
	public final static int DIRECTION_NORTH_EAST=0x5;
	public final static int DIRECTION_SOUTH_WEST=0x6;
	public final static int DIRECTION_NORTH_WEST=0x7;
	public final static int DIRECTION_CENTER=0x8;

	public CircularRodAction(int direction, double polarDiameter, double maxPolarDiameter)
	{
		this.direction = direction;
		this.polarDiameter = polarDiameter;
		this.maxPolarDiameter = maxPolarDiameter;
	}
	
	public CircularRodAction(int direction)
	{
		this.direction = direction;
	}

	public void setPolarDiameter(double polarDiameter)
	{
		this.polarDiameter = polarDiameter;
	}

	public double getPolarDiameter()
	{
		return polarDiameter;
	}

	public void setMaxPolarDiameter(double maxPolarDiameter)
	{
		this.maxPolarDiameter = maxPolarDiameter;
	}

	public double getMaxPolarDiameter()
	{
		return maxPolarDiameter;
	}

	public void setDirection(int direction)
	{
		this.direction = direction;
	}

	public int getDirection()
	{
		return direction;
	}
}
