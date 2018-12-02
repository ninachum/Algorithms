import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.AcyclicSP;
import edu.princeton.cs.algs4.Queue;

public class SeamCarver {
	private EdgeWeightedDigraph ewd;
	private Picture pic;
	private int[] picArr;
	private int width;
	private int height;
	private double[] energyArr;
	
	public SeamCarver(Picture picture) {
		if (picture == null)
			throw new java.lang.IllegalArgumentException();
		
		// ewd not initialized here
		pic = new Picture(picture);
		width = pic.width();
		height = pic.height();
		picArr = new int[width * height];
		energyArr = new double[height * width];
		
		for (int x = 0; x < width; x++) 
			for (int y = 0; y < height; y++) 
				picArr[x + y * width] = (pic.getRGB(x, y));
	}
	
	public Picture picture() {

		pic = new Picture(width, height);
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				pic.setRGB(x, y, picArr[x + width * y]);
			}
		}
		
		return pic;
	}
	
	public int width() {
		return width;
	}
	
	public int height() {
		return height;
	}

	public double energy(int x, int y) {
		if (!isInRange(x, y))
			throw new java.lang.IllegalArgumentException();
		
		if (isCorner(x, y))
			return 1000;
		
		return Math.sqrt(verticalEnergy(x, y) + horizontalEnergy(x, y)); 
	}
	
	private boolean isInRange(int x, int y) {
		return (0 <= x && x < width && 0 <= y && y < height);
	}
	private boolean isCorner(int x, int y) {
		return (x == 0 || x == (width - 1) || y == 0 || y == (height - 1));
	}
	
	private int verticalEnergy(int x, int y) {
		int redDiff = ((picArr[(x + (y - 1) * width)] >> 16) & 0xFF) - ((picArr[(x + (y + 1) * width)] >> 16) & 0xFF);
		int greenDiff = ((picArr[(x + (y - 1) * width)] >> 8) & 0xFF) - ((picArr[(x + (y + 1) * width)] >> 8) & 0xFF);
		int blueDiff = ((picArr[(x + (y - 1) * width)]) & 0xFF) - ((picArr[(x + (y + 1) * width)]) & 0xFF);
		
		return (redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
	}
	
	private int horizontalEnergy(int x, int y) {
		int redDiff = ((picArr[((x - 1) + y * width)] >> 16) & 0xFF) - ((picArr[((x + 1) + y * width)] >> 16) & 0xFF);
		int greenDiff = ((picArr[((x - 1) + y * width)] >> 8) & 0xFF) - ((picArr[((x + 1) + y * width)] >> 8) & 0xFF);
		int blueDiff = ((picArr[((x - 1) + y * width)]) & 0xFF) - ((picArr[((x + 1) + y * width)]) & 0xFF);
		
		return (redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
	}
	
	public int[] findHorizontalSeam() {
		energyArr = new double[width * height];
		
		for (int x = 0; x < width; x++) 
			for (int y = 0; y < height; y++) 
				energyArr[x + y * width] = energy(x, y);
		
		ewd = new EdgeWeightedDigraph(width * height + 2);
		
		for(int y = 0; y < height; y++) {
			ewd.addEdge(new DirectedEdge(width * height, y * width, 0.0));
			ewd.addEdge(new DirectedEdge((width - 1) + y * width, width * height + 1, 1000.0));	
		}
		
		// NO right-end vertices!
		for (int x = 0; x < (width - 1); x++) {
			for (int y = 0; y < height; y++) {
				ewd.addEdge(new DirectedEdge(x + width * y, (x + 1) + width * y, energyArr[x + width * y]));
				if(isTop(x, y) && isTopSafe(x, y)) {
					ewd.addEdge(new DirectedEdge(x + width * y, (x + 1) + width * (y + 1), energyArr[x + width * y]));
				}
				else if(isBottom(x, y) && isBottomSafe(x, y)) {
					ewd.addEdge(new DirectedEdge(x + width * y, (x + 1) + width * (y - 1), energyArr[x + width * y]));
				}
				else if (isHorizontalSafe(x, y)){
					ewd.addEdge(new DirectedEdge(x + width * y, (x + 1) + width * (y - 1), energyArr[x + width * y]));
					ewd.addEdge(new DirectedEdge(x + width * y, (x + 1) + width * (y + 1), energyArr[x + width * y]));	
				}
			}
		}
		
		AcyclicSP asp = new AcyclicSP(ewd, width * height);
		Queue<Integer> temp = new Queue<Integer>();
		for (DirectedEdge x : asp.pathTo(width * height + 1)) 
			temp.enqueue(x.from());
		temp.dequeue();
		int returning[] = new int[temp.size()];
		for (int i = 0; i < returning.length; i++) {
			returning[i] = temp.dequeue() / width;
		}
		
		return returning;
	}
	
	private boolean isTopSafe(int x, int y) {
		return (y + 1 < height);
	}
	
	private boolean isBottomSafe(int x, int y) {
		return (y - 1 >= 0);
	}
	
	private boolean isHorizontalSafe(int x, int y) {
		return (isTopSafe(x, y) && isBottomSafe(x, y));
	}
	
	private boolean isTop(int x, int y) {
		return (y == 0);
	}
	
	private boolean isBottom(int x, int y) {
		return (y == (height - 1));
	}
	
	private boolean isLeftEnd(int x, int y) {
		return (x == 0);
	}
	
	private boolean isRightEnd(int x, int y) {
		return (x == (width - 1));
	}
	
	public int[] findVerticalSeam() {
		energyArr = new double[width * height];
		
		for (int x = 0; x < width; x++) 
			for (int y = 0; y < height; y++) 
				energyArr[x + y * width] = energy(x, y);
		
		ewd = new EdgeWeightedDigraph(width * height + 2);
		
		for(int x = 0; x < width; x++) {
			ewd.addEdge(new DirectedEdge(width * height, x, 0.0));
			ewd.addEdge(new DirectedEdge((width * (height - 1) + x), width * height + 1, 1000.0));	
		}
		
		// NO bottom vertices!
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < (height - 1); y++) {
				ewd.addEdge(new DirectedEdge(x + width * y, x + width * (y + 1), energyArr[x + width * y]));
				if (isLeftEnd(x, y) && isLeftEndSafe(x, y)) {
					ewd.addEdge(new DirectedEdge(x + width * y, (x + 1) + width * (y + 1), energyArr[x + width * y]));
				}
				else if (isRightEnd(x, y) && isRightEndSafe(x, y)) {
					ewd.addEdge(new DirectedEdge(x + width * y, (x - 1) + width * (y + 1), energyArr[x + width * y]));
				}
				else if (isVerticalSafe(x, y)){
					ewd.addEdge(new DirectedEdge(x + width * y, (x - 1) + width * (y + 1), energyArr[x + width * y]));
					ewd.addEdge(new DirectedEdge(x + width * y, (x + 1) + width * (y + 1), energyArr[x + width * y]));	
				}
			}
		}
		
		AcyclicSP asp = new AcyclicSP(ewd, width * height);
		Queue<Integer> temp = new Queue<Integer>();
		for (DirectedEdge x : asp.pathTo(width * height + 1)) 
			temp.enqueue(x.from());
		temp.dequeue();
		int returning[] = new int[temp.size()];
		for (int i = 0; i < returning.length; i++) {
			returning[i] = temp.dequeue() % width;
		}
		
		return returning;
	}

	private boolean isLeftEndSafe(int x, int y) {
		return (x - 1 >= 0);
	}
	
	private boolean isRightEndSafe(int x, int y) {
		return (x + 1 < width);
	}
	
	private boolean isVerticalSafe(int x, int y) {
		return (isLeftEndSafe(x, y) && isRightEndSafe(x, y));
	}
	
	public void removeHorizontalSeam(int[] seam) {
		if (height <= 1)
			throw new java.lang.IllegalArgumentException();
		if (seam == null || !isValidHorizontalSeam(seam))
			throw new java.lang.IllegalArgumentException();
		
		
		int new_height = height - 1;
		
		int[] newPicArr = new int[width * new_height];
		
		for (int x = 0; x < width; x++) {
			for (int temp = x + width * seam[x]; temp < width * (height - 1); temp += width) {
				picArr[temp] = picArr[temp + width];
			}
		}
		
		for (int i = 0; i < newPicArr.length; i++) {
			newPicArr[i] = picArr[i];
		}
		
		height = new_height;
		picArr = newPicArr;
		ewd = null;
		energyArr = null;
	}
	
	private boolean isValidHorizontalSeam(int[] seam) {
		if (seam == null)
			return false;
		if (seam.length != width)
			return false;
		
		for (int i = 0; i < seam.length - 1; i++) {
			int gap = seam[i] - seam[i + 1];
			if (!(-1 <= gap && gap <= 1))
				return false;
		}
		
		for (int i = 0; i < seam.length; i++) {
			if (!(0 <= seam[i] && seam[i] < height))
				return false;
		}
		return true;
	}
	
	public void removeVerticalSeam(int[] seam) {
		if (width <= 1)
			throw new java.lang.IllegalArgumentException();
		
		if (seam == null || !isValidVerticalSeam(seam))
			throw new java.lang.IllegalArgumentException();
		
		int newWidth = width - 1;
				
		int[] newPicArr = new int[newWidth * height];
		
		for (int y = 0; y < height; y++) {
			for (int temp = seam[y] + width * y; temp < (width * (y + 1)) - 1; temp++) {
				picArr[temp] = picArr[temp + 1];
			}
		}
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < newWidth; x++) {
				newPicArr[x + y * newWidth] = picArr[x + y * width];
			}
		}

		width = newWidth;
		picArr = newPicArr;
		ewd = null;
		energyArr = null;
	}
	
	private boolean isValidVerticalSeam(int[] seam) {
		if (seam == null)
			return false;
		if (seam.length != height)
			return false;
		
		for (int i = 0; i < seam.length - 1; i++) {
			int gap = seam[i] - seam[i + 1];
			if (!(-1 <= gap && gap <= 1))
				return false;
		}	
		
		for (int i = 0; i < seam.length; i++) {
			if (!(0 <= seam[i] && seam[i] < width))
				return false;
		}
		return true;
	}
	
}

