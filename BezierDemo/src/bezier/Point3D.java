package bezier;

public class Point3D {

	private double x, y, z;

	public Point3D(double x, double y, double z){
		setX(x);
		setY(y);
		setZ(z);
	}
	
	public void setX(double newX){
		this.x = newX;
	}
	
	public void setY(double newY){
		this.y = newY;
	}
	
	public void setZ(double newZ){
		this.z = newZ;
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
	public double getZ(){
		return this.z;
	}
	

	public double getDistanceTo(Point3D p){
		double dx = Math.pow(p.getX() - this.getX(), 2);
		double dy = Math.pow(p.getY() - this.getY(), 2);
		double dz = Math.pow(p.getZ() - this.getZ(), 2);
		return Math.sqrt(dx + dy + dz);
	}
	
	public Point3D getMidPointTo(Point3D p){
		return new Point3D((p.getX() + this.getX()) / 2, (p.getY() + this.getY()) / 2, (p.getZ() + this.getZ()) / 2);
	}
	
	public String toString(){
		return "X, Y, Z: (" + this.getX() + ", " + this.getY() + ", " + this.getZ() + ")";
	}
	
	public void print(){
		System.out.println(this.toString());
	}
}
