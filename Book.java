import java.util.*;

public class Book
{
    private String title;
    private int yearOfPublication;
    private Author author;
    private ArrayList<Integer> numberOfSales = new ArrayList<Integer>();
    private int totalSales;
    private float price;
    private String country;
    private String successful;
    private String fallingSales;
    private int ID;
    private int predictedSales=-1;
    
    public Book(){}
    
    public Book(String title, int yearOfPublication, Author author, ArrayList<Integer> numberOfSales, float price, String country, int ID)
    {
        this.title=title;
        this.yearOfPublication=yearOfPublication;
        this.author=author;
        this.numberOfSales=numberOfSales;
        totalSales=0;
        for (int i=0; i<numberOfSales.size(); i++)
            totalSales += numberOfSales.get(i);
        this.price=price;
        successful = "NO";
        if (numberOfSales.size() != 0)
        {
            if (totalSales*price/numberOfSales.size() > 1000)
                successful = "YES";
        }
        fallingSales="-";
        if (numberOfSales.size() >= 3)
        {
            if (2*numberOfSales.get(numberOfSales.size()-1) < numberOfSales.get(numberOfSales.size()-2) && 2*numberOfSales.get(numberOfSales.size()-2) < numberOfSales.get(numberOfSales.size()-3))
                fallingSales="WARNING";
        }
        this.country = country;
        this.ID=ID;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public int getYearOfPublication()
    {
        return yearOfPublication;
    }
    
    public Author getAuthor()
    {
        return author;
    }
    
    public ArrayList<Integer> getNumberOfSales()
    {
        return numberOfSales;
    }
    
    public int getTotalSales()
    {
        return totalSales;
    }
    
    public float getPrice()
    {
        return price;
    }
    
    public String getCountry()
    {
        return country;
    }
    
    public String getSuccessful()
    {
        return successful;
    }
    
    public String getFallingSales()
    {
        return fallingSales;
    }
    
    public int getID()
    {
        return ID;
    }
    
    public int getPredictedSales()
    {
        return predictedSales;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public void setYearOfPublication(int yearOfPublication)
    {
        this.yearOfPublication = yearOfPublication;
    }
    
    public void setAuthor(Author author)
    {
        this.author = author;
    }
    
    public void setNumberOfSales(ArrayList<Integer> numberOfSales)
    {
        this.numberOfSales = numberOfSales;
        totalSales = 0;
        for (int i=0; i<numberOfSales.size(); i++)
            totalSales += numberOfSales.get(i);
            
        successful = "NO";
        if (numberOfSales.size() != 0)
        {
            if (totalSales*price/numberOfSales.size() > 1000)
                successful = "YES";
        }
        
        fallingSales="-";
        if (numberOfSales.size() >= 3)
        {
            if (2*numberOfSales.get(numberOfSales.size()-1) < numberOfSales.get(numberOfSales.size()-2) && 2*numberOfSales.get(numberOfSales.size()-2) < numberOfSales.get(numberOfSales.size()-3))
                fallingSales="WARNING";
        }
    }
    
    public void setPrice(float price)
    {
        this.price = price;
        
        successful = "NO";
        if (numberOfSales.size() != 0)
        {
            if (totalSales*price/numberOfSales.size() > 1000)
                successful = "YES";
        }
    }
    
    public void setCountry(String country)
    {
        this.country=country;
    }
    
    public void setID(int ID)
    {
        this.ID = ID;
    }
    
    public void setPredictedSales(Matrix W)
    {
        double[][] mat = new double[1][20];
        double m = numberOfSales.size();
        double m1 = numberOfSales.get(numberOfSales.size()-1);
        double m2 = numberOfSales.get(numberOfSales.size()-2);
        double m3 = numberOfSales.get(numberOfSales.size()-3);
        double p = price;
        mat[0][0] = m;
        mat[0][1] = m1;
        mat[0][2] = m2;
        mat[0][3] = m3;
        mat[0][4] = p;
        mat[0][5] = m*m;
        mat[0][6] = m*m1;
        mat[0][7] = m*m2;
        mat[0][8] = m*m3;
        mat[0][9] = m*p;
        mat[0][10] = m1*m1;
        mat[0][11] = m1*m2;
        mat[0][12] = m1*m3;
        mat[0][13] = m1*p;
        mat[0][14] = m2*m2;
        mat[0][15] = m2*m3;
        mat[0][16] = m2*p;
        mat[0][17] = m3*m3;
        mat[0][18] = m3*p;
        mat[0][19] = p*p;
        Matrix X = new Matrix(mat);
        predictedSales = (int)(Double.parseDouble((X.multiplyBy(W)).toString()));
        if (predictedSales<0)
            predictedSales=0;
    }
    
    public void setPredictedSales(int neg)
    {
        predictedSales=neg;
    }
    
    public String toString()
    {
        return(title + "\n" + yearOfPublication + "\n" + author + "\n" + numberOfSales + "\n" + price + "\n" + country + "\n" + ID);
    }
}