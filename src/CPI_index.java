import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;


public class CPI_index {
	private static final double EARTH_RADIUS = 6371393; // 平均半径,单位：m
	static clustered_point[] centers; //聚类中心集
	static int num_cluster; //聚类中心的个数

	public static void main(String[] args) {
		// 读取CSV文件中的聚类点数据
		String filePath = new String("bin\\512x512.csv");
		ReadCSV file_csv = new ReadCSV(filePath);
		ArrayList<clustered_point> points = file_csv.read(filePath);
//		System.out.printf("test %d",file_csv.num_cluster);
		num_cluster = file_csv.num_cluster;
		centers = new clustered_point[file_csv.num_cluster];
		centers = cal_centers(points);
		
	}
	public static clustered_point[] cal_centers(ArrayList<clustered_point> points) {
		//计算聚类点中心
		double X = 0, Y = 0, Z = 0;
		
		for(clustered_point cpoint : points) {
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
		
		return null;
	}
	
	/**
	 * 通过AB点经纬度获取距离
	 * @param pointA A点(经，纬)
	 * @param pointB B点(经，纬)
	 * @return 距离(单位：米)
	 */
	public static double getDistance(Point2D pointA, Point2D pointB) {
	    // 经纬度（角度）转弧度。弧度用作参数，以调用Math.cos和Math.sin
	    double radiansAX = Math.toRadians(pointA.getX()); // A经弧度
	    double radiansAY = Math.toRadians(pointA.getY()); // A纬弧度
	    double radiansBX = Math.toRadians(pointB.getX()); // B经弧度
	    double radiansBY = Math.toRadians(pointB.getY()); // B纬弧度

	    // 公式中“cosβ1cosβ2cos（α1-α2）+sinβ1sinβ2”的部分，得到∠AOB的cos值
	    double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX)
	            + Math.sin(radiansAY) * Math.sin(radiansBY);
//	    System.out.println("cos = " + cos); // 值域[-1,1]
	    double acos = Math.acos(cos); // 反余弦值
//	    System.out.println("acos = " + acos); // 值域[0,π]
//	    System.out.println("∠AOB = " + Math.toDegrees(acos)); // 球心角 值域[0,180]
	    return EARTH_RADIUS * acos; // 最终结果
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
			//读取聚类数据
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			ArrayList<clustered_point> points = new ArrayList<clustered_point>();
			String line = null;
			int flag = 1;
			while((line = reader.readLine()) != null) {
				String[] item = line.split(",");
				if (flag == 1) {
					//获取聚类个数
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
			System.out.println("读取文件出错!");
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





