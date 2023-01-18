import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
/**
 *	Population	Uses and sorts a database of USA cities based on user specifications
 *				Repeatedly asks the user to input a number from 1-6 to determine how
 *				to sort or filter the dataset and then prints and formats the data into
 *				a table.
 *
 *	Requires FileUtils and Prompt classes.
 *
 *	@author	Maya Pullara
 *	@since	01.10.2023
 */
public class Population{
	
	// List of cities
	private List<City> cities;
	
	// US data file
	private final String DATA_FILE = "usPopData2017.txt";
	
	public static void main(String[] args) {
		Population pop = new Population();
		pop.run();
	}

	/**
	 * 	Runs the main methods of the program. Iteratively takes in input and then runs
	 * 	the corresponding method to compelete the user-indicated sort and print results
	 */
	public void run(){
		printIntroduction();
		loadCities();

		int playChoice, elapsedTime = 0;

		do {
			printMenu();
			playChoice = Prompt.getInt("Enter Selection", 0, 9);
			System.out.println();
			List<City> printList = cities;
			switch(playChoice){
				case 1: elapsedTime = leastPop();
					System.out.println("Fifty least populous cities"); break;
				case 2: elapsedTime = mostPop(cities);
					System.out.println("Fifty most populous cities"); break;
				case 3: elapsedTime = namesAToZ();
					System.out.println("Fifty cities sorted by name"); break;
				case 4: elapsedTime = namesZToA();
					System.out.println("Fifty cities sorted by name descending"); break;
				case 5: printList = popState(); break;
				case 6: printList = popName(); break;
			}
			if(playChoice <= 6)
				printTable( printList );
			if(playChoice <= 4)
				System.out.printf("Elapsed Time %d milliseconds%n%n", elapsedTime);
		}while(playChoice <9);
		
		System.out.println("\nThanks for using Population!\n");
	}
	
	/**	Prints the introduction to Population */
	public void printIntroduction() {
		System.out.println("   ___                  _       _   _");
		System.out.println("  / _ \\___  _ __  _   _| | __ _| |_(_) ___  _ __ ");
		System.out.println(" / /_)/ _ \\| '_ \\| | | | |/ _` | __| |/ _ \\| '_ \\ ");
		System.out.println("/ ___/ (_) | |_) | |_| | | (_| | |_| | (_) | | | |");
		System.out.println("\\/    \\___/| .__/ \\__,_|_|\\__,_|\\__|_|\\___/|_| |_|");
		System.out.println("           |_|");
		System.out.println();
	}
	
	/**	Print out the choices for population sorting */
	public void printMenu() {
		System.out.println("1. Fifty least populous cities in USA (Selection Sort)");
		System.out.println("2. Fifty most populous cities in USA (Merge Sort)");
		System.out.println("3. First fifty cities sorted by name (Insertion Sort)");
		System.out.println("4. Last fifty cities sorted by name descending (Merge Sort)");
		System.out.println("5. Fifty most populous cities in named state");
		System.out.println("6. All cities matching a name sorted by population");
		System.out.println("9. Quit");
	}
	
	/** read the population file and fill  List<City> */
	public void loadCities(){
		cities = new ArrayList<City>();
		
		Scanner readCities = FileUtils.openToRead(DATA_FILE);
		readCities.useDelimiter("[\t\n]");
		
		while( readCities.hasNext() ){
			cities.add(new City(readCities.next(), readCities.next(), 
				readCities.next(), Integer.parseInt(readCities.next())));
		}
		
		System.out.println( cities.size() + " cities in database\n");
	}

	/**
	 *	Print a table of up to the first 50 items of a provided Lists
	 *
	 * 	@param printList 	List of items to print
	 */
	public void printTable(List<City> printList){
		System.out.printf("%5s%-22s %-22s %-12s %12s%n", "", "State", "City", "Type", "Population");
		int count = 1;
		while( count<=50 && count <= printList.size()){
			System.out.printf("%4d:" + printList.get(count-1) + "\n", count);
			count++;
		}
		System.out.println();
	}

	/**
	 * 	swaps the values of 2 indeces within a list
	 *
	 * 	@param ind1	ind2 	Indeces of elements to be swapped
	 * 	@param largerList	List to be swapped within
	 */
	public void swap(int ind1, int ind2, List<City> largerList){
		City temp = new City(largerList.get(ind1));
		largerList.set(ind1, largerList.get(ind2));
		largerList.set(ind2, temp);
	}

	/**
	 * 	Uses a Selection sort to sort the cities list by populations
	 * 	from least to greatest
	 *
	 * 	@return	integer time in milliseconds that the sort took to complete
	 */
	public int leastPop() {
		long startMillisec = System.currentTimeMillis();

		int max = 0;
		for (int i = cities.size() - 1; i > 0; i--) {
			max = i;
			for (int j = i; j >= 0; j--) {
				if (cities.get(max).compareTo(cities.get(j)) < 0){
					max = j;
				}
			}
			swap(max, i, cities);
		}

		long endMillisec = System.currentTimeMillis();
		return (int)(endMillisec-startMillisec);
	}

	/**
	 * 	Uses a Merge sort to sort the a given List by populations
	 * 	from greatest to least
	 *
	 * 	@param 	cityList	The list to be sorted
	 * 	@return		integer time in milliseconds that the sort took to complete
	 */
	public int mostPop( List<City> cityList ){
		long startMillisec = System.currentTimeMillis();

		mergeSortPop(0, cityList.size()-1, cityList);

		long endMillisec = System.currentTimeMillis();
		return (int)(endMillisec-startMillisec);
	}

	/**
	 * 	Completes a merge sort to sort cities based first on their population, then their
	 * 	state, and finally their name,
	 *
	 * 	@param 	start, end 	the first and last index of the indicated list indicating the
	 * 	              		section of the list currently being sorted
	 * 	@param	largerList 	The list to be sorted, named because it is not a sublist/section
	 * 	                    of the list to be sorted.
	 */
	public void mergeSortPop( int start, int end, List<City> largerList){
		if (end-start > 1){
			mergeSortPop(start, (start + end)/2, largerList);
			mergeSortPop((start + end)/2 + 1, end, largerList);

			List<City> list1 = new ArrayList<City>(largerList.subList(start, (start + end)/2+1));
			List<City> list2 = new ArrayList<City>(largerList.subList((start + end)/2 + 1, end+1));
			int point1, point2, ind;
			point1 = point2 = 0;
			ind = start;
			while( point1 < list1.size() && point2 < list2.size()){
				if( list1.get(point1).compareTo(list2.get(point2)) > 0 ){
					largerList.set(ind, list1.get(point1));
					point1++;
				}
				else{
					largerList.set(ind, list2.get(point2));
					point2++;
				}
				ind++;
			}
			while(point1 < list1.size()){
				largerList.set(ind, list1.get(point1));
				point1++;
				ind++;
			}
			while(point2 < list2.size()){
				largerList.set(ind, list2.get(point2));
				point2++;
				ind++;
			}
		} else if (end - start == 1 && largerList.get(end).compareTo(largerList.get(start)) > 0){
			swap(start, end, largerList);
		} else {
			return;
		}
	}

	/**
	 * 	Uses an Insertion sort to sort the cities list by names
	 * 	from A to Z
	 *
	 * 	@return	integer time in milliseconds that the sort took to complete
	 */
	public int namesAToZ(){
		long startMillisec = System.currentTimeMillis();

		for(int i = 1; i < cities.size(); i++){
			int j = i;

			while( j >= 1 && cities.get(j).compareToNames(cities.get(j-1)) < 0 ){
				swap(j-1, j, cities);
				j--;
			}
		}

		long endMillisec = System.currentTimeMillis();
		return (int)(endMillisec-startMillisec);
	}

	/**
	 * 	Uses a Merge sort to sort the cities list by names
	 * 	from Z to A
	 *
	 * 	@return	integer time in milliseconds that the sort took to complete
	 */
	public int namesZToA(){
		long startMillisec = System.currentTimeMillis();

		mergeSortNames(0, cities.size()-1);

		long endMillisec = System.currentTimeMillis();
		return (int)(endMillisec-startMillisec);
	}

	/**
	 * 	Completes a merge sort to sort cities based on their name and population,
	 * 	first sorting by names from Z to A and the secondarily sorting by population
	 *
	 * 	@param start, end 	the first and last index of the cities list indicating the
	 * 	              		section of the list currently being sorted
	 */
	public void mergeSortNames( int start, int end){
		if (end-start > 1){
			mergeSortNames(start, (start + end)/2);
			mergeSortNames((start + end)/2 + 1, end);

			List<City> list1 = new ArrayList<City>(cities.subList(start, (start + end)/2+1));
			List<City> list2 = new ArrayList<City>(cities.subList((start + end)/2 + 1, end+1));
			int point1, point2, ind;
			point1 = point2 = 0;
			ind = start;

			while( point1 < list1.size() && point2 < list2.size()){
				if( list1.get(point1).compareToNames(list2.get(point2)) > 0){
					cities.set(ind, list1.get(point1));
					point1++;
				}
				else{
					cities.set(ind, list2.get(point2));
					point2++;
				}
				ind++;
			}
			while(point1 < list1.size()){
				cities.set(ind, list1.get(point1));
				point1++;
				ind++;
			}
			while(point2 < list2.size()){
				cities.set(ind, list2.get(point2));
				point2++;
				ind++;
			}
		} else if (end - start == 1 && cities.get(start).compareToNames(cities.get(end)) < 0){
			swap(start, end, cities);
		} else {
			return;
		}
	}

	/**
	 * 	Filters through cities list to create a new list of cities in exclusively
	 * 	one user-defined state. Then uses the mostPop() method to sort this new list
	 * 	by populations from least to greatest.
	 *
	 * 	@return	the new shortened and sorted list
	 */
	public List<City> popState(){
		String stateName;
		List<City> stateList = new ArrayList<City>();
		do {
			stateList = new ArrayList<City>();
			stateName = Prompt.getString("Enter state name (ie. Alabama)");
			for( City city: cities){
				if( stateName.equals(city.getState())) {
					stateList.add(city);
				}
			}
		}while(stateList.size() == 0);

		System.out.println("\nFifty most populous cities in " + stateName);

		mostPop(stateList);

		return stateList;
	}

	/**
	 * 	Filters through cities list to create a new list of cities with exclusively
	 * 	one user-defined name. Then uses the mostPop() method to sort this new list
	 * 	by populations from least to greatest.
	 *
	 * 	@return	the new shortened and sorted list
	 */
	public List<City> popName(){
		String cityName;
		List<City> nameList = new ArrayList<City>();
		do {
			nameList = new ArrayList<City>();
			cityName = Prompt.getString("Enter city name");
			for( City city: cities){
				if( cityName.equals(city.getName())) {
					nameList.add(city);
				}
			}
		}while(nameList.size() == 0);
		
		System.out.println("\nCity " + cityName + " by population");

		mostPop(nameList);

		return nameList;
	}
}
