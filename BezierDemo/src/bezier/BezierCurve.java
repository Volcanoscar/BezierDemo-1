package bezier;

public class BezierCurve {

	private Point3D[] controlPoints;
	
	public final byte X_AXIS = 0;
	public final byte Y_AXIS = 1;
	public final byte Z_AXIS = 2;
	
	public BezierCurve(Point3D[] ctrlPts){
		setControlPoints(ctrlPts);
	}
	
	public void setControlPoints(Point3D[] points){
		this.controlPoints = points;
	}
	
	public Point3D[] getControlPoints(){
		return this.controlPoints;
	}
	
	public int getDimension(){
		return controlPoints.length;
	}

	private Point3D doIteration(Point3D p1, Point3D p2, double t){
		if(t > 1 || t < 0){
			throw new IllegalArgumentException("doIteration(Point3D p1, Point3D p2, double t) requires t > 0 and t < 1.");
		}
		
		return new Point3D((1-t)*p1.getX() + t*p2.getX(), (1-t)*p1.getY() + t*p2.getY(), (1-t)*p1.getZ() + t*p2.getZ());
	}
	
	public Point3D calculatePoint(double t){
		if(t > 1 || t < 0){
			throw new IllegalArgumentException("calculatePoint(double t) requires t > 0 and t < 1.");
		}
		
		int d = this.getDimension();
		Point3D[][] tmpPoints = new Point3D[d][];
		
		for(int i = 0; i < d; i++){
			tmpPoints[i] = new Point3D[d - i];
		}
		
		tmpPoints[0] = this.getControlPoints();
		
		for(int i = 1; i < d ; i++){
			for(int j = 0; j < (d - i); j++){
				tmpPoints[i][j] = doIteration(tmpPoints[i-1][j], tmpPoints[i-1][j+1], t);
			}
		}
		return tmpPoints[d - 1][0];
	}
	
	public Point3D[] calculateCurve(int curveResolution){
		
		double deltaT = 1.0 / curveResolution;
		Point3D[] tmpPoints = new Point3D[curveResolution];
		
		for(int i = 0; i < curveResolution; i++){
			tmpPoints[i] = this.calculatePoint(deltaT*(double)i);
		}
		tmpPoints[tmpPoints.length - 1] = this.calculatePoint(1.0);
		
		return tmpPoints;
	}
	
	public Point3D[] calculateCurve(double deltaT){
		if(deltaT > 1 || deltaT < 0){
			throw new IllegalArgumentException("calculateCurve(double deltaT) requires deltaT > 0 and deltaT < 1.");
		} else {
			return this.calculateCurve((int)Math.ceil(1.0/deltaT));
		}
	}
}
