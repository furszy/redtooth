package iop_sdk.global.utils;


import iop_sdk.global.exceptions.NotValidParametersException;

/**
 * Created by mati on 05/12/16.
 */

public class Preconditions {

    /**
     *
     * @param o1
     * @param o2
     * @param exceptionMessage
     * @throws NotValidParametersException
     */
    public static void checkEquals(Object o1,Object o2,String exceptionMessage) throws NotValidParametersException {
        if (!o1.equals(o2)) throw new NotValidParametersException(exceptionMessage);
    }

    /**
     *
     * @param obj
     * @param lessThanNumber
     * @param exceptionMessage
     * @throws NotValidParametersException
     */
    public static void compareLessThan(int obj,int lessThanNumber,String exceptionMessage){
        if (obj<lessThanNumber) throw new IllegalArgumentException(exceptionMessage);
    }


}
