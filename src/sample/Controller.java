package sample;


import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import clasessForTable.Result;
import clasessForTable.RoutsData;
import clasessForTable.TableColumnData;
import graph.Dijkstra;
import graph.Graph;
import graph.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:tcp://localhost/~/test";

    static final String USER = "sa";
    static final String PASS = "";
    Connection connection = null;
    Statement statement = null;
    ResultSet rs = null;
    List<Node> nodes = new ArrayList<Node>();
    Graph graph = new Graph();
    int id = 1;
    private ObservableList<TableColumnData> pipelineSystemData = FXCollections.observableArrayList();
    private ObservableList<RoutsData> routsToFindData = FXCollections.observableArrayList();
    private ObservableList<Result> resultData = FXCollections.observableArrayList();
    @FXML
    private TableView<Result> resultTable;

    @FXML
    private TableColumn<Result, String> resultColumn;

    @FXML
    private TableView<RoutsData> routsData;

    @FXML
    private TableColumn<RoutsData, Integer> idA;


    @FXML
    private TextField outLength, outStartPoint, outFinishPoint;

    @FXML
    private TableColumn<RoutsData, Integer> idB;
    @FXML
    private TableView<TableColumnData> tableSystem;

    @FXML
    private TableColumn<TableColumnData, Integer> startPointColumn;

    @FXML
    private TableColumn<TableColumnData, Integer> finishPointColumn;

    @FXML
    private TableColumn<TableColumnData, Integer> lengthColumn;

    @FXML
    private Button buttonChangeFirstTable;

    @FXML
    private TextField outIdB, outIdA;

    @FXML
    void ClickToAddPoints(ActionEvent event) throws Exception {
        //pipelineSystemData.add(new TableColumnData(Integer.parseInt(outStartPoint.getText()), Integer.parseInt(outFinishPoint.getText()), Integer.parseInt(outLength.getText())));
        CSVWriter writer = new CSVWriter(new FileWriter("set_of_points.csv" , true), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
        String[] record = (outIdA.getText() + ";" + outIdB.getText()).split(",");
        writer.writeNext(record);
        writer.close();
        //System.out.println(outFinishPoint.getText() + "   ");
        initialize();
    }


    @FXML
    void ClickToChangeFirstTable(ActionEvent event) throws Exception {
        //pipelineSystemData.add(new TableColumnData(Integer.parseInt(outStartPoint.getText()), Integer.parseInt(outFinishPoint.getText()), Integer.parseInt(outLength.getText())));
        CSVWriter writer = new CSVWriter(new FileWriter("pipeline_system.csv" , true), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
        String[] record = (outStartPoint.getText() + ";" + outFinishPoint.getText() + ";" + outLength.getText()).split(",");
        writer.writeNext(record);
        writer.close();
        //System.out.println(outFinishPoint.getText() + "   ");
       initialize();
    }

    @FXML
    public void initialize() throws Exception{
        if(!tableSystem.getItems().isEmpty()) {
            tableSystem.getItems().clear();
            routsData.getItems().clear();
            resultTable.getItems().clear();
        }
        initSystemData();
        initData();
        initRoutsData();
        initResult();
        startPointColumn.setCellValueFactory(new PropertyValueFactory<TableColumnData, Integer>("startPoint"));
        finishPointColumn.setCellValueFactory(new PropertyValueFactory<TableColumnData, Integer>("finishPoint"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<TableColumnData, Integer>("length"));


        idA.setCellValueFactory(new PropertyValueFactory<RoutsData, Integer>("idA"));
        idB.setCellValueFactory(new PropertyValueFactory<RoutsData, Integer>("idB"));

        resultColumn.setCellValueFactory(new PropertyValueFactory<Result, String>("result"));

        resultTable.setItems(resultData);
        routsData.setItems(routsToFindData);
        tableSystem.setItems(pipelineSystemData);
    }
    private void initData() {
        try {

        Class.forName(JDBC_DRIVER);

        connection = DriverManager.getConnection(DB_URL, USER, PASS);
        statement = connection.createStatement();
        String query = "select * from Pipeline_system";
        rs = statement.executeQuery(query);
        while (rs.next()){
            pipelineSystemData.add(new TableColumnData(rs.getInt("start_point"), rs.getInt("finish_point"), rs.getInt("length")));
        }

        }catch (SQLException se){
            se.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void initRoutsData()throws Exception{

        CSVReader reader = new CSVReader(new FileReader("set_of_points.csv"), ';' , '"' , 1);
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            Integer startPoint = Integer.parseInt(nextLine[0]);
            Integer finishPoint = Integer.parseInt(nextLine[1]);

            routsToFindData.add(new RoutsData(startPoint, finishPoint));

        }
    }
    public void initResult() throws Exception{
        CSVReader reader = new CSVReader(new FileReader("result.csv"), ';' , '"' , 1);
        //Read CSV line by line and use the string array as you want
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            if (nextLine != null) {
                //Verifying the read data here
                resultData.add(new Result(new String(nextLine[0].replace('"', ' '))));
            }
        }
    }

    public void initSystemData() throws SQLException{
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<Node> nodes = new ArrayList<Node>();
        Graph graph = new Graph();
        int id = 1;
        try{
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            createSimpleDBSchema(connection);
            statement = connection.createStatement();
            String query = "delete from Pipeline_system";
            statement.execute(query);

            try{
                CSVReader reader = new CSVReader(new FileReader("pipeline_system.csv"), ';' , '"' , 1);
                //Read CSV line by line and use the string array as you want
                String[] nextLine;
                while ((nextLine = reader.readNext()) != null) {
                    if (nextLine != null) {
                        //Verifying the read data here

                        query = "INSERT INTO Pipeline_system VALUES (" + id + ", " + nextLine[0] + ", " + nextLine[1] + ", " + nextLine[2] + ");";
                        id++;
                        statement.execute(query);


                    }

                }

            }catch (Exception e){
                System.out.println("exception");
                //e.printStackTrace();
            }
            query = "select * from Pipeline_system;";
            rs = statement.executeQuery(query);
            while (rs.next()){
                Integer startPoint = rs.getInt("start_point");
                Integer finishPoint = rs.getInt("finish_point");
                int length = rs.getInt("length");

                if(!CheckElementInList(nodes, startPoint.toString()))
                    nodes.add(new Node(startPoint.toString()));

                if (!!CheckElementInList(nodes, startPoint.toString()))
                    nodes.add(new Node(finishPoint.toString()));

            }
            rs.close();
            rs = statement.executeQuery(query);
            while (rs.next()){
                Integer startPoint = rs.getInt("start_point"); startPoint = startPoint - 1;
                Integer startPoint2 = rs.getInt("finish_point");startPoint2 = startPoint2 - 1;
                Integer finishPoint2 = rs.getInt("start_point");
                Integer finishPoint = rs.getInt("finish_point");//finishPoint = finishPoint - 1;
                int length = rs.getInt("length");
                nodes.get(startPoint).addDestination(findNodeByNameInList(nodes, finishPoint.toString()), length);
                nodes.get(startPoint2).addDestination(findNodeByNameInList(nodes, finishPoint2.toString()), length);


              //   System.out.println((startPoint2) + "    " + findNodeByNameInList(nodes, finishPoint2.toString()) + "      " + length);
            }
            rs.close();


        }catch (SQLException se){
            System.out.println("sql exception");

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("exception s");
        }finally {

            connection.close();
        }


        for (int i = 0; i < nodes.size(); i++) {
            graph.addNode(nodes.get(i));
        }




        //graph = Dijkstra.calculateShortestPathFromSource(graph, nodes.get(0));

        // System.out.println(nodes.get(4).getShortestPath() + " " + nodes.get(4).getDistance());

        try{
            CSVReader reader = new CSVReader(new FileReader("set_of_points.csv"), ';' , '"' , 1);
            CSVWriter writer = new CSVWriter(new FileWriter("result.csv"));
            //Read CSV line by line and use the string array as you want
            String[] nextLine;
            String[] record = "ROUTE EXISTS;MINLENGTH".split(";");
            writer.writeNext(record);

            while ((nextLine = reader.readNext()) != null) {
                Integer startPoint = Integer.parseInt(nextLine[0]); //startPoint = startPoint - 1;
                Integer finishPoint = Integer.parseInt(nextLine[1]);//finishPoint = finishPoint - 1;
              //  System.out.println(nextLine[0] + "  " +  nextLine[1]);
                graph = Dijkstra.calculateShortestPathFromSource(graph, findNodeByNameInList(nodes, startPoint.toString()));
                if(findNodeByNameInList(nodes, finishPoint.toString()).getShortestPath().isEmpty()){
                    record = "FALSE;".split(";");
                    writer.writeNext(record);
                 //   System.out.println("false");
                }else {
                //    System.out.println(findNodeByNameInList(nodes, finishPoint.toString()).getShortestPath() + " -> " + findNodeByNameInList(nodes, finishPoint.toString()).getDistance());
                    record = ("TRUE;" + findNodeByNameInList(nodes, finishPoint.toString()).getDistance()).split(";");
                    writer.writeNext(record);
                }
                for (int i = 0; i < nodes.size(); i++) {
                    nodes.get(i).destinct();
                }
            }
            nodes.clear();
            writer.close();

        }catch (Exception e){
            System.out.println();
            //e.printStackTrace();
        }
    }

    private  boolean CheckElementInList(List< Node > nodes, String element){
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getName().equals(element)){
                return true;
            }
        }

        return false;
    }
    private  Node findNodeByNameInList(List < Node > nodes, String name){
        for (Node node : nodes){
            if (node.getName().equals(name)){
                return node;
            }
        }
        return null;
    }
    void createSimpleDBSchema(Connection con) {
        try (Statement stmt = con.createStatement()) {
            statement = con.createStatement();
            rs = statement.executeQuery("SHOW TABLES;");
            if (!rs.next()) {
                stmt.executeUpdate("CREATE TABLE Pipeline_system (id number , start_point number, finish_point number, length number);");
            }
            statement.close();

        } catch (SQLException e) {
            System.err.printf("%s: %s\n", e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
