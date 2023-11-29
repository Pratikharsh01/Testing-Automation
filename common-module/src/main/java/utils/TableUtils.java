package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class TableUtils {

    WebDriver driver;
    By tableLocator;

    public TableUtils(By tableLocator) {
        this.tableLocator = tableLocator;
        driver = DriverManager.getCurrentDriver();
    }

    public List<String> getColumnNames() {
        List<String> columnNames = new ArrayList<>();
        driver.findElement(tableLocator).findElements(By.xpath("//thead/tr/th")).stream().forEach(column -> columnNames.add(column.getText()));
        return columnNames;
    }

    public List<String> getDataInAColumnByHeader(String searchColumn) {
        List<String> columnData = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();
        driver.findElement(tableLocator).findElements(By.xpath("//thead/tr/th")).stream().forEach(column -> columnNames.add(column.getText()));
        int columnIndex = 0;
        for (String column : columnNames) {
            columnIndex++;
            if (column.equalsIgnoreCase(searchColumn)) {
                break;
            }
        }
        driver.findElement(tableLocator).findElements(By.xpath("//tbody/tr/td[" + columnIndex + "]")).stream().forEach(cellElm -> columnData.add(cellElm.getText()));
        System.out.println("Data = " + columnData);
        return columnData;
    }

    public List<String> getDataInAColumnByIndex(int index) {
        List<String> columnData = new ArrayList<>();
        driver.findElement(tableLocator).findElements(By.xpath("//tbody/tr/td[" + index + "]")).stream().forEach(cellElm -> columnData.add(cellElm.getText()));
        return columnData;
    }

    public String getData(String columnName, int rowIndex) {
        String data = getDataInAColumnByHeader(columnName).get(rowIndex);

        return data;
    }

    public String getData(int columnIndex, int rowIndex) {
        String data = getDataInAColumnByIndex(columnIndex).get(rowIndex);

        return data;
    }

    public int getCountOfRowsInTable() {
        List<String> columnNames = new ArrayList<>();
        driver.findElement(tableLocator).findElements(By.xpath("//tbody/tr")).stream().forEach(column -> columnNames.add(column.getText()));
        return columnNames.size();
    }
}
