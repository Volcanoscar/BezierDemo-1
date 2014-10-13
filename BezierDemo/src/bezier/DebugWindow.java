package bezier;

import java.awt.*;
import java.awt.event.*;

public class DebugWindow extends Frame implements ActionListener, MouseMotionListener, ItemListener{

	private static final long serialVersionUID = 0L;
	
	private int w, h;
	private Button addDegree, subDegree, changeResolution;
	private TextField resolution;
	private FlowLayout layout;
	private Checkbox drawControlGrid, showRaster;
	
	private Image bufferedImage;
	private Graphics bufferedGraphics;
	
	private Point3D[] controlPoints;
	private int curveResolution;
	
	public DebugWindow(int width, int height){
		this.w = width;
		this.h = height;
	}
	
	private Point3D[] randomPoints(int n){
		Point3D[] tmp = new Point3D[n];
		
		for(int i = 0; i < n; i++){
			tmp[i] = new Point3D(Math.random()*(double)this.w, Math.random()*this.h, 0);
		}
		
		return tmp;
	}
	
	public void launchInitSequence(){
		this.createComponents();
		this.addComponents();
		this.addActionListeners();
		
		this.setVisible(true);
		bufferedImage = this.createImage(this.w, this.h);
		bufferedGraphics = bufferedImage.getGraphics();
		curveResolution = 100;
		
		this.drawBezier();
	}
	
	private void addActionListeners(){
		WindowAdapter listener = new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		};

		this.addWindowListener(listener);
		this.addMouseMotionListener(this);
		
		addDegree.addActionListener(this);
		subDegree.addActionListener(this);
		changeResolution.addActionListener(this);
		drawControlGrid.addItemListener(this);
	}

	private void createComponents(){
		addDegree = new Button("Add control point");
		subDegree = new Button("Remove control point");
		resolution = new TextField("1000");
		changeResolution = new Button("Apply resolution");
		drawControlGrid = new Checkbox("Control Grid");
		showRaster = new Checkbox("Raster");
		layout = new FlowLayout();
		
		controlPoints = randomPoints(3);

		this.setSize(this.w, this.h);
		this.setTitle("Beziér Test Window");
		this.setLayout(layout);
		this.setBackground(Color.white);
		this.setResizable(false);
		this.setLocationByPlatform(true);
	}
	
	private void addComponents(){
		this.add(addDegree);
		this.add(subDegree);
		this.add(resolution);
		this.add(changeResolution);
		this.add(drawControlGrid);
		this.add(showRaster);
	}
	
	private void drawBezier(){
		BezierCurve curve = new BezierCurve(controlPoints);
		Point3D[] curvePoints;
		
		curvePoints = curve.calculateCurve(curveResolution);
		
		bufferedGraphics.clearRect(0, 0, this.w, this.h);
		bufferedGraphics.setFont(new Font("Arial", Font.BOLD, 14));
		
		bufferedGraphics.setColor(Color.GREEN);
		for(int i = 0; i + 1 < curvePoints.length; i++){
			bufferedGraphics.drawLine((int)curvePoints[i].getX(), (int)curvePoints[i].getY(), (int)curvePoints[i+1].getX(), (int)curvePoints[i+1].getY());
		}
		
		if(drawControlGrid.getState()){
			bufferedGraphics.setColor(Color.BLACK);
			for(int i = 0; i + 1 < controlPoints.length; i++){
				bufferedGraphics.drawLine((int)controlPoints[i].getX(), (int)controlPoints[i].getY(), (int)controlPoints[i+1].getX(), (int)controlPoints[i+1].getY());
			}
		}
		
		if(showRaster.getState()){
			bufferedGraphics.setColor(Color.BLUE);
			for(int i = 0; i < bufferedImage.getWidth(this); i = i+10){
				for(int j = 0; j < bufferedImage.getHeight(this); j = j+10){
					bufferedGraphics.drawLine(i, j, i, j);
				}
			}
		}
		
		bufferedGraphics.setColor(Color.RED);
		for(int i = 0; i < controlPoints.length; i++){
			bufferedGraphics.fillOval((int)controlPoints[i].getX() - 5, (int)controlPoints[i].getY() - 5, 10, 10);
		}
		bufferedGraphics.setColor(Color.GRAY);
		bufferedGraphics.drawString("Beziér control points: " + curve.getDimension(), 20, this.h - 20);
	}
	
	public void paint(Graphics g){
		 g.drawImage(bufferedImage, 0, 0, this);
	}
	
	public void update(Graphics g){
		paint(g);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.subDegree){
			Point3D[] tmp = new Point3D[this.controlPoints.length - 1];
			System.arraycopy(this.controlPoints, 0, tmp, 0, tmp.length);
			this.controlPoints = tmp;
			
			if(this.controlPoints.length <= 2){
				this.subDegree.setEnabled(false);
			}
		}
		if(e.getSource() == this.addDegree){
			Point3D[] tmp = new Point3D[this.controlPoints.length + 1];
			System.arraycopy(this.controlPoints, 0, tmp, 0, this.controlPoints.length);
			tmp[tmp.length - 1] = new Point3D(Math.random()*this.w, Math.random()*this.h, 0);
			this.controlPoints = tmp;
			
			if(this.controlPoints.length > 2){
				this.subDegree.setEnabled(true);
			}
		}
		if(e.getSource() == this.changeResolution){
			try{
				int check = Integer.parseInt(resolution.getText());
				if(check < 2){
					resolution.setText("invalid");
				} else {
					this.curveResolution = check;
				}
			} catch (NumberFormatException exception){
				resolution.setText("invalid");
			}
		}
		
		this.drawBezier();
		repaint();
	}

	public void mouseDragged(MouseEvent e) {
		Point3D p = new Point3D(e.getX(), e.getY(), 0);
		int pointIndex = -1;
		int d = Integer.MAX_VALUE;
		
		for(int i = 0; i < controlPoints.length; i++){
			if((int)Math.ceil(controlPoints[i].getDistanceTo(p)) < d){
				d = (int)Math.ceil(controlPoints[i].getDistanceTo(p));
				pointIndex = i;
			}
		}
		
		controlPoints[pointIndex] = p;
		
		this.drawBezier();
		this.repaint();
	}
	
	public void mouseMoved(MouseEvent arg0) {}
	
	public void itemStateChanged(ItemEvent e){
		if(e.getSource() == drawControlGrid){
			this.drawBezier();
			this.repaint();
		}
		if(e.getSource() == showRaster){
			this.drawBezier();
			this.repaint();
		}
	}
}
