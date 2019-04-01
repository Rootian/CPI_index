import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;


public class CPI_index {
	private static final double EARTH_RADIUS = 6371393; 
	static clustered_point[] centers; //total cluster centers
	static int num_cluster; //number of clusters
	static double cp;

	public static void main(String[] args) {
		// read csv file
		String filePath = new String("/Users/aaronkb/Desktop/512x512.csv");
		ReadCSV file_csv = new ReadCSV(filePath);
		ArrayList<clustered_point> points = file_csv.read(filePath);
//		System.out.printf("test %d",file_csv.num_cluster);
		num_cluster = file_csv.num_cluster;
		centers = new clustered_point[file_csv.num_cluster];
		for(int i = 0; i < num_cluster; i++) {
			centers[i] = cal_center(points,i+1);
		}
//		System.out.print(centers[186].index);
		cp = 0;
		//calculate cp of all points
		for (int i = 0; i < num_cluster; i ++) {
			cp += cal_cp(points,centers[i],i+1);
		}
		System.out.print(cp);

		
	}
	public static double cal_cp(ArrayList<clustered_point> cpoints,clustered_point cluster_center,int index) {
		//calculate CP for every cluster
		double cpi = 0;
		int count = 0;
		for(clustered_point cpoint : cpoints) {
			if(cpoint.index == index) {
				cpi += getDistance(cpoint.point, cluster_center.point);
				count ++;
			}
		}
		cpi = cpi / count;
		return cpi;
	}
	public static clustered_point cal_center(ArrayList<clustered_point> cpoints,int index) {
		//calculate center
		int count = 0;
		double X = 0, Y = 0, Z = 0;
		for(clustered_point cpoint : cpoints) {
			if(cpoint.index == index) {
				count ++;
				double lat,lon,x,y,z;
				lon = cpoint.point.getX() * Math.PI / 180;
				lat = cpoint.point.getY() * Math.PI / 180;
				x = Math.cos(lat) * Math.cos(lon);  
		        y = Math.cos(lat) * Math.sin(lon);  
		        z = Math.sin(lat);  
		        X += x;  
		        Y += y;  
		        Z += z; 
			}	        
		}
		X = X / count;
		Y = Y / count;
		Z = Z / count;
        double Lon = Math.atan2(Y, X);  
        double Hyp = Math.sqrt(X * X + Y * Y);  
        double Lat = Math.atan2(Z, Hyp); 
		double center_lat = Lat * 180 / Math.PI;
		double center_lon = Lon * 180 / Math.PI;
		Point2D p = new Point2D.Double(center_lon,center_lat);
		clustered_point center = new clustered_point(p, index);
		return center;
	}
	
	
	public static double getDistance(Point2D pointA, Point2D pointB) {
	    double radiansAX = Math.toRadians(pointA.getX()); 
	    double radiansAY = Math.toRadians(pointA.getY()); 
	    double radiansBX = Math.toRadians(pointB.getX()); 
	    double radiansBY = Math.toRadians(pointB.getY()); 

	    double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX)
	            + Math.sin(radiansAY) * Math.sin(radiansBY);
	    double acos = Math.acos(cos);
	    return EARTH_RADIUS * acos; 
	}

}

class ReadCSV{
	String filePath;
	int num_cluster;
	public ReadCSV(String file) {
		filePath = file;
	}
	public ArrayList<clustered_point> read(String file) {
		try {
			//read file
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			ArrayList<clustered_point> points = new ArrayList<clustered_point>();
			String line = null;
			int flag = 1;
			while((line = reader.readLine()) != null) {
				String[] item = line.split(",");
				if (flag == 1) {
					//read num of centers
					num_cluster = Integer.parseInt(item[2]);
					flag = 0;
				}
				Point2D point = new Point2D.Double(Double.parseDouble(item[0]),Double.parseDouble(item[1]));
				clustered_point p = new clustered_point(point, Integer.parseInt(item[2]));
				points.add(p);
			}
			System.out.printf("size:%d\n",points.size());
			return points;
		}

		catch(IOException e){
			System.out.println("Read file error!");
		}
		return null;
	}
	
}
class clustered_point{
	Point2D point;
	int index;
	public clustered_point(Point2D p, int i) {
		point = p;
		index = i;
	}
}





