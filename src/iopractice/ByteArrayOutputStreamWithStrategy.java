package iopractice;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ByteArrayOutputStreamWithStrategy extends OutputStream {
    private static final int DEFAULT_START_SIZE = 8;
    private static final AllocateStrategy DEFAULT_ALLOCATE_STRATEGY = new DoubleAllocateStrategy();

    private final AllocateStrategy strategy;
    private final List<byte[]> bufferList = new ArrayList<>(8);
    private int count;

    public ByteArrayOutputStreamWithStrategy(){
        this(DEFAULT_START_SIZE, DEFAULT_ALLOCATE_STRATEGY);
    }

    public ByteArrayOutputStreamWithStrategy(int startSize){
        this(startSize, DEFAULT_ALLOCATE_STRATEGY);
    }

    public ByteArrayOutputStreamWithStrategy(AllocateStrategy strategy){
        this(DEFAULT_START_SIZE, strategy);
    }

    public ByteArrayOutputStreamWithStrategy(int startSize, AllocateStrategy strategy){
        this.bufferList.add(new byte[startSize]);
        this.strategy = strategy;
    }

    @Override
    public void write(int b) throws IOException{
        byte[] lastBuff = bufferList.get(bufferList.size() - 1);
        if (count == lastBuff.length){
            int newSize = strategy.nextAfter(lastBuff.length);
            byte[] newLastBuff = new byte[newSize];
            bufferList.add(newLastBuff);
            count = 0;
            lastBuff = newLastBuff;
        }
        lastBuff[count++] = (byte) b;
    }

    @Override
    public void write(byte[] bytes) throws IOException{
        write(bytes, 0, bytes.length);
    }

    @Override
    public void write(byte[] bytes, int off, int len) throws IOException{
        for (int k = off; k < off + len; k++){
            byte b = bytes[k];
            write(b & 0xFF);
        }
    }

    public void writeTo(OutputStream out) throws IOException{
        for (byte[] bytes : bufferList){
            out.write(bytes);
        }
    }

    public byte[] toByteArray() throws IOException{
        byte[] array;
        int sumLengths = 0;
        count = 0;

        for (byte[] bytes : bufferList){
            sumLengths += bytes.length;

            for (int index = 0; index < bytes.length; index++){
                if (bytes[index] == 0){
                    sumLengths -= (bytes.length - index);
                    break;
                }
            }
        }

        array = new byte[sumLengths];

        for (int indexList = 0; indexList < bufferList.size(); indexList++){

            if (indexList != bufferList.size() - 1){
                System.arraycopy(bufferList.get(indexList), 0, array, count, bufferList.get(indexList).length);
                count += bufferList.get(indexList).length;
            } else {
                System.arraycopy(bufferList.get(indexList), 0, array, count, sumLengths - count);
            }
        }

        return array;
    }
}
