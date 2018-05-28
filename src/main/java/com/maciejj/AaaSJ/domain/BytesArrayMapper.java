package com.maciejj.AaaSJ.domain;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.function.Supplier;

public class BytesArrayMapper {

    private ByteBuffer bb;
    private Supplier supplier;
    private int bytesPerOutputSample;

    public BytesArrayMapper(byte[] data, int bytesPerOutputSample) throws UnsupportedAudioFileException {
        this(bytesPerOutputSample);
        bb = ByteBuffer.wrap(data);
    }

    public BytesArrayMapper(int bytesPerOutputSample) throws UnsupportedAudioFileException {
        switch (bytesPerOutputSample){
            case 2:
                supplier = () -> (double) bb.getShort(); // TODO: this is error prone! Take type as constr. parameter and cast to that type
                break;
            default:
                throw new UnsupportedAudioFileException();
        }

        this.bytesPerOutputSample = bytesPerOutputSample;
    }

    //TODO: make this generic.
    public double[] mapToDoubleArr(){
        double[] doubles = new double[bb.remaining()/bytesPerOutputSample];

        for(int i = 0; i < doubles.length; i++){
            try{
                doubles[i] = (double) supplier.get();
            } catch (BufferUnderflowException e){
                break;
            }
        }
        return doubles;
    }

    public BytesArrayMapper updateData(byte[] data){
        bb = ByteBuffer.wrap(data);
        return this;
    }

}
