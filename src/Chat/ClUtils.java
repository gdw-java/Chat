package Chat;

import java.io.Closeable;
import java.io.IOException;

public class ClUtils {
    /*
    * 释放资源
    * */
    public static void close(Closeable... targets) {
        for (Closeable target : targets) {
            try {
                if (null != target) {
                    target.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
