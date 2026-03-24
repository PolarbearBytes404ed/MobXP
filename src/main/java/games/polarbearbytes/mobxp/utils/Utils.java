package games.polarbearbytes.mobxp.utils;
import org.jetbrains.annotations.Nullable;

public class Utils {
    public static Integer tryParse(String text, int defaultValue){
        try{
            return Integer.parseInt(text);
        } catch(Exception ignored){
            return defaultValue;
        }
    }
    public static Double tryParse(String text, Double defaultValue){
        try{
            return Double.parseDouble(text);
        } catch(Exception ignored){
            return defaultValue;
        }
    }
    public static Float tryParse(String text, Float defaultValue){
        try{
            return Float.parseFloat(text);
        } catch(Exception ignored){
            return defaultValue;
        }
    }
}
