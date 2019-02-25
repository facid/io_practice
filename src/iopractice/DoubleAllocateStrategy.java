package iopractice;

public class DoubleAllocateStrategy implements AllocateStrategy{
    @Override
    public int nextAfter(int now){
        return now * 2;
    }
}
