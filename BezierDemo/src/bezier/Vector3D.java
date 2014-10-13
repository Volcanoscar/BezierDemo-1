package bezier;

public class Vector3D extends Point3D{

	public final byte X_AXIS = 0;
	public final byte Y_AXIS = 1;
	public final byte Z_AXIS = 2;
	
	public Vector3D(double x, double y, double z){
		super(x, y, z);
	}
	
	public Vector3D(Point3D v){
		super(v.getX(), v.getY(), v.getZ());
	}
	
	public double dotProduct(Vector3D v){
		return (this.getX()*v.getX()) + (this.getY()*v.getY()) + (this.getZ()*v.getZ());
	}
	
	//AKA Gibbs vector product, requires n to be normal of the plane this and v are in
	public Vector3D crossProduct(Vector3D v, Vector3D n){
		if(n.modulus() != 1){
			throw new IllegalArgumentException("crossProduct(Vector3D v, Vector3D n) requires n be the normal of the plane this and v are in");
		}
		return n.scalarProduct(this.modulus()*v.modulus() * Math.sin(getAngleTo(v)));
	}
	
	//mult. with scalar
	public Vector3D scalarProduct(double n){
		return new Vector3D(this.getX()*n, this.getY()*n, this.getZ()*n);
	}
	
	public double modulus(){
		return Math.sqrt(Math.pow(this.getX(), 2) + Math.pow(this.getY(), 2) + Math.pow(this.getZ(), 2));
	}

	public double getAngleTo(Vector3D v){
		return Math.acos(this.dotProduct(v) / (this.modulus() * v.modulus()));
	}
	
	public Vector3D add(Vector3D v){
		return new Vector3D(this.getX()+v.getX(), this.getY()+v.getY(), this.getZ()+v.getZ());
	}
	
	//unnecessary, but for completion's sake...
	public Vector3D sub(Vector3D v){
		return new Vector3D(this.getX()-v.getX(), this.getY()-v.getY(), this.getZ()-v.getZ());
	}
	
	//make this vector a unit vector
	public void normalise(){
		double mod = this.modulus();
		setX(this.getX()/mod);
		setY(this.getY()/mod);
		setZ(this.getZ()/mod); 
	}
	
	public Point3D toPoint(){
		return new Point3D(this.getX(), this.getY(), this.getZ());
	}
}
