public class Matrix // class Matrix responsible for predictions 
{
    private double[][] mat; //new matrix which will hold sales 
    
    Matrix(){}
    
    Matrix(double[][] mat) //instantiation 
    {
        this.mat = new double[mat.length][mat[0].length];
        for (int i=0; i<mat.length; i++)
        {
            for (int j=0; j<mat[0].length; j++)
            {
                this.mat[i][j] = mat[i][j];
            }
        }
    }
    
    public double[][] getMat()
    {
        return mat;
    }
    
    public Matrix transpose()
    {
        double[][] ret = new double[mat[0].length][mat.length];
        for (int i=0; i<mat.length; i++)
        {
            for (int j=0; j<mat[0].length; j++)
            {
                ret[j][i] = mat[i][j];
            }
        }
        return (new Matrix(ret));
    }
    
    public Matrix multiplyBy(Matrix m)
    {
        double[][] mult = new double[mat.length][m.getMat()[0].length];
        for (int i=0; i<mult.length; i++)
        {
            for (int j=0; j<mult[0].length; j++)
            {
                double temp=0;
                for(int k=0; k<mat[0].length; k++)
                {
                    temp += mat[i][k] * m.getMat()[k][j];
                }
                mult[i][j] = temp;
            }
        }
        return (new Matrix(mult));
    }
    
    public Matrix multiplyBy(double scalar)
    {
        double[][] mult = new double[mat.length][mat[0].length];
        for (int i=0; i<mult.length; i++)
        {
            for (int j=0; j<mult.length; j++)
            {
                mult[i][j] = mat[i][j]*scalar;
            }
        }
        return (new Matrix(mult));
    }
    
    
    public Matrix inverse()
    {
        double[][] temp = new double[mat.length][mat.length];
        for (int i=0; i<mat.length; i++)
        {
            for (int j=0; j<mat.length; j++)
                temp[i][j] = mat[i][j];
        }
        double[][] ret = new double[mat.length][mat.length];
        for (int i=0; i<mat.length; i++)
        {
            for (int j=0; j<mat.length; j++)
            {
                if (i==j)
                    ret[i][j]=1;
                else
                    ret[i][j]=0;
            }
        }
        for (int i=0; i<mat.length; i++)
        {
            int cur=i+1;
            while (temp[i][i] == 0)
            {
                swapRows(temp, i,cur);
                swapRows(ret, i, cur);
                cur++;
            }
            double t = temp[i][i];
            for (int j=0; j<mat.length; j++)
            {                
                temp[i][j] /= t;
                ret[i][j] /= t;
            }
            for (int j=i+1; j<mat.length; j++)
            {
                t=temp[j][i];
                for (int k=0; k<mat.length; k++)
                {
                    temp[j][k] -= (t*temp[i][k]);
                    ret[j][k] -= (t*ret[i][k]);
                }
            }
        }
        for (int i=mat.length-1; i>0; i--)
        {
            for (int j=i-1; j>=0; j--)
            {
                double t=temp[j][i];
                for (int k=0; k<mat.length; k++)
                {
                    temp[j][k] -= (t*temp[i][k]);
                    ret[j][k] -= (t*ret[i][k]);
                }
            }
        }
        
        return (new Matrix(ret));
    }
    
    private void swapRows(double[][] arr, int i, int j)
    {
        for (int k=0; k<arr[0].length; k++)
        {
            double temp = arr[i][k];
            arr[i][k] = arr[j][k];
            arr[j][k]=temp;
        }
    }
    
    public String toString()
    {
        String s="";
        for (int i=0; i<mat.length; i++)
        {
            for (int j=0; j<mat[0].length; j++)
            {
                s+=mat[i][j];
                if (j!=mat[0].length-1)
                    s+="\t";
            }
            if(i!=mat.length-1)
                s+="\n";
        }
        return s;
    }
}