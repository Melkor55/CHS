package Utils;

import android.widget.TextView;

public interface ArrayFunctions {


    default void printArray(Object[] array)
    {
        for (int i = 0; i < array.length; i++)
        {
            System.out.println("Array[" + i + "] = '" + array[i] + "'");

        }
    }

    default boolean checkIFArrayIsNull(Object[] array/*, int startPosition, int endPosition */)
    {
        if (array == null)
        {
            System.out.println("Array is Null");
            return true;
        }
        else if (array.length == 0)
        {
            System.out.println("Array is Empty");
            return true;
        }
        else
        {
            for (int i = 0; i < array.length; i++)
            {
                if ((array[i] == null) || (array[i].equals("")))
                {
                    System.out.println("Element '" + i + "' of the Array is Null");
                    return true;
                }
            }
        }
        return false;
    }
}
