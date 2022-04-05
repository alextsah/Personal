public class Author
{
    private String firstName;
    private String lastName;
    private boolean female;
    
    public Author(){}
    
    public Author(String firstName, String lastName, boolean female)
    {
        this.firstName=firstName;
        this.lastName=lastName;
        this.female=female;
    }
    
    public String getFirstName()
    {
        return firstName;
    }
    
    public String getLastName()
    {
        return lastName;
    }
    
    public boolean isFemale()
    {
        return female;
    }
    
    public void setFirstName(String firstName)
    {
        this.firstName=firstName;
    }
    
    public void setLastName(String lastName)
    {
        this.lastName=lastName;
    }
    
    public void isFemale(boolean female)
    {
        this.female=female;
    }
    
    public String toString()
    {
        String gender;
        if (female)
            gender="female";
        else
            gender="male";
            
        return(firstName + "," + lastName + "," + gender);     
    }
}