package a9;

//not finished with magnifier

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PixelInspectorUI extends JPanel implements ActionListener {

	public static Pixel reference = new ColorPixel(0.5,0.5,0.5);
	
	private JPanel inspector_panel;
	private JPanel button_panel;
	private ObservablePictureImpl magnified;
	private JLabel x_label;
	private JLabel y_label;
	private JLabel pixel_info;
	private JButton copyPixel;
	private Pixel copiedPix;
	private PictureView magnifyPicture;
	
	private List<PixelListener> listeners;
	
	
	public PixelInspectorUI() {
		
		x_label = new JLabel("X: ");
		y_label = new JLabel("Y: ");
		pixel_info = new JLabel("(r,g,b)");
		copyPixel = new JButton("Set Paint Brush Color");
		
		setLayout(new GridLayout(1,2));
		
		copyPixel.addActionListener(this);
		
		magnified = new ObservablePictureImpl(50,50);
		magnifyPicture = new PictureView(magnified);

		inspector_panel = new JPanel();
		inspector_panel.setLayout(new GridLayout(3,1));
		
		button_panel = new JPanel();
		button_panel.setLayout(new BorderLayout());
		
		inspector_panel.add(x_label);
		inspector_panel.add(y_label);
		inspector_panel.add(pixel_info);
		
		button_panel.add(copyPixel, BorderLayout.CENTER);
		button_panel.add(magnifyPicture, BorderLayout.EAST);
		
		add(inspector_panel);
		add(button_panel);
		
		listeners = new ArrayList<PixelListener>();
	}
	
	public void setInfo(int x, int y, Pixel p) {
		x_label.setText("X: " + x);
		y_label.setText("Y: " + y);
		pixel_info.setText(String.format("(%3.2f, %3.2f, %3.2f)", p.getRed(), p.getBlue(), p.getGreen()));	
		copiedPix = new ColorPixel(p.getRed(), p.getGreen(), p.getBlue());
	}
	
	public Pixel getPixelColor() {
		return magnifyPicture.getPicture().getPixel(0,0);
	}
		
	
	private void updateMagnifyPicture(int x, int y, Pixel p, Picture f) {
		if(x < 7){
			x = 7;
		}
		
		if(y < 7){
			y = 7;
		}
		
		if(x > f.getWidth() - 16){
			x = f.getWidth() - 16;
		}
		
		if(y > f.getHeight() - 16){
			y = f.getHeight() - 16;
		}
		
		SubPictureImpl magArea = new SubPictureImpl(f, x - 7, y - 7, 16, 16);
		
		for(int i = 0; i < magnified.getWidth(); i++){
			for(int j = 0; j < magnified.getHeight(); j++){
				int magAreaX = i / 8;
				int magAreaY = j / 8;
				
				magnified.setPixel(i, j, magArea.getPixel(magAreaX, magAreaY));
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		BrushPixel b = new BrushPixel();
		b.setPixel(copiedPix);
		
	}
	
}
