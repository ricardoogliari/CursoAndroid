package curso.mpgo.com.cursoandroid;

import java.io.Serializable;

/**
 * Created by ricardoogliari on 5/10/16.
 */
public class Posicao implements Serializable{

    public int id;
    public String name;
    public double latitude;
    public double longitude;

    @Override
    public String toString() {
        return name;
    }
}
