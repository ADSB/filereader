import java.io.File;
import java.util.*;

public class MuWeatherReader {

	public static void main(String[] args) {
		File file = new File("weather-data.csv");
		Scanner scanner = null;

		try {
			scanner = new Scanner(file);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		String[] schema = scanner.nextLine().split(",");

		List<CityData> rainfall = new ArrayList<CityData>();
		String[] raw;
		List<Float> buffer;

		while (scanner.hasNextLine()) {
			raw = scanner.nextLine().trim().split(",");
			buffer = new ArrayList<Float>();
			for (int i = 1; i < raw.length; i++) {
				buffer.add(Float.parseFloat(raw[i]));
			}
			rainfall.add(new CityData(raw[0], buffer));
		}

		int size = rainfall.size();

		scanner = new Scanner(System.in);

		do {

			for (int i = 1; i < schema.length; i++) {
				System.out.printf("%s: %d\n", schema[i], i - 1);
			}

			String output = "";

			final int selection = scanner.nextInt();

			if (selection < 0 || selection > 4) {
				scanner.close();
				continue;
			}

			System.out.println("Mean: 0\nMin: 1\nMax: 2\nMedian: 3");

			final int datamode = scanner.nextInt();

			Collections.sort(rainfall, new Comparator<CityData>() {
				@Override
				public int compare(CityData o, CityData p) {
					return (int) (o.getData().get(selection) - p.getData().get(selection));
				}
			});

			switch (datamode) {
				case 0: {
					float total = 0.f;
					for (CityData city : rainfall) {
						total += city.getData().get(selection);
					}
					output = "Mean: " + total / size;
					break;
				}
				case 1: {
					CityData city = rainfall.get(0);
					output = "Min: " + city.getData().get(selection) + " - " + city.getName();
					break;
				}
				case 2: {
					CityData city = rainfall.get(rainfall.size() - 1);
					output = "Max: " + city.getData().get(selection) + " - " + city.getName();
					break;
				}
				case 3: {
					output = "Median: ";
					if (size % 2 == 0) {
						CityData city = rainfall.get(size / 2);
						output += city.getData().get(selection) + " - " + city.getName();
					}
					else {
						CityData lowCity = rainfall.get(size / 2);
						CityData highCity = rainfall.get(size / 2 + 1);
						output += (lowCity.getData().get(selection) + highCity.getData().get(selection)) / 2 + " - " + lowCity.getName() + ", " + highCity.getName();
					}
					break;
				}
				default:
					scanner.close();
					continue;
			}

			System.out.println(output + '\n');
		} while (scanner.hasNextLine());
	}

	protected static class CityData {
		protected String _name;
		protected List<Float> _data;

		public CityData(String name, List<Float> data) {
			_name = name;
			_data = data;
		}

		public String getName() {
			return _name;
		}
		public List<Float> getData() {
			return _data;
		}
	}

}
