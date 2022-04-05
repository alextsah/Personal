import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.paint.*;
import javafx.scene.effect.*;
import javafx.scene.chart.*;
import javafx.scene.input.*;
import javafx.scene.image.*;
import java.awt.image.*;
import javafx.embed.swing.*;
import javafx.scene.control.cell.*;
import javafx.stage.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.util.converter.*;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.*;
import javax.imageio.ImageIO;
import java.util.logging.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.util.*;
import java.io.*; 

public class Main extends Application
{ 
    private static final String file="Books.txt"; //file from which books will be read from 
    private static ObservableList<Book> books=FXCollections.observableArrayList();
    private static Stage secStage=null; //close all secondary windows 
    private static Stage TStage=null; //close all primary windows 
    private static Stage fStage=null; //close any dialogue boxes 
    private static TextField txtTitle, txtYearOfPublication, txtAuthorFirstName, txtAuthorLastName, txtNumberOfSales, txtPrice, txtCountry, txtID;
    private static ChoiceBox cbAuthorGender; //new choice box for gender 
    private static TableView<Book> table;
    private static Pdf doc = null; //set pdf to empty 
    public static void main(String[] args) //method to initiate program 
    {
        launch(args);
    }

    @Override public void start(Stage primStage) //method to initiate primary stage
    {
        readBooks();
        predictSales();

        Label lblHeading = new Label("Books"); //title of stage 
        lblHeading.setFont(new Font("Arial", 30)); //font / size 

        table = new TableView<Book>();
        table.setItems(books);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableColumn colTitle = new TableColumn("Title"); //new column containing title 
        colTitle.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));

        TableColumn colYearOfPublication = new TableColumn("Year of Publication"); //new column containing YearOfPublication 
        colYearOfPublication.setMaxWidth(50);
        colYearOfPublication.setCellValueFactory(new PropertyValueFactory<Book, Integer>("yearOfPublication"));

        TableColumn colAuthor = new TableColumn("Author"); //new column containing Author 
        colAuthor.setCellValueFactory(new PropertyValueFactory<Book, Author>("author"));

        TableColumn colNumberOfSales = new TableColumn("Number of Sales"); //new table column containg NumberOfSales 
        colNumberOfSales.setCellValueFactory(new PropertyValueFactory<Book, ArrayList>("numberOfSales"));

        TableColumn colTotalSales = new TableColumn("Total sales"); //new column containing TotalSales 
        colTotalSales.setCellValueFactory(new PropertyValueFactory<Book, Integer>("totalSales"));

        TableColumn colPrice = new TableColumn("Price"); //new column containing Price 
        colPrice.setCellValueFactory(new PropertyValueFactory<Book, Float>("price"));

        TableColumn colCountry = new TableColumn("Country"); //new column containing Country 
        colCountry.setCellValueFactory(new PropertyValueFactory<Book, String>("country"));

        TableColumn colSuccessful = new TableColumn("Successful"); //new column containing Successful 
        colSuccessful.setCellValueFactory(new PropertyValueFactory<Book, String>("successful"));

        TableColumn colFallingSales = new TableColumn("Falling Sales"); //new column containing FallingSales 
        colFallingSales.setCellValueFactory(new PropertyValueFactory<Book, String>("fallingSales"));

        TableColumn colID = new TableColumn("ID"); //new column containing ID 
        colID.setCellValueFactory(new PropertyValueFactory<Book, Integer>("ID"));

        TableColumn colPredictedSales = new TableColumn("Predicted Sales"); //new column containing PredictedSales 
        colPredictedSales.setCellValueFactory(new PropertyValueFactory<Book, Integer>("predictedSales"));

        table.getColumns().addAll(colTitle,colYearOfPublication,colAuthor,colNumberOfSales, colTotalSales, colPrice, colCountry, colSuccessful, colFallingSales, colPredictedSales, colID);

        Button btnAdd = new Button("Add");
        btnAdd.setMinWidth(100);
        btnAdd.setOnAction(e->btnAdd_Clicked());

        Button btnDelete = new Button("Delete");
        btnDelete.setMinWidth(100);
        btnDelete.setOnAction(e->btnDelete_Clicked());

        Button btnFind = new Button("Find");
        btnFind.setMinWidth(100);
        btnFind.setOnAction(e->btnFind_Clicked());

        Button btnEdit = new Button("Edit");
        btnEdit.setMinWidth(100);
        btnEdit.setOnAction(e->btnEdit_Clicked());

        Button btnStatistics = new Button("Statistics");
        btnStatistics.setMinWidth(100);
        btnStatistics.setOnAction(e->btnStatistics_Clicked());

        VBox VB = new VBox();
        VB.setPadding(new Insets(30));
        VB.getChildren().addAll(btnAdd, btnEdit, btnFind, btnDelete, btnStatistics);

        HBox HB = new HBox();
        HB.getChildren().addAll(table, VB);

        VBox paneMain = new VBox();
        paneMain.setSpacing(10);
        paneMain.getChildren().addAll(lblHeading, HB);

        Scene scene = new Scene(paneMain);
        primStage.setScene(scene);
        primStage.setTitle("Bookstore");
        primStage.show();
    }

    @Override public void stop()
    {
        int n=books.size();
        try
        {
            PrintWriter outFile = new PrintWriter(new FileWriter(file));

            outFile.println(n);

            for (int i=0; i<n; i++)
                outFile.println(books.get(i));

            outFile.close();
        }
        catch (IOException io)
        {
            System.out.println("Error writing to the data file - " + io.getMessage());
        }
    }

    public void readBooks()
    {
        try{
            BufferedReader inFile = new BufferedReader(new FileReader(file)); //txt line reader 

            int elements = Integer.parseInt(inFile.readLine());
            for (int i=0; i<elements; i++) //loop through all elements in txt file
            {
                String line;

                String title = inFile.readLine(); //read string on first line (title)
                int yearOfPublication = Integer.parseInt(inFile.readLine()); //read yearOfPublication
                String[] temp = inFile.readLine().split(",");
                String firstName=temp[0], lastName=temp[1];
                boolean female; //boolean female initially set to true 
                if (temp[2]=="female")
                    female=true;
                else
                    female=false; //if "male" set boolean to false 
                temp = inFile.readLine().split(",");
                ArrayList<Integer> numberOfSales = new ArrayList<Integer>();
                for (int t=0; t<temp.length-1; t++) //read all elements of ArrayList 
                    numberOfSales.add(Integer.parseInt(temp[t].substring(1,temp[t].length())));
                int t=temp.length-1;
                numberOfSales.add(Integer.parseInt(temp[t].substring(1, temp[t].length()-1)));
                float price = Float.parseFloat(inFile.readLine()); //read price 
                String country = inFile.readLine(); //read country 
                int ID = Integer.parseInt(inFile.readLine()); //read ID 

                Author author = new Author(firstName, lastName, female);
                Book b = new Book(title, yearOfPublication, author, numberOfSales, price, country, ID);
                books.add(b);
            }

            inFile.close();
        }
        catch (IOException io) //if error is found 
        {
            return; //return error code 
        }
    }

    public void predictSales()
    {
        Matrix X, Y;
        int n =books.size();
        for (int i=0; i<books.size(); i++)
        {
            if (books.get(i).getNumberOfSales().size() < 4)
                n--;
        }
        double[][] mat = new double[n][20];
        double[][] maty = new double[n][1];
        int ind=0;
        for (int i=0; i<books.size(); i++)
        {
            if (books.get(i).getNumberOfSales().size() >= 4)
            {
                double m = (double)(books.get(i).getNumberOfSales().size()-1);
                double m1 = (double)(books.get(i).getNumberOfSales().get(books.get(i).getNumberOfSales().size()-2));
                double m2 = (double)(books.get(i).getNumberOfSales().get(books.get(i).getNumberOfSales().size()-3));
                double m3 = (double)(books.get(i).getNumberOfSales().get(books.get(i).getNumberOfSales().size()-4));
                double p = (double)(books.get(i).getPrice());
                mat[ind][0] = m;
                mat[ind][1] = m1;
                mat[ind][2] = m2;
                mat[ind][3] = m3;
                mat[ind][4] = p;
                mat[ind][5] = m*m;
                mat[ind][6] = m*m1;
                mat[ind][7] = m*m2;
                mat[ind][8] = m*m3;
                mat[ind][9] = m*p;
                mat[ind][10] = m1*m1;
                mat[ind][11] = m1*m2;
                mat[ind][12] = m1*m3;
                mat[ind][13] = m1*p;
                mat[ind][14] = m2*m2;
                mat[ind][15] = m2*m3;
                mat[ind][16] = m2*p;
                mat[ind][17] = m3*m3;
                mat[ind][18] = m3*p;
                mat[ind][19] = p*p;
                maty[ind][0] = books.get(i).getNumberOfSales().get(books.get(i).getNumberOfSales().size()-1);
                ind++;
            }
        }
        X=new Matrix(mat);
        Y=new Matrix(maty);
        Matrix W = (X.transpose().multiplyBy(X));
        if (n<=20)
        {
            for (int i=0; i<W.getMat().length; i++)
                W.getMat()[i][i]+=0.001;
        }
        W = ((W.inverse()).multiplyBy(X.transpose())).multiplyBy(Y);
        for (int i=0; i<books.size(); i++)
        {
            if(books.get(i).getNumberOfSales().size()>=3)
                books.get(i).setPredictedSales(W);
            else
                books.get(i).setPredictedSales(-1);
        }
    }

    public void btnAdd_Clicked()
    {
        if (secStage != null)
            secStage.close();
        secStage = null;

        HBox txts = txtsHBox();
        table.getSelectionModel().clearSelection();

        Button btnAdd2 = new Button("Add");
        btnAdd2.setMinWidth(150);
        btnAdd2.setOnAction(e->btnAdd2_Clicked());

        Button btnCancel = new Button("Cancel");
        btnCancel.setMinWidth(150);
        btnCancel.setOnAction(e->btnCancel_Clicked());

        HBox btns = new HBox();
        btns.getChildren().addAll(btnAdd2, btnCancel);
        btns.setAlignment(Pos.BASELINE_RIGHT);

        VBox win = new VBox();
        win.getChildren().addAll(txts, btns);
        win.setSpacing(10);

        Scene sc = new Scene(win);
        secStage = new Stage();
        secStage.setScene(sc);
        secStage.setTitle("Add book");
        secStage.show();
    }

    public void btnDelete_Clicked()
    {
        if (secStage != null)
            secStage.close();
        secStage=null;

        if (table.getSelectionModel().getSelectedItems().isEmpty())
        {
            errorWindow("No item selected");
            return;
        }

        Label lblQuestion;
        int num = table.getSelectionModel().getSelectedItems().size();
        if (num == 1)
            lblQuestion = new Label("Are you sure you want to delete the selected item?");
        else
            lblQuestion = new Label("Are you sure you want to delete " + num + " selected items?");
        lblQuestion.setFont(new Font("Arial", 20));

        Button btnYes = new Button("Confirm");
        btnYes.setMinWidth(150);
        btnYes.setOnAction(e->btnYes_Clicked());

        Button btnNo = new Button("Cancel");
        btnNo.setMinWidth(150);
        btnNo.setOnAction(e->btnCancel_Clicked());

        HBox answers = new HBox();
        answers.getChildren().addAll(btnYes, btnNo);
        answers.setAlignment(Pos.BASELINE_CENTER);

        VBox total = new VBox();
        total.setSpacing(20);
        total.getChildren().addAll(lblQuestion, answers);

        Scene sc = new Scene(total);
        secStage = new Stage();
        secStage.setScene(sc);
        secStage.setTitle("Confirmation");
        secStage.show();
    }

    public void btnFind_Clicked()
    {
        if (secStage != null)
            secStage.close();
        secStage = null;

        HBox txts = txtsHBox();

        Button btnFind2 = new Button("Find");
        btnFind2.setMinWidth(150);
        btnFind2.setOnAction(e->btnFind2_Clicked());

        Button btnCancel = new Button("Cancel");
        btnCancel.setMinWidth(150);
        btnCancel.setOnAction(e->btnCancel_Clicked());

        HBox btns = new HBox();
        btns.getChildren().addAll(btnFind2, btnCancel);
        btns.setAlignment(Pos.BASELINE_RIGHT);

        VBox win = new VBox();
        win.getChildren().addAll(txts, btns);
        win.setSpacing(10);

        Scene sc = new Scene(win);
        secStage = new Stage();
        secStage.setScene(sc);
        secStage.setTitle("Find book");
        secStage.show();
    }

    public void btnStatistics_Clicked()
    {
        if (TStage != null)
            TStage.close();
        TStage = null;

        doc = new Pdf();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Book Title");
        xAxis.setAnimated(false);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Sales");
        yAxis.setAnimated(false);
        BarChart<String, Number> bookSalesChart = new BarChart<String, Number>(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        for (int i=0; i<books.size(); i++)
            series.getData().add(new XYChart.Data(books.get(i).getTitle(), books.get(i).getTotalSales()));
        bookSalesChart.getData().add(series);
        bookSalesChart.setTitle("Total Book Sales");

        CategoryAxis xAxis1 = new CategoryAxis();
        xAxis1.setLabel("Book Title");
        xAxis1.setAnimated(false);
        NumberAxis yAxis1 = new NumberAxis();
        yAxis1.setLabel("Revenue");
        yAxis1.setAnimated(false);
        BarChart<String, Number> bookRevenueChart = new BarChart<String, Number>(xAxis1, yAxis1);
        XYChart.Series series1 = new XYChart.Series();
        for (int i=0; i<books.size(); i++)
            series1.getData().add(new XYChart.Data(books.get(i).getTitle(), books.get(i).getTotalSales()*books.get(i).getPrice()));
        bookRevenueChart.getData().add(series1);
        bookRevenueChart.setTitle("Total Book Revenue");

        LineChart[] lineCharts = new LineChart[books.size()];
        for (int i=0; i<books.size(); i++)
        {
            NumberAxis xAx = new NumberAxis();
            xAx.setLabel("Month");
            xAx.setAnimated(false);
            NumberAxis yAx = new NumberAxis();
            yAx.setLabel("Sales");
            yAx.setAnimated(false);
            lineCharts[i] = new LineChart(xAx, yAx);

            XYChart.Series ser = new XYChart.Series();
            for (int j=0; j<books.get(i).getNumberOfSales().size(); j++)
                ser.getData().add(new XYChart.Data(j,books.get(i).getNumberOfSales().get(j)));
            lineCharts[i].getData().add(ser);
            lineCharts[i].setTitle(books.get(i).getTitle());
        }

        LineChart[] lineCharts2 = new LineChart[books.size()];
        for (int i=0; i<books.size(); i++)
        {
            NumberAxis xAx = new NumberAxis();
            xAx.setLabel("Month");
            xAx.setAnimated(false);
            NumberAxis yAx = new NumberAxis();
            yAx.setLabel("Revenue");
            yAx.setAnimated(false);
            lineCharts2[i] = new LineChart(xAx, yAx);

            XYChart.Series ser = new XYChart.Series();
            for (int j=0; j<books.get(i).getNumberOfSales().size(); j++)
                ser.getData().add(new XYChart.Data(j,books.get(i).getNumberOfSales().get(j)*books.get(i).getPrice()));
            lineCharts2[i].getData().add(ser);
            lineCharts2[i].setTitle(books.get(i).getTitle());
        }

        VBox charts = new VBox();
        charts.setPadding(new Insets(100));
        charts.setSpacing(100);
        charts.getChildren().addAll(bookSalesChart, bookRevenueChart);
        for (int i=0; i<books.size(); i++)
            charts.getChildren().addAll(lineCharts[i], lineCharts2[i]);

        ScrollPane sp = new ScrollPane();
        sp.setContent(charts);
        sp.setMaxHeight(600);

        Scene temp = new Scene(sp);
        if (TStage != null)
            TStage.close();
        TStage = null;
        TStage = new Stage();
        TStage.setScene(temp);
        TStage.setTitle("Stats");
        TStage.show();

        doc.addChart(bookSalesChart);
        doc.addChart(bookRevenueChart);
        for(int i=0; i<lineCharts.length; i++)
        {
            doc.addChart(lineCharts[i]);
            doc.addChart(lineCharts2[i]);
        }
        try {

            doc.getDoc().save("pdf_file.pdf");
            doc.getDoc().close();
        } catch (IOException ex) {Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);}

        if (fStage != null)
            fStage.close();
        fStage=null;
        Label lblEmail = new Label("Results saved in pdf. Do you want to email pdf?");
        Button btnEmail = new Button("Yes");
        btnEmail.setOnAction(e->onBtnEmail_Clicked());
        Button btnNotEmail = new Button("No");
        btnNotEmail.setOnAction(e->onBtnNotEmail_Clicked()); 
        HBox h = new HBox();
        h.getChildren().addAll(btnEmail, btnNotEmail);
        VBox v = new VBox();
        v.getChildren().addAll(lblEmail, h);
        Scene s = new Scene(v);
        fStage = new Stage();
        fStage.setScene(s);
        fStage.setTitle("Email");
        fStage.show();
    }

    public void btnEdit_Clicked()
    {
        if (secStage != null)
            secStage.close();
        secStage = null;

        if (table.getSelectionModel().getSelectedItems().isEmpty())
        {
            errorWindow("No item selected");
            return;
        }
        else if (table.getSelectionModel().getSelectedItems().size() != 1)
        {
            errorWindow("More than one items selected");
            return;
        }

        HBox hbox = new HBox();
        Book tb=table.getSelectionModel().getSelectedItems().get(0);
        int initID=tb.getID();

        txtTitle = new TextField();
        txtTitle.setText(tb.getTitle());
        txtTitle.setFocusTraversable(false);

        txtYearOfPublication = new TextField();
        txtYearOfPublication.setText(Integer.toString(tb.getYearOfPublication()));
        txtYearOfPublication.setFocusTraversable(false);

        txtAuthorFirstName = new TextField();
        txtAuthorFirstName.setText(tb.getAuthor().getFirstName());
        txtAuthorFirstName.setFocusTraversable(false);

        txtAuthorLastName = new TextField();
        txtAuthorLastName.setText(tb.getAuthor().getLastName());
        txtAuthorLastName.setFocusTraversable(false);

        cbAuthorGender = new ChoiceBox(FXCollections.observableArrayList("Male", "Female"));
        if (tb.getAuthor().isFemale())
            cbAuthorGender.getSelectionModel().select(1);
        else
            cbAuthorGender.getSelectionModel().select(0);

        txtNumberOfSales = new TextField();
        String mSales ="";
        for (int j=0; j<tb.getNumberOfSales().size(); j++)
        {
            mSales += (Integer.toString(tb.getNumberOfSales().get(j)) + ",");
        }
        txtNumberOfSales.setText(mSales);
        txtNumberOfSales.setFocusTraversable(false);

        txtPrice = new TextField();
        txtPrice.setText(Float.toString(tb.getPrice()));
        txtPrice.setFocusTraversable(false);

        txtCountry = new TextField();
        txtCountry.setText(tb.getCountry());
        txtCountry.setFocusTraversable(false);

        txtID = new TextField();
        txtID.setText(Integer.toString(tb.getID()));
        txtID.setFocusTraversable(false);

        hbox.getChildren().addAll(txtTitle, txtYearOfPublication, txtAuthorFirstName, txtAuthorLastName, cbAuthorGender, txtNumberOfSales, txtPrice, txtCountry, txtID);

        Button btnDone = new Button("Done");
        btnDone.setMinWidth(150);
        btnDone.setOnAction(e->btnDone_Clicked(hbox, initID));

        Button btnCancel = new Button("Cancel");
        btnCancel.setMinWidth(150);
        btnCancel.setOnAction(e->btnCancel_Clicked());

        HBox btns = new HBox();
        btns.getChildren().addAll(btnDone, btnCancel);
        btns.setAlignment(Pos.BASELINE_RIGHT);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(hbox,btns);
        vbox.setSpacing(10);

        Scene sc = new Scene(vbox);
        secStage = new Stage();
        secStage.setScene(sc);
        secStage.setTitle("Edit");
        secStage.show();
    }

    public void btnAdd2_Clicked()
    {
        String title=txtTitle.getText();

        int yearOfPublication=0;
        try
        {
            yearOfPublication=Integer.parseInt(txtYearOfPublication.getText());
        }
        catch(Exception e)
        {
            errorWindow("Not valid year of publication");
            return;
        }
        if(yearOfPublication < 0 || yearOfPublication > 2019)
        {
            errorWindow("Not valid year of publication");
            return;
        }

        String firstName=txtAuthorFirstName.getText();
        if(!isName(firstName))
        {
            errorWindow("Not valid author's first name");
            return;
        }

        String lastName=txtAuthorLastName.getText();
        if(!isName(lastName))
        {
            errorWindow("Not valid author's last name");
            return;
        }

        boolean female;
        if (cbAuthorGender.getValue() == "Female")
            female=true;
        else if (cbAuthorGender.getValue() == "Male")
            female=false;
        else
        {
            errorWindow("Not valid author's gender");
            return;
        }

        ArrayList<Integer> numberOfSales = new ArrayList<Integer>();
        String[] temp = txtNumberOfSales.getText().split(",");
        for (int t=0; t<temp.length; t++)
        {
            try
            {
                numberOfSales.add(Integer.parseInt(temp[t]));
            }
            catch(Exception e)
            {
                errorWindow("Not valid number of sales");
                return;
            }

            if(numberOfSales.get(numberOfSales.size()-1) < 0)
            {
                errorWindow("Not valid number of sales");
                return;
            }
        }

        float price=0;
        try
        {
            price = Float.parseFloat(txtPrice.getText());
        }
        catch(Exception e)
        {
            errorWindow("Not valid price");
            return;
        }
        if(price<=0)
        {
            errorWindow("Not valid price");
            return;
        }

        String country = txtCountry.getText();
        if(!isName(country))
        {
            errorWindow("Not valid country");
            return;
        }

        int ID=-1;
        try
        {
            ID = Integer.parseInt(txtID.getText());
        }
        catch(Exception e)
        {
            errorWindow("Not valid ID");
            return;
        }
        if(ID<0)
        {
            errorWindow("Not valid ID");
            return;
        }
        for (int i=0; i<books.size(); i++)
        {
            if(books.get(i).getID() == ID)
            {
                errorWindow("ID already exists");
                return;
            }
        }

        Author author = new Author(firstName, lastName, female);
        Book b = new Book(title, yearOfPublication, author, numberOfSales, price, country, ID);
        books.add(b);

        table.getSelectionModel().select(books.size()-1);

        secStage.close();
        secStage=null;
    }

    public void btnCancel_Clicked()
    {
        secStage.close();
        secStage=null;
    }

    public void btnYes_Clicked()
    {
        ArrayList<Book> arrli = new ArrayList<Book>();
        for (int i=0; i<table.getSelectionModel().getSelectedItems().size(); i++)
            arrli.add(table.getSelectionModel().getSelectedItems().get(i));

        for (int i=0; i<arrli.size(); i++)
        {
            for (int j=0; j<books.size(); j++)
            {
                if (arrli.get(i).getID() == books.get(j).getID())
                {
                    books.remove(j);
                    break;
                }    
            }
        }

        table.getSelectionModel().clearSelection();

        secStage.close();
        secStage=null;
    }

    public HBox txtsHBox()
    {
        txtTitle = new TextField();
        txtTitle.setPromptText("Title");
        txtTitle.setFocusTraversable(false);

        txtYearOfPublication = new TextField();
        txtYearOfPublication.setPromptText("Year of Publication");
        txtYearOfPublication.setFocusTraversable(false);

        txtAuthorFirstName = new TextField();
        txtAuthorFirstName.setPromptText("Author's First Name");
        txtAuthorFirstName.setFocusTraversable(false);

        txtAuthorLastName = new TextField();
        txtAuthorLastName.setPromptText("Author's Last Name");
        txtAuthorLastName.setFocusTraversable(false);

        cbAuthorGender = new ChoiceBox(FXCollections.observableArrayList("Author's Gender", "Male", "Female"));
        cbAuthorGender.getSelectionModel().selectFirst();

        txtNumberOfSales = new TextField();
        txtNumberOfSales.setPromptText("Number of sales");
        txtNumberOfSales.setFocusTraversable(false);

        txtPrice = new TextField();
        txtPrice.setPromptText("Price");
        txtPrice.setFocusTraversable(false);

        txtCountry = new TextField();
        txtCountry.setPromptText("Country");
        txtCountry.setFocusTraversable(false);

        txtID = new TextField();
        txtID.setPromptText("ID");
        txtID.setFocusTraversable(false);

        HBox txts = new HBox();
        txts.getChildren().addAll(txtTitle, txtYearOfPublication, txtAuthorFirstName, txtAuthorLastName, cbAuthorGender, txtNumberOfSales, txtPrice, txtCountry, txtID);

        return txts;
    }

    public void btnFind2_Clicked()
    {
        table.getSelectionModel().clearSelection();

        String title=txtTitle.getText();
        int yearOfPublication=-1;
        if(!txtYearOfPublication.getText().equals("")) 
        {
            try
            {
                yearOfPublication=Integer.parseInt(txtYearOfPublication.getText());
            }
            catch(Exception e)
            {
                errorWindow("Not valid year of publication");
                return;
            }

            if(yearOfPublication > 2019 || yearOfPublication < 0)
            {
                errorWindow("Not valid year of publication");
                return;
            }
        }

        String firstName=txtAuthorFirstName.getText();
        if (!isName(firstName) && !firstName.equals(""))
        {
            errorWindow("Not valid author's first name");
            return;
        }

        String lastName=txtAuthorLastName.getText();
        if (!isName(lastName) && !lastName.equals(""))
        {
            errorWindow("Not valid author's last name");
            return;
        }

        boolean female=true;
        boolean gender=true;
        if (cbAuthorGender.getValue().equals("Female"))
            female=true;
        else if (cbAuthorGender.getValue().equals("Male"))
            female=false;
        else
            gender=false;

        ArrayList<Integer> numberOfSales = new ArrayList<Integer>();
        if(txtNumberOfSales.getText().equals(""))
            numberOfSales=null;
        else
        {
            String[] temp = txtNumberOfSales.getText().split(",");
            for (int t=0; t<temp.length; t++)
            {
                try
                {
                    numberOfSales.add(Integer.parseInt(temp[t]));
                }
                catch(Exception e)
                {
                    errorWindow("Not valid number of sales");
                    return;
                }

                if(numberOfSales.get(numberOfSales.size() - 1) < 0)
                {
                    errorWindow("Not valid number of sales");
                    return;
                }
            }
        }
        float price = -1;
        if(!txtPrice.getText().equals(""))
        {
            try
            {
                price = Float.parseFloat(txtPrice.getText());
            }
            catch(Exception e)
            {
                errorWindow("Not valid price");
                return;
            }

            if (price<0)
            {
                errorWindow("Not valid price");
                return;
            }
        }

        String country = txtCountry.getText();
        if(!isName(country) && !country.equals(""))
        {
            errorWindow("Not valid country");
            return;
        }

        int ID = -1;
        if(!txtID.getText().equals(""))
        {
            try
            {
                ID = Integer.parseInt(txtID.getText());
            }
            catch(Exception e)
            {
                errorWindow("Not valid ID");
                return;
            }

            if(ID<0)
            {
                errorWindow("Not valid ID");
                return;
            }
        }

        boolean results=false;
        for (int i=0; i<books.size(); i++)
        {
            if (table.getItems().get(i).getTitle().contains(title) || title.equals(""))
            {
                if (table.getItems().get(i).getYearOfPublication() == yearOfPublication || yearOfPublication==-1)
                {
                    if (table.getItems().get(i).getAuthor().getFirstName().contains(firstName) || firstName.equals(""))
                    {
                        if (table.getItems().get(i).getAuthor().getLastName().contains(lastName) ||lastName.equals(""))
                        {
                            if (table.getItems().get(i).getAuthor().isFemale() == female || !gender)
                            {
                                boolean sameNumber=true;
                                if(numberOfSales!=null)
                                {
                                    if (numberOfSales.size() != table.getItems().get(i).getNumberOfSales().size())
                                        sameNumber=false;
                                    else
                                    {
                                        for (int j=0; j<numberOfSales.size(); j++)
                                        {
                                            if(numberOfSales.get(j) != table.getItems().get(i).getNumberOfSales().get(j))
                                            {
                                                sameNumber=false;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if(sameNumber)
                                {
                                    if(table.getItems().get(i).getPrice()==price || price==-1)
                                    {
                                        if(table.getItems().get(i).getCountry().equals(country) || country.equals(""))
                                        {
                                            if(table.getItems().get(i).getID() == ID || ID==-1)
                                            {
                                                table.getSelectionModel().select(i);
                                                table.getFocusModel().focus(i);
                                                results=true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if(!results)
        {
            errorWindow("No items match search");
            return;
        }

        secStage.close();
        secStage=null;
    }

    public void btnDone_Clicked(HBox hbox, int initID)
    {

        String title= ((TextField)hbox.getChildren().get(0)).getText();

        int yearOfPublication=-1;
        try
        {
            yearOfPublication=Integer.parseInt(((TextField)hbox.getChildren().get(1)).getText());
        }
        catch(Exception e)
        {
            errorWindow("Not valid year of publication");
            return;
        }
        if(yearOfPublication<0 || yearOfPublication>2019)
        {
            errorWindow("Not valid year of publication");
            return;
        }

        String firstName=((TextField)hbox.getChildren().get(2)).getText();
        if(!isName(firstName))
        {
            errorWindow("Not valid author's first name");
            return;
        }

        String lastName=((TextField)hbox.getChildren().get(3)).getText();
        if(!isName(lastName))
        {
            errorWindow("Not valid author's last name");
            return;
        }

        boolean female;
        if (((ChoiceBox)hbox.getChildren().get(4)).getValue() == "Female")
            female=true;
        else
            female=false;

        ArrayList<Integer> numberOfSales = new ArrayList<Integer>();
        String[] temp = ((TextField)hbox.getChildren().get(5)).getText().split(",");

        for (int t=0; t<temp.length; t++)
        {
            try
            {
                numberOfSales.add(Integer.parseInt(temp[t]));
            }
            catch(Exception e)
            {
                errorWindow("Not valid number of sales");
                return;
            }

            if(numberOfSales.get(numberOfSales.size()-1) < 0)
            {
                errorWindow("Not valid number of sales");
                return;
            }
        }

        float price=-1;
        try
        {
            price = Float.parseFloat(((TextField)hbox.getChildren().get(6)).getText());
        }
        catch(Exception e)
        {
            errorWindow("Not valid price");
            return;
        }
        if(price<=0)
        {
            errorWindow("Not valid price");
            return;
        }

        String country = ((TextField)hbox.getChildren().get(7)).getText();
        if(!isName(country))
        {
            errorWindow("Not valid country");
            return;
        }

        int ID=-1;
        try
        {
            ID = Integer.parseInt(((TextField)hbox.getChildren().get(8)).getText());
        }
        catch(Exception e)
        {
            errorWindow("Not valid ID");
            return;
        }
        if(ID<0)
        {
            errorWindow("ID already exists");
            return;
        }
        for(int j=0; j<table.getItems().size(); j++)
        {
            if(table.getItems().get(j).getID() != initID && table.getItems().get(j).getID()==ID)
            {
                errorWindow("ID is not unique");
                return;
            }
        }

        Author author = new Author(firstName, lastName, female);
        Book tempBook = new Book(title, yearOfPublication, author, numberOfSales, price, country, ID);

        for (int i=0; i<books.size(); i++)
        {
            if(books.get(i).getID() == initID)
            {
                books.remove(i);
                books.add(tempBook);
                break;
            }
        }

        table.getSelectionModel().clearSelection();
        table.getSelectionModel().select(books.size()-1);

        secStage.close();
        secStage = null;
    }

    public void errorWindow(String error)
    {
        if (secStage!=null)
            secStage.close();
        secStage=null;

        Label lblError = new Label(error);
        lblError.setFont(new Font("Arial", 20));

        VBox l = new VBox();
        l.getChildren().add(lblError);
        l.setAlignment(Pos.BASELINE_CENTER);

        Scene sc = new Scene(l);
        secStage = new Stage();
        secStage.setScene(sc);
        secStage.setTitle("Error");
        secStage.setMinWidth(300);
        secStage.setMinHeight(100);
        secStage.show();
    }

    public boolean isName(String name)
    {
        if(name.length() < 2)
            return false;

        if(!Character.isUpperCase(name.charAt(0)))
            return false;

        for (int i=1; i<name.length(); i++)
        {
            if(!Character.isLowerCase(name.charAt(i)))
                return false;
        }

        return true;
    }

    public void onBtnEmail_Clicked()
    {
        final String em = "alextsah12@gmail.com"; //email address 
        final String password = "Tsaha2002%"; //password of email address

        Properties prop = System.getProperties();
        prop.setProperty("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        Session ses = Session.getDefaultInstance(prop, new javax.mail.Authenticator(){protected PasswordAuthentication getPasswordAuthentication(){return new PasswordAuthentication(em, password);}});
        try
        {
            MimeMessage mes = new MimeMessage(ses);
            mes.setFrom(new InternetAddress(em));
            mes.addRecipient(Message.RecipientType.TO, new   InternetAddress(em));
            mes.setSubject("Stats pdf"); //set email subject  
            MimeBodyPart mesBody = new MimeBodyPart();
            DataSource source = new FileDataSource("pdf_file.pdf"); //locate pdf file 
            mesBody.setDataHandler(new DataHandler(source)); //attach pdf file 
            mesBody.setFileName("pdf_file.pdf"); 
            Multipart mult = new MimeMultipart();
            mult.addBodyPart(mesBody); //add body part 
            mes.setContent(mult); //add content 
            Transport.send(mes); //send email 
        }
        catch(MessagingException ex)
        {
            ex.printStackTrace(); //return error in sending 
        }
        fStage.close();
    }

    public void onBtnNotEmail_Clicked()
    {
        fStage.close();
        fStage=null;
    }
}