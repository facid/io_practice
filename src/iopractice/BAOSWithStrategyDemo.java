package iopractice;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class BAOSWithStrategyDemo {
    public static void main(String[] args) throws IOException {
        ByteArrayOutputStreamWithStrategy baos = new ByteArrayOutputStreamWithStrategy();

        baos.write(new byte[]{1, 2, 3, 4, 5, 6});
        baos.write(new byte[]{9, 10, 11, 12, 13, 14, 15, 16});
        baos.write(new byte[]{17, 18});

        byte[] bytes = baos.toByteArray();
        System.out.print(Arrays.toString(bytes));

        try (OutputStream out = new FileOutputStream("example.txt")){
            baos.writeTo(out);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
