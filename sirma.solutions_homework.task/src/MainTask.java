import javafx.util.*;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class MainTask {

    public static List<String> splitData(String string) {
        return Stream.of(string.split(", "))
                .map(elem -> new String(elem))
                .collect(Collectors.toList());
    }

    public static Long getDifferenceOfDates(Date firstDate, Date secondDate) {
        long differenceInMillies = Math.abs(firstDate.getTime() - secondDate.getTime());
        long differenceInDays = TimeUnit.DAYS.convert(differenceInMillies, TimeUnit.MILLISECONDS);

        return differenceInDays;
    }

    public static void calculateDaysForEveryCouple(List<Record> recordsList, Map<List<String>, Long> daysByCoupleEmployeеsMap) {
        int recordsSize = recordsList.size();
        long difference = 0;

        Date startFirstDate, startSecondDate, finalFirstDate, finalSecondDate;

        for (int i = 0; i < recordsSize - 1; ++i) {
            for (int j = i + 1; j < recordsSize; ++j) {
                startFirstDate = recordsList.get(i).getDateFrom();
                startSecondDate = recordsList.get(j).getDateFrom();

                finalFirstDate = recordsList.get(i).getDateTo();
                finalSecondDate = recordsList.get(j).getDateTo();

                if (startFirstDate.compareTo(startSecondDate) <= 0) {
                    if (finalSecondDate.compareTo(finalFirstDate) <= 0) {
                        difference = getDifferenceOfDates(startSecondDate, finalSecondDate);
                    }

                    if (finalFirstDate.compareTo(startSecondDate) <= 0) {
                        difference = 0;
                    }

                    if ((startSecondDate.compareTo(finalFirstDate) <= 0) && (finalFirstDate.compareTo(finalSecondDate) <= 0)) {
                        difference = getDifferenceOfDates(startSecondDate, finalFirstDate);
                    }
                }

                else {
                    if (finalFirstDate.compareTo(finalSecondDate) <= 0) {
                        difference = getDifferenceOfDates(startFirstDate, finalFirstDate);
                    }

                    if (finalSecondDate.compareTo(startFirstDate) <= 0) {
                        difference = 0;
                    }

                    if ((startFirstDate.compareTo(finalSecondDate) <= 0) && (finalSecondDate.compareTo(finalFirstDate) <= 0)) {
                        difference = getDifferenceOfDates(startFirstDate, finalSecondDate);
                    }
                }

                List<String> currentCouple = new ArrayList<String>();
                if (recordsList.get(j).getEmployeеID().compareTo(recordsList.get(i).getEmployeеID()) < 0) {
                    currentCouple.add(recordsList.get(j).getEmployeеID());
                    currentCouple.add(recordsList.get(i).getEmployeеID());
                }

                else {
                    currentCouple.add(recordsList.get(i).getEmployeеID());
                    currentCouple.add(recordsList.get(j).getEmployeеID());
                }

                if (daysByCoupleEmployeеsMap.containsKey(currentCouple)) {
                    difference += daysByCoupleEmployeеsMap.get(currentCouple);
                }

                daysByCoupleEmployeеsMap.put(currentCouple, difference);
            }
        }

    }

    public static Pair<List<String>, Long> getMaxDaysCoupleEmployeеs(Map<List<String>, Long> daysByCoupleEmployeеsMap) {
        long maxDays = 0;
        List<String> result = null;

        for (Map.Entry<List<String>, Long> entry : daysByCoupleEmployeеsMap.entrySet()) {
            if (entry.getValue() >= maxDays) {
                maxDays = entry.getValue();
                result = entry.getKey();
            }
        }
        Pair<List<String>, Long> EmployeеsMaxDays = new Pair(result, maxDays);

        return EmployeеsMaxDays;
    }

    public static void main(String[] args) throws IOException, ParseException {

        System.out.println("Enter the full path of the input file: ");
        BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
        String filePath = cin.readLine();
        cin.close();


        String currentLine = null;
        List<String> currentRecord = null;
        List<Record> currentRecordList = null;
        Map RecordsListByProject = new HashMap<String, List<Record>>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        try {
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            while ((currentLine = buffReader.readLine()) != null) {
                currentRecord = splitData(currentLine);

                Date startDate, finalDate;

                startDate = dateFormat.parse(currentRecord.get(2));

                if (currentRecord.get(3).equals("NULL")) {
                    finalDate = new Date();
                }

                else {
                    finalDate = dateFormat.parse(currentRecord.get(3));
                }

                if(startDate.compareTo(finalDate) > 0){
                    throw new ParseException("Invalid input dates!", 1);
                }


                if (RecordsListByProject.containsKey(currentRecord.get(1))) {
                    currentRecordList = (List<Record>) RecordsListByProject.get(currentRecord.get(1));
                    currentRecordList.add(new Record(currentRecord.get(0), currentRecord.get(1), startDate, finalDate));
                }

                else {
                    currentRecordList = new ArrayList<>();
                    currentRecordList.add(new Record(currentRecord.get(0), currentRecord.get(1), startDate, finalDate));
                }

                RecordsListByProject.put(currentRecord.get(1), currentRecordList);
            }

            buffReader.close();

        }

        catch (IOException ioE) {
            ioE.printStackTrace();
            throw ioE;
        }

        catch (ParseException pE) {
            pE.printStackTrace();
            throw pE;
        }

        Map<List<String>, Long> DaysByCoupleEmployees = new HashMap<>();

        Collection<List<Record>> listCollection = RecordsListByProject.values();
        for (List<Record> listRecord : listCollection) {
            calculateDaysForEveryCouple(listRecord, DaysByCoupleEmployees);
        }

        Pair<List<String>, Long> EmployeesMaxDays = getMaxDaysCoupleEmployeеs(DaysByCoupleEmployees);

        String firstEmployeeID = EmployeesMaxDays.getKey().get(0);
        String secondEmployeeID = EmployeesMaxDays.getKey().get(1);

        System.out.println("First EmployeeID = " + firstEmployeeID + ", Second EmployeeID = " + secondEmployeeID);
        System.out.println("Number of days working together: " + EmployeesMaxDays.getValue());
    }

}
