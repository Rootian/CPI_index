import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
public class CPI_index {
	private static final double EARTH_RADIUS = 6371393; // ƽ���뾶,��λ��m

	public static void main(String[] args) {
		// ��ȡCSV�ļ��еľ��������
		String filePath = new String("bin\\512x512.csv");
		ReadCSV file_csv = new ReadCSV(filePath);
		ArrayList<clustered_point> points = file_csv.read(filePath);
//		System.out.printf("test %d",points.get(3).index);
		
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
	public ReadCSV(String file) {
		filePath = file;
	}
	public ArrayList<clustered_point> read(String file) {
		try {
			//��ȡ��������
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			ArrayList<clustered_point> points = new ArrayList<clustered_point>();
			String line = null;
			while((line = reader.readLine()) != null) {
				String[] item = line.split(",");
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





