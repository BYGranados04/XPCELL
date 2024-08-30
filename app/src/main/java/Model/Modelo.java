package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Modelo implements Parcelable {
    private String nombre_modelo;

    // Constructor
    public Modelo(String nombre_modelo) {
        this.nombre_modelo = nombre_modelo;
    }

    // Getter para nombre_modelo
    public String getNombre_modelo() {
        return nombre_modelo;
    }

    // Implementar Parcelable
    protected Modelo(Parcel in) {
        nombre_modelo = in.readString();
    }

    public static final Creator<Modelo> CREATOR = new Creator<Modelo>() {
        @Override
        public Modelo createFromParcel(Parcel in) {
            return new Modelo(in);
        }

        @Override
        public Modelo[] newArray(int size) {
            return new Modelo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre_modelo);
    }
}
