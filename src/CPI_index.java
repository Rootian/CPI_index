import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;


public class CPI_index {
	private static final double EARTH_RADIUS = 6371393; // ƽ���뾶,��λ��m
	static clustered_point[] centers; //�������ļ�
	static int num_cluster; //�������ĵĸ���

	public static void main(String[] args) {
		// ��ȡCSV�ļ��еľ��������
		String filePath = new String("bin\\512x512.csv");
		ReadCSV file_csv = new ReadCSV(filePath);
		ArrayList<clustered_point> points = file_csv.read(filePath);
//		System.out.printf("test %d",file_csv.num_cluster);
		num_cluster = file_csv.num_cluster;
		centers = new clustered_point[file_csv.num_cluster];
		centers = cal_centers(points);
		
	}
	public static clustered_point[] cal_centers(ArrayList<clustered_point> points) {
		//������������
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
	 * ͨ��AB�㾭γ�Ȼ�ȡ����
	 * @param pointA A��(����γ)
	 * @param pointB B��(����γ)
	 * @return ����(��λ����)
	 */
	public static double getDistance(Point2D pointA, Point2D pointB) {
	    // ��γ�ȣ��Ƕȣ�ת���ȡ����������������Ե���Math.cos��Math.sin
	    double radiansAX = Math.toRadians(pointA.getX()); // A������
	    double radiansAY = Math.toRadians(pointA.getY()); // Aγ����
	    double radiansBX = Math.toRadians(pointB.getX()); // B������
	    double radiansBY = Math.toRadians(pointB.getY()); // Bγ����

	    // ��ʽ�С�cos��1cos��2cos����1-��2��+sin��1sin��2���Ĳ��֣��õ���AOB��cosֵ
	    double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX)
	            + Math.sin(radiansAY) * Math.sin(radiansBY);
//	    System.out.println("cos = " + cos); // ֵ��[-1,1]
	    double acos = Math.acos(cos); // ������ֵ
//	    System.out.println("acos = " + acos); // ֵ��[0,��]
//	    System.out.println("��AOB = " + Math.toDegrees(acos)); // ���Ľ� ֵ��[0,180]
	    return EARTH_RADIUS * acos; // ���ս��
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
			//��ȡ��������
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			ArrayList<clustered_point> points = new ArrayList<clustered_point>();
			String line = null;
			int flag = 1;
			while((line = reader.readLine()) != null) {
				String[] item = line.split(",");
				if (flag == 1) {
					//��ȡ�������
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
			System.out.println("��ȡ�ļ�����!");
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





