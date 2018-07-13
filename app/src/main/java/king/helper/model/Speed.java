package king.helper.model;

public class Speed
{
	//轴向
	private double axialX;
	private double axialY;
	//斜向
	private double obliqueX;
	private double obliqueY;

	public Speed(){
		
	}
	
	public Speed(double axialX, double axialY, double obliqueX, double obliqueY)
	{
		this.axialX = axialX;
		this.axialY = axialY;
		this.obliqueX = obliqueX;
		this.obliqueY = obliqueY;
	}

	public void setAxialX(double axialX)
	{
		this.axialX = axialX;
	}

	public double getAxialX()
	{
		return axialX;
	}

	public void setAxialY(double axialY)
	{
		this.axialY = axialY;
	}

	public double getAxialY()
	{
		return axialY;
	}

	public void setObliqueX(double obliqueX)
	{
		this.obliqueX = obliqueX;
	}

	public double getObliqueX()
	{
		return obliqueX;
	}

	public void setObliqueY(double obliqueY)
	{
		this.obliqueY = obliqueY;
	}

	public double getObliqueY()
	{
		return obliqueY;
	}
}
