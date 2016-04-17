package com.example.chiakiiwamoto.countme;

/**
 * Created by chiakiiwamoto on 2/02/16.
 */
public class CoffeeModel {
    private String _coffeeName;
    private String _coffeeImage;
    private int _coffeeId;
    private int _sugars;
    private int _sugarSum;
    private int _caffeineSum;
    private int _sugarDailySum;
    private int _caffeineDailySum;
    private int _coffeeTablenumRows;
    private int _configTablenumRows;
    private int _chosenTablenumRows;
    private int _chosenDailyTablenumRows;


    public String get_coffeeName()
    {
        return _coffeeName;
    }

    public void set_coffeeName(String cname)
    {
        _coffeeName = cname;
    }

    public String get_coffeeImage() {
        return _coffeeImage;
    }

    public void set_coffeeImage(String iname) {
        _coffeeImage = iname;
    }

    public int get_coffeeId() {
        return _coffeeId;
    }

    public void set_coffeeId(int id) {
        _coffeeId = id;
    }

    public int get_sugars() {
        return _sugars;
    }

    public void set_sugars(int nsugar) {
        _sugars = nsugar;
    }

    public int get_sugarDailySum() {
        return _sugarDailySum;
    }

    public void set_sugarDailySum(int sugardc) {
        _sugarDailySum = sugardc;
    }

    public int get_caffeineDailySum() {
        return _caffeineDailySum;
    }

    public void set_caffeineDailySum(int caffeinedc) {
        _caffeineDailySum = caffeinedc;
    }

    public int get_sugarSum() {
        return _sugarSum;
    }

    public void set_sugarSum(int sugarc) {
        _sugarSum = sugarc;
    }

    public int get_caffeineSum() {
        return _caffeineSum;
    }

    public void set_caffeineSum(int caffeinec) {
        _caffeineSum = caffeinec;
    }

    public int get_coffeeTablenumRows() {
        return _coffeeTablenumRows;
    }

    public void set_coffeeTablenumRows(int coffeeTable) {
        _coffeeTablenumRows = coffeeTable;
    }

    public int get_configTablenumRows() {
        return _configTablenumRows;
    }

    public void set_configTablenumRows(int configTable) {
        _configTablenumRows = configTable;
    }

    public int get_chosenTablenumRows() {
        return _chosenTablenumRows;
    }

    public void set_chosenTablenumRows(int chosenTable) {
        _chosenTablenumRows = chosenTable;
    }

    public int get_chosenDailyTablenumRows() {
        return _chosenDailyTablenumRows;
    }

    public void set_chosenDailyTablenumRows(int chosenTableToday) {
        _chosenDailyTablenumRows = chosenTableToday;
    }

}
