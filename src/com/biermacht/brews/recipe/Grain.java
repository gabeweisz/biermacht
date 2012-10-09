package com.biermacht.brews.recipe;

// Grain subclass of Ingredient
public class Grain extends Ingredient {
	private double weight;
	private double color;
	private double gravity;
	private String grainType;
	private float efficiency;
	
	public static String GRAIN = "Grain";
	public static String EXTRACT = "Extract";
	public static String ADJUNCT = "Adjunct";

	public Grain(String name)
	{
		super(name);
		setName(name);
		this.weight = 0;
		this.color = 0;
		this.gravity = 1;
		this.grainType = GRAIN;
		this.efficiency = 1;
		setUnit("lbs");
	}
	
	public Grain(String name, String units, double colour, double grav, String gType)
	{
		super(name);
		setUnit(units);
		this.color = colour;
		this.gravity = grav;
		this.grainType = gType;
		this.efficiency = 1;
	}

	@Override
	public String getType() {
		return Ingredient.GRAIN;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getLovibondColor() {
		color = (float) Math.round(color * 10) / 10;
		return color;
	}

	public void setLovibondColor(double color) {
		color = (float) Math.round(color * 10) / 10;
		this.color = color;
	}

	public double getGravity() {
		gravity = (double) Math.round(gravity * 1000) / 1000;
		return gravity;
	}

	public void setGravity(double gravity) {
		this.gravity = gravity;
	}

	public String getGrainType() {
		return grainType;
	}	
	
	public void setEfficiency(float e)
	{
		this.efficiency = e;
	}
	
	public float getEfficiency()
	{
		return this.efficiency;
	}
	
	public void setGrainType(String s)
	{
		this.grainType = s;
	}

	public float getPpg() {
		
		float adjPPG = (float) (efficiency * gravity);
		
		return adjPPG;
	}
	
	@Override
	public double getAmount()
	{
		return getWeight();
	}
	
	@Override 
	public void setAmount(double amt)
	{
		setWeight(amt);
	}

	@Override
	public String getShortDescription() {
		// TODO
		return "";
	}
}

