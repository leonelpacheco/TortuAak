package mx.edukweb.tortuaak;

/**
 * Created by Leonel on 04/03/2015.
 */
public class Variables {
    private static Variables mInstance= null;

    public String usuario;

    protected Variables(){}

    public static synchronized Variables getInstance(){
        if(null == mInstance){
            mInstance = new Variables();
        }
        return mInstance;
    }

}//Fin de clase
